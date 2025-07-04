package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfo {

    @EmbeddedId
    private AccountInfoId id;  // 복합 기본키 (loan_id, account_num)

    @Column(name = "account_seq", length = 6)
    private String accountSeq;  // 계좌순번

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("loanId")
    @JoinColumn(name = "loan_id")
    private LoanContract loanContract;  // 대출계약
}