package com.hyundai_capital.openbanking.dto.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTransactionInquiryRequest {
    
    @JsonProperty("bank_tran_id")
    private String bankTranId;              // 은행거래고유번호
    
    @JsonProperty("loan_account_num")
    private String loanAccountNum;          // 대출계좌번호
    
    @JsonProperty("inquiry_start_date")
    private String inquiryStartDate;        // 조회시작일자 (YYYYMMDD)
    
    @JsonProperty("inquiry_end_date")
    private String inquiryEndDate;          // 조회종료일자 (YYYYMMDD)
    
    @JsonProperty("sort_order")
    @Builder.Default
    private String sortOrder = "D";         // 정렬순서 (D: 내림차순, A: 오름차순)
    
    @JsonProperty("tran_dtime")
    private String tranDtime;               // 거래일시
} 