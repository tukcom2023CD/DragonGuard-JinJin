package com.dragonguard.backend.domain.blockchain.mapper;

import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.entity.History;
import com.dragonguard.backend.domain.member.entity.Member;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 김승진
 * @description 블록체인 Entity와 dto의 변환을 돕는 클래스
 */
@Mapper(
        componentModel = "spring",
        imports = {ContributeType.class})
public interface BlockchainMapper {
    Blockchain toEntity(final Member member, final ContributeType contributeType);

    @Mapping(target = "amount", expression = "java(history.getAmount().longValue())")
    @Mapping(target = "memberId", source = "history.blockchain.member.id")
    @Mapping(target = "githubId", source = "history.blockchain.member.githubId")
    @Mapping(
            target = "createdAt",
            expression = "java(history.getBaseTime().getCreatedAt().toString())")
    @Mapping(
            target = "contributeType",
            expression = "java(history.getBlockchain().getContributeType().toString())")
    @Mapping(target = "transactionHashUrl", expression = "java(history.getTransactionHashUrl())")
    BlockchainResponse toResponse(final History history);

    default List<BlockchainResponse> toResponseList(final List<History> histories) {
        return histories.stream()
                .sorted(Comparator.comparing(h -> h.getBaseTime().getCreatedAt()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    default List<BlockchainResponse> toBlockchainResponseList(final List<Blockchain> blockchains) {
        return toResponseList(
                blockchains.stream()
                        .map(Blockchain::getHistories)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()));
    }
}
