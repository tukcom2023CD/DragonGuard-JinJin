package com.dragonguard.backend.domain.blockchain.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    private String transactionHash;

    private BigInteger amount;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Blockchain blockchain;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public History(String transactionHash, BigInteger amount, Blockchain blockchain) {
        this.transactionHash = transactionHash;
        this.amount = amount;
        this.blockchain = blockchain;
    }

    public String getTransactionHashUrl() {
        return "https://baobab.scope.klaytn.com/tx/" + this.transactionHash + "?tabId=tokenTransfer";
    }
}
