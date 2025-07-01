package com.hyundai_capital.openbanking.dto.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTransactionInquiryResponse {
    
    @JsonProperty("loan_account_num")
    private String loanAccountNum;          // 대출계좌번호
    
    @JsonProperty("loan_prod_name")
    private String loanProdName;            // 대출상품명
    
    @JsonProperty("loan_balance_amt")
    private String loanBalanceAmt;          // 대출잔액
    
    @JsonProperty("tran_list")
    private List<LoanTransactionDetail> tranList;  // 거래내역목록
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoanTransactionDetail {
        
        @JsonProperty("tran_date")
        private String tranDate;            // 거래일자 (YYYYMMDD)
        
        @JsonProperty("tran_time")
        private String tranTime;            // 거래시각 (HHMMSS)
        
        @JsonProperty("tran_type")
        private String tranType;            // 거래구분
        
        @JsonProperty("tran_type_name")
        private String tranTypeName;        // 거래구분명
        
        @JsonProperty("tran_amt")
        private String tranAmt;             // 거래금액
        
        @JsonProperty("after_balance_amt")
        private String afterBalanceAmt;     // 거래후잔액
        
        @JsonProperty("branch_name")
        private String branchName;          // 점명
        
        @JsonProperty("tran_memo")
        private String tranMemo;            // 거래적요
        
        @JsonProperty("tran_unique_no")
        private String tranUniqueNo;        // 거래고유번호
    }
} 