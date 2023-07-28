package com.dragonguard.backend.domain.blockchain.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Entity
@Where(clause = "deleted_at is null")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    private String transactionHash;

    private BigInteger amount;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
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

    public boolean isUpdatable() {
        return this.baseTime.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(20L));
    }
}
