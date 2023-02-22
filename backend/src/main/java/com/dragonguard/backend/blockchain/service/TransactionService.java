package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.ContractResponse;
import com.dragonguard.backend.config.blockchain.BlockChainProperties;
import com.dragonguard.backend.global.exception.BlockchainException;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;

import static com.klaytn.caver.kct.kip7.KIP7ConstantData.ABI;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final BlockChainProperties properties;
    private final SingleKeyring deployer;
    private final String abiJson;
    private final Caver caver;

    public void deploy() {
        caver.wallet.add(deployer);

        try {
            Contract contract = caver.contract.create(abiJson);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(deployer.getAddress());
            sendOptions.setGas(BigInteger.valueOf(4000000));

            contract.deploy(sendOptions, properties.getByteCode());
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BlockchainException();
        }
    }

    public void set(String privateKey, ContractRequest request) {
        SingleKeyring executor = KeyringFactory.createFromPrivateKey(privateKey);
        caver.wallet.add(executor);
        try {
            Contract contract = load();

            contract.call("set", request.getAddress(),
                    request.getContributeType(), request.getAmount(), request.getName());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BlockchainException();
        }
    }

    public ContractResponse getInfo(String privateKey, String address) {
        SingleKeyring executor = KeyringFactory.createFromPrivateKey(privateKey);
        caver.wallet.add(executor);
        try {
            Contract contract = load();

            List<Type> info = contract.call("getInfo", address);

            return new ContractResponse((String) info.get(0).getValue(),
                    (BigInteger) info.get(1).getValue(), (String) info.get(2).getValue());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BlockchainException();
        }
    }

    public Contract load() {
        try {
            return new Contract(caver, ABI, properties.getUrl());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }
}
