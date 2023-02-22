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
import kotlin.text.Charsets;
import org.springframework.stereotype.Service;
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

@Service
public class TransactionService {
    private final BlockChainProperties properties;
    private final String abiJson;
    private final Caver caver;
    private final SingleKeyring singleKeyring;

    public TransactionService(BlockChainProperties properties, Caver caver) {
        Path path = Paths.get(Paths.get("src/main/resources/").toAbsolutePath().normalize()
                .resolve("DragonContract.json")
                .normalize()
                .toString());
        String json;
        try {
            json = Files.readAllLines(path, Charsets.UTF_8).stream().collect(Collectors.joining());
        } catch (IOException e) {
            throw new BlockchainException();
        }
        this.properties = properties;
        this.abiJson = json;
        this.caver = caver;
        this.singleKeyring = KeyringFactory.generate();
    }

    public void deploy() {
        caver.wallet.add(singleKeyring);

        try {
            Contract contract = caver.contract.create(abiJson);
            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(singleKeyring.getAddress());
            sendOptions.setGas(BigInteger.valueOf(4000000));

            contract.deploy(sendOptions, properties.getByteCode());
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException |
                    InvocationTargetException | InstantiationException | IllegalAccessException e) {
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
            return new Contract(caver, ABI, singleKeyring.getAddress());
        } catch (IOException e) {
            throw new BlockchainException();
        }
    }
}
