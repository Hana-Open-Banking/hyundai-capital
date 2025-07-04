package com.hyundai_capital.openbanking.dto.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hyundai_capital.openbanking.entity.LoanContract;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanContractDTO {
    
    @JsonProperty("loan_id")
    private String loanId;
    
    @JsonProperty("loan_account_num")
    private String loanAccountNum;
    
    @JsonProperty("loan_product_name")
    private String loanProductName;
    
    @JsonProperty("loan_amount")
    private BigDecimal loanAmount;
    
    @JsonProperty("remaining_amount")
    private BigDecimal remainingAmount;
    
    @JsonProperty("interest_rate")
    private BigDecimal interestRate;
    
    @JsonProperty("contract_date")
    private String contractDate;
    
    @JsonProperty("maturity_date")
    private String maturityDate;
    
    @JsonProperty("repayment_day")
    private Integer repaymentDay;
    
    @JsonProperty("loan_status")
    private String loanStatus;
    
    @JsonProperty("loan_type")
    private String loanType;
    
    @JsonProperty("user_seq_no")
    private String userSeqNo;
    
    @JsonProperty("user_name")
    private String userName;
    
    // 엔티티를 DTO로 변환하는 정적 메서드
    public static LoanContractDTO fromEntity(LoanContract entity) {
        return LoanContractDTO.builder()
                .loanId(entity.getLoanId())
                .loanAccountNum(entity.getLoanAccountNum())
                .loanProductName(entity.getLoanProductName())
                .loanAmount(entity.getLoanAmount())
                .remainingAmount(entity.getRemainingAmount())
                .interestRate(entity.getInterestRate())
                .contractDate(entity.getContractDate())
                .maturityDate(entity.getMaturityDate())
                .repaymentDay(entity.getRepaymentDay())
                .loanStatus(entity.getLoanStatus().name())
                .loanType(entity.getLoanType().name())
                .userSeqNo(entity.getUser().getUserSeqNo())
                .userName(entity.getUser().getUserName())
                .build();
    }
}