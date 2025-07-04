package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountInfoId implements Serializable {

    @Column(name = "loan_id", length = 20)
    private String loanId;  // 대출계약 ID

    @Column(name = "account_num", length = 16)
    private String accountNum;  // 계좌번호
}