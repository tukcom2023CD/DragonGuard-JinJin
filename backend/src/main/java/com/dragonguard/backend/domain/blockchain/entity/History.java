package com.dragonguard.backend.domain.blockchain.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.*;

@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History implements Auditable {
    private static final String TRANSACTION_URL_FORMAT =
            "https://baobab.scope.klaytn.com/tx/%s?tabId=tokenTransfer";
    private static final Long UPDATE_TIME_UNIT = 20L;

    @Id @GeneratedValue private Long id;

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
    public History(
            final String transactionHash, final BigInteger amount, final Blockchain blockchain) {
        this.transactionHash = transactionHash;
        this.amount = amount;
        this.blockchain = blockchain;
    }

    public String getTransactionHashUrl() {
        return String.format(TRANSACTION_URL_FORMAT, this.transactionHash);
    }

    public boolean isUpdatable() {
        return this.baseTime
                .getCreatedAt()
                .isBefore(LocalDateTime.now().minusSeconds(UPDATE_TIME_UNIT));
    }
}
