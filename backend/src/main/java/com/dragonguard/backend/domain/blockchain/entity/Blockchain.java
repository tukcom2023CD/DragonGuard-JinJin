package com.dragonguard.backend.domain.blockchain.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.global.audit.SoftDelete;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 김승진
 * @description 블록체인 정보를 담는 DB Entity
 */

@Getter
@Entity
@SoftDelete
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blockchain implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContributeType contributeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "BINARY(16)")
    private Member member;

    private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blockchain")
    private List<History> histories = new ArrayList<>();

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Blockchain(final ContributeType contributeType, final Member member) {
        this.contributeType = contributeType;
        this.member = member;
        this.address = member.getWalletAddress();
        organizeMember();
    }

    private void organizeMember() {
        this.member.organizeBlockchain(this);
    }

    public void addHistory(final BigInteger amount, final String transactionHash) {
        this.histories.add(new History(transactionHash, amount, this));
    }

    public boolean isNewHistory(final long amount) {
        return amount > 0 && getSumOfAmount() < amount;
    }

    public long getSumOfAmount() {
        return this.histories.stream().map(History::getAmount).mapToLong(BigInteger::longValue).sum();
    }

    public void deleteByMember() {
        this.histories.forEach(History::delete);
        this.delete();
    }
}
