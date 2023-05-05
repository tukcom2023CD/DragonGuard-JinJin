package com.dragonguard.backend.domain.blockchain.controller;

import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.blockchain.service.BlockchainService;
import com.dragonguard.backend.domain.blockchain.service.TransactionService;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlockchainController.class)
class BlockchainControllerTest extends RestDocumentTest {
    @MockBean
    private BlockchainService blockchainService;
    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("블록체인 부여 기록 리스트 조회")
    void getBlockchainInfo() throws Exception {
        List<BlockchainResponse> expected = List.of(
                new BlockchainResponse(1L, ContributeType.COMMIT, new BigInteger("10"), "ohksj77", UUID.randomUUID()),
                new BlockchainResponse(2L, ContributeType.COMMIT, new BigInteger("5"), "ohksj77", UUID.randomUUID()));
        given(blockchainService.getBlockchainList()).willReturn(expected);

        ResultActions perform =
                mockMvc.perform(
                        get("/blockchain")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer apfawfawfa.awfsfawef2.r4svfv32"));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        perform.andDo(print())
                .andDo(document("get blockchain list", getDocumentRequest(), getDocumentResponse()));
    }
}
