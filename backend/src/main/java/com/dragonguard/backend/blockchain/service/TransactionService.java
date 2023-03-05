package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.config.blockchain.BlockchainProperties;
import com.dragonguard.backend.blockchain.exception.BlockchainException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
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
public class TransactionService {
    private final BlockchainProperties properties;
    private final Caver caver;
    private final AbstractKeyring keyring;
    private final Contract contract;

    public void deploy() {
        caver.wallet.add(keyring);

        try {
            SendOptions options = new SendOptions();
            options.setFrom(keyring.getAddress());
            options.setGas(BigInteger.valueOf(3000000));
            options.setFeeDelegation(true);
            options.setFeePayer(keyring.getAddress());
            contract.deploy(options, properties.getByteCode(), "Gitter", "GTR", new BigInteger("10000000000000"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainException();
        }
    }

    public void transfer(ContractRequest request) {
        SendOptions options = new SendOptions();
        options.setFrom(keyring.getAddress());
        options.setGas(BigInteger.valueOf(3000000));
        options.setFeeDelegation(true);
        options.setFeePayer(keyring.getAddress());

        try {
            contract.send(options, "set", request.getAddress(), request.getAmount(), request.getContributeType().toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainException();
        }
    }

    public BigInteger balanceOf(String address) {
        try {
            List<Type> info = contract.call("balanceOf", address);

            return new BigInteger(objectToString(info.get(0).getValue()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BlockchainException();
        }
    }

    private String objectToString(Object value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }
}
