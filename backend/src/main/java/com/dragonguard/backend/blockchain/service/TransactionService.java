package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.config.blockchain.BlockChainProperties;
import com.dragonguard.backend.global.exception.BlockchainException;
import com.klaytn.caver.Caver;
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
import java.util.Arrays;

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

    public void transact(String privateKey, BigInteger amount) {
        SingleKeyring executor = KeyringFactory.createFromPrivateKey("0x{private key}");
        caver.wallet.add(executor);
        try {
            Contract contract = load();

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(executor.getAddress());
            sendOptions.setGas(amount);
            contract.getMethod("set").send(Arrays.asList("testValue"), sendOptions);

        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException |
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
