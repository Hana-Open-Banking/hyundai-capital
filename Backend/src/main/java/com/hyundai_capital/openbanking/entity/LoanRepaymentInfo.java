package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loan_repayment_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRepaymentInfo {

    @Id
    @Column(name = "loan_id", length = 20)
    private String loanId;  // 대출계약 ID

    @Column(name = "repay_date", length = 8, nullable = false)
    private String repayDate;  // 상환일자

    @Column(name = "repay_method", length = 2, nullable = false)
    private String repayMethod;  // 상환방식

    @Column(name = "repay_org_code", length = 3, nullable = false)
    private String repayOrgCode;  // 기관코드

    @Column(name = "repay_account_num", length = 16, nullable = false)
    private String repayAccountNum;  // 계좌번호

    @Column(name = "repay_account_num_masked", length = 20, nullable = false)
    private String repayAccountNumMasked;  // 마스킹 계좌

    @Column(name = "next_repay_date", length = 8)
    private String nextRepayDate;  // 다음 상환일

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "loan_id")
    private LoanContract loanContract;  // 대출계약
}