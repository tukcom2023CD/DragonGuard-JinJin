package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.ContractResponse;
import com.dragonguard.backend.config.blockchain.BlockchainProperties;
import com.dragonguard.backend.global.exception.BlockchainException;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;

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
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(keyring.getAddress());
            sendOptions.setGas(BigInteger.valueOf(3000000));
            this.contract.deploy(sendOptions, properties.getByteCode());

        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new BlockchainException();
        }
    }

    public void set(ContractRequest request) {
        try {
            contract.getMethod("set").call(List.of(request.getAddress(),
                    request.getContributeType(), request.getAmount(), request.getName()));

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BlockchainException();
        }
    }

    public ContractResponse getInfo(String address) {
        try {
            List<Type> info = contract.getMethod("getInfo").call(List.of(address));

            return new ContractResponse((String) info.get(0).getValue(),
                    (BigInteger) info.get(1).getValue(), (String) info.get(2).getValue());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BlockchainException();
        }
    }
}
