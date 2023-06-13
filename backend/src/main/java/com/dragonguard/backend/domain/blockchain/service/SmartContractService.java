package com.dragonguard.backend.domain.blockchain.service;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.exception.BlockchainException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * @author 김승진
 * @description 블록체인 스마트 컨트랙트로의 접근을 수행하는 클래스
 */

@Service
@RequiredArgsConstructor
public class SmartContractService {
    private final Contract contract;
    private final ObjectMapper objectMapper;
    private final SendOptions sendOptions;
    private static final String SET_METHOD = "set";
    private static final String BALANCE_OF_METHOD = "balanceOf";

    public void transfer(ContractRequest request, String walletAddress) {
        try {
            contract.send(sendOptions, SET_METHOD, walletAddress, request.getAmount(), request.getContributeType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainException();
        }
    }

    public BigInteger balanceOf(String address) {
        try {
            List<Type> info = contract.call(BALANCE_OF_METHOD, address);

            return new BigInteger(objectToString(info.get(0).getValue()));

        } catch (Exception e) {
            throw new BlockchainException();
        }
    }

    private String objectToString(Object value) throws JsonProcessingException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }
}
