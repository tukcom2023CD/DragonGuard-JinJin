package com.dragonguard.backend.blockchain.service;

import com.dragonguard.backend.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.blockchain.dto.response.ContractResponse;
import com.dragonguard.backend.config.blockchain.BlockChainProperties;
import com.dragonguard.backend.global.exception.BlockchainException;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.wallet.keyring.AbstractKeyring;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import io.ipfs.multibase.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.klaytn.caver.kct.kip7.KIP7ConstantData.ABI;

@Slf4j
@Service
public class TransactionService {
    private final BlockChainProperties properties;
    private final Caver caver;
    private final AbstractKeyring keyring;
    private static String abiJson;
    private static String keyRingJson;
    public TransactionService(BlockChainProperties properties, Caver caver) {
        this.properties = properties;
        this.caver = caver;
        Path abiPath = Paths.get(Paths.get("src/main/resources/").toAbsolutePath().normalize()
                .resolve("abi.json")
                .normalize()
                .toString());
        Path keyringPath = Paths.get(Paths.get("src/main/resources/").toAbsolutePath().normalize()
                .resolve("keyring.json")
                .normalize()
                .toString());

        try {
            this.abiJson = Files.readAllLines(abiPath, Charsets.UTF_8).stream().collect(Collectors.joining());
            this.keyRingJson = Files.readAllLines(keyringPath, Charsets.UTF_8).stream().collect(Collectors.joining());
            this.keyring = KeyringFactory.decrypt(keyRingJson, properties.getPassword());
        } catch (CipherException | IOException e) {
            throw new BlockchainException();
        }
    }

    public void deploy() {
        caver.wallet.add(keyring);

        try {
            log.info("=========11111===========");
            Contract contract = caver.contract.create(abiJson);
            log.info("=========22222===========");
            SendOptions sendOptions = new SendOptions();
            log.info("=========33333===========");
            sendOptions.setFrom(keyring.getAddress());
            log.info("=========44444===========");
            sendOptions.setGas(BigInteger.valueOf(3000000));
            log.info("=========55555===========");
            contract.deploy(sendOptions, properties.getByteCode());
            log.info("=========66666===========");
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new BlockchainException();
        }
    }

    public void set(ContractRequest request) {
        try {
            Contract contract = load();

            contract.call("set", request.getAddress(),
                    request.getContributeType(), request.getAmount(), request.getName());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BlockchainException();
        }
    }

    public ContractResponse getInfo(String address) {
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
            return new Contract(caver, ABI, keyring.getAddress());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }
}
