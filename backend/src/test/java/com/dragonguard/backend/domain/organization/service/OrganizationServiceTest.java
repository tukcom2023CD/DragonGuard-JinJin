package com.dragonguard.backend.domain.organization.service;

import com.dragonguard.backend.domain.email.entity.Email;
import com.dragonguard.backend.domain.email.repository.EmailRepository;
import com.dragonguard.backend.domain.email.service.EmailService;
import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.domain.member.repository.MemberRepository;
import com.dragonguard.backend.domain.member.service.MemberService;
import com.dragonguard.backend.domain.organization.dto.request.AddMemberRequest;
import com.dragonguard.backend.domain.organization.dto.request.OrganizationRequest;
import com.dragonguard.backend.domain.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.domain.organization.entity.Organization;
import com.dragonguard.backend.domain.organization.entity.OrganizationStatus;
import com.dragonguard.backend.domain.organization.entity.OrganizationType;
import com.dragonguard.backend.domain.organization.repository.OrganizationRepository;
import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.support.database.DatabaseTest;
import com.dragonguard.backend.support.database.LoginTest;
import com.dragonguard.backend.support.fixture.member.entity.MemberFixture;
import com.dragonguard.backend.support.fixture.organization.entity.OrganizationFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@DatabaseTest
@DisplayName("Organization 서비스의")
class OrganizationServiceTest extends LoginTest {
    @Autowired private OrganizationService organizationService;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private EmailRepository emailRepository;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @MockBean private EmailService emailService;


    @Nested
    @DisplayName("조직 저장 로직 중")
    class SaveOrg {
        @Test
        @DisplayName("처음 저장할때 로직이 수행되는가")
        void saveOrganizationWhenNotExists() {
            //given

            //when
            IdResponse<Long> result = organizationService.saveOrganization(new OrganizationRequest("한국공학대학교", OrganizationType.UNIVERSITY, "tukorea.ac.kr"));

            //then
            Optional<Organization> oTuk = organizationRepository.findById(result.getId());
            assertThat(oTuk).isNotEmpty();
        }

        @Test
        @DisplayName("이미 저장돼있을때 로직이 수행되는가")
        void saveOrganizationWhenExists() {
            //given
            Organization google = organizationRepository.save(OrganizationFixture.GOOGLE.toEntity());

            //when
            IdResponse<Long> result = organizationService.saveOrganization(new OrganizationRequest("Google", OrganizationType.COMPANY, "gmail.com"));

            //then
            Optional<Organization> oGoogle = organizationRepository.findById(google.getId());
            assertThat(oGoogle).isNotEmpty().contains(google);
            assertThat(result.getId()).isEqualTo(oGoogle.orElse(null).getId());
        }
    }

    @Nested
    @DisplayName("조직 upsert 로직 중")
    class GetOrSaveOrg {
        @Test
        @DisplayName("해당 조직이 DB에 있을때 로직이 수행되는가")
        void getOrganization() {
            //given
            Organization google = organizationRepository.save(OrganizationFixture.GOOGLE.toEntity());

            //when
//            Organization result = organizationService.getOrSaveOrganization(new OrganizationRequest("Google", OrganizationType.COMPANY, "gmail.com"));

            //then
//            assertThat(google).isEqualTo(result);
        }

        @Test
        @DisplayName("해당 조직이 DB에 없을때 로직이 수행되는가")
        void saveAndGetOrganization() {
            //given

            //when
//            Organization result = organizationService.getOrSaveOrganization(new OrganizationRequest("Google", OrganizationType.COMPANY, "gmail.com"));

            //then
//            Optional<Organization> tukResult = organizationRepository.findById(result.getId());
//            assertThat(tukResult).isNotEmpty();
        }
    }

    @Test
    @DisplayName("조직에 멤버 추가 및 이메일 전송이 수행되는가")
    void addMemberAndSendEmail() {
        //given
        Organization org = organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());
        Email email = Email.builder().memberId(loginUser.getId()).code(11111).build();
        Email savedEmail = emailRepository.save(email);
        when(emailService.sendAndSaveEmail()).thenReturn(new IdResponse<>(savedEmail.getId()));

        //when
        IdResponse<Long> result = organizationService.addMemberAndSendEmail(new AddMemberRequest(org.getId(), "tukorea.ac.kr"));

        //then
        Optional<Email> emailResult = emailRepository.findById(result.getId());
        Optional<Organization> orgResult = organizationRepository.findById(org.getId());
        memberService.getLoginUserWithPersistence().finishAuth();

        assertThat(emailResult).isNotEmpty();
        assertThat(emailResult.orElse(null).getMemberId()).isEqualTo(loginUser.getId());
        assertThat(orgResult).isNotEmpty();
    }

    @Test
    @DisplayName("멤버 조회후 조직에 추가가 수행되는가")
    void findAndAddMember() {
        //given
        Organization org = organizationRepository.save(OrganizationFixture.TUKOREA.toEntity());

        //when
        org.addMember(loginUser, "tukorea.ac.kr");

        Member member = memberService.getLoginUserWithPersistence();
        member.finishAuth();

        //then
        assertThat(org.getMembers()).contains(loginUser);
    }

    @Test
    @DisplayName("조직의 타입(대학/회사/고등학교/ETC) 조회가 수행되는가")
    void getTypes() {
        //given

        //when
//        List<OrganizationType> result = organizationService.getTypes();

        //then
//        assertThat(result).containsAll(Arrays.asList(OrganizationType.values()));
    }

    @Nested
    @DisplayName("유형별 조회 중에")
    class findOrg {

        @BeforeEach
        void init() {
            List.of(MemberFixture.SAMMUELWOOJAE.toEntity(),
                    MemberFixture.POSITE.toEntity(),
                    MemberFixture.HJ39.toEntity())
                    .forEach(memberRepository::save);

            List.of(OrganizationFixture.TUKOREA.toEntity(),
                    OrganizationFixture.GOOGLE.toEntity(),
                    OrganizationFixture.SNU.toEntity())
                    .forEach(organizationRepository::save);

            List<Member> members = memberRepository.findAll();

            Organization org = organizationRepository.findByName(OrganizationFixture.TUKOREA.getName()).orElse(null);
            assert org != null;

            organizationRepository.findAllByType(OrganizationType.COMPANY, PageRequest.of(0, 20))
                    .forEach(com -> com.updateStatus(OrganizationStatus.ACCEPTED));
            organizationRepository.findAllByType(OrganizationType.UNIVERSITY, PageRequest.of(0, 20))
                    .forEach(univ -> univ.updateStatus(OrganizationStatus.ACCEPTED));

            members.forEach(member -> {
                org.addMember(member, member.getName() + "@" + org.getEmailEndpoint());
                member.finishAuth();
            });
        }

        @Test
        @DisplayName("조직 타입별 조회가 수행되는가")
        void findByType() {
            //given

            //when
            List<OrganizationResponse> companyResult = organizationService.findByType(OrganizationType.COMPANY, PageRequest.of(0, 20));
            List<OrganizationResponse> universityResult = organizationService.findByType(OrganizationType.UNIVERSITY, PageRequest.of(0, 20));

            //then
            assertThat(companyResult).hasSize(1);
            assertThat(universityResult).hasSize(2);
        }

        @Test
        @DisplayName("전체 조직 랭킹 조회가 수행되는가")
        void getOrganizationRank() {
            //given

            //when
            List<OrganizationResponse> result = organizationService.getOrganizationRank(PageRequest.of(0, 20));

            //then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("타입별 랭킹 조회가 수행되는가")
        void getOrganizationRankByType() {
            //given

            //when
            List<OrganizationResponse> result = organizationService.getOrganizationRankByType(OrganizationType.UNIVERSITY, PageRequest.of(0, 20));

            //then
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("검색어를 통한 조회가 수행되는가")
        void searchOrganization() {
            //given

            //when
            List<OrganizationResponse> result = organizationService.searchOrganization(OrganizationType.UNIVERSITY, "한국공학대학교", PageRequest.of(0, 20));

            //then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("한국공학대학교");
        }

        @Test
        @DisplayName("이름을 통한 조회가 수행되는가")
        void findByName() {
            //given

            //when
            IdResponse<Long> result = organizationService.findByName(OrganizationFixture.TUKOREA.getName());

            //then
            Optional<Organization> org = organizationRepository.findById(result.getId());
            assertThat(org).isNotEmpty();
            assertThat(org.orElse(null).getId()).isEqualTo(result.getId());
        }

        @Test
        @DisplayName("id를 통한 조회가 수행되는가")
        void loadEntity() {
            //given
            Organization expected = organizationRepository.findByName(OrganizationFixture.TUKOREA.getName()).orElse(null);

            //when
            Organization result = organizationService.loadEntity(expected.getId());

            //then
            assertThat(result).isEqualTo(expected);
        }
    }
}
