package com.dragonguard.backend.blockchain.controller;

import com.dragonguard.backend.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.blockchain.entity.ContributeType;
import com.dragonguard.backend.blockchain.service.BlockchainService;
import com.dragonguard.backend.blockchain.service.TransactionService;
import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.support.DatabaseTest;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DatabaseTest
@WebMvcTest(BlockchainController.class)
class BlockchainControllerTest extends RestDocumentTest {
    @MockBean
    private BlockchainService blockchainService;
    @MockBean
    protected AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    protected Member loginUser;

    @BeforeEach
    public void setup() {
        Member member = new Member("Kim", "ohksj77", new Commit(2023, 100, "ohksj77"), "12341234", "https://github");
        loginUser = memberRepository.save(member);
        when(authService.getLoginUser()).thenReturn(loginUser);
    }

    @Test
    @DisplayName("블록체인 부여 기록 리스트 조회")
    void getBlockchainInfo() throws Exception {
        List<BlockchainResponse> expected = List.of(
                new BlockchainResponse(1L, ContributeType.COMMIT, new BigInteger("10"), "ohksj77", UUID.randomUUID()),
                new BlockchainResponse(2L, ContributeType.COMMIT, new BigInteger("5"), "ohksj77", UUID.randomUUID()));
        given(blockchainService.getBlockchainList(any())).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/blockchain/1")
                                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        perform.andDo(print())
                .andDo(document("get blockchain list", getDocumentRequest(), getDocumentResponse()));
    }
}
