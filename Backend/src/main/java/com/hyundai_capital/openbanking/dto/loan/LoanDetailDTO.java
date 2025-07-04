package com.hyundai_capital.openbanking.dto.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hyundai_capital.openbanking.entity.AccountInfo;
import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.LoanRepaymentInfo;
import com.hyundai_capital.openbanking.entity.LoanTransaction;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDetailDTO {
    
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
    
    // 상환 정보
    @JsonProperty("repayment_info")
    private RepaymentInfo repaymentInfo;
    
    // 계좌 정보
    @JsonProperty("account_info_list")
    private List<AccountInfoDTO> accountInfoList;
    
    // 최근 거래 내역 (최대 5건)
    @JsonProperty("recent_transactions")
    private List<TransactionDTO> recentTransactions;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RepaymentInfo {
        @JsonProperty("repay_date")
        private String repayDate;
        
        @JsonProperty("repay_method")
        private String repayMethod;
        
        @JsonProperty("repay_org_code")
        private String repayOrgCode;
        
        @JsonProperty("repay_account_num")
        private String repayAccountNum;
        
        @JsonProperty("repay_account_num_masked")
        private String repayAccountNumMasked;
        
        @JsonProperty("next_repay_date")
        private String nextRepayDate;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AccountInfoDTO {
        @JsonProperty("account_num")
        private String accountNum;
        
        @JsonProperty("account_seq")
        private String accountSeq;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TransactionDTO {
        @JsonProperty("transaction_id")
        private String transactionId;
        
        @JsonProperty("transaction_date")
        private String transactionDate;
        
        @JsonProperty("transaction_time")
        private String transactionTime;
        
        @JsonProperty("transaction_type")
        private String transactionType;
        
        @JsonProperty("transaction_amount")
        private BigDecimal transactionAmount;
        
        @JsonProperty("after_balance")
        private BigDecimal afterBalance;
        
        @JsonProperty("transaction_summary")
        private String transactionSummary;
    }
    
    // 엔티티를 DTO로 변환하는 정적 메서드
    public static LoanDetailDTO fromEntity(
            LoanContract loanContract, 
            LoanRepaymentInfo repaymentInfo, 
            List<AccountInfo> accountInfos,
            List<LoanTransaction> recentTransactions) {
        
        // 상환 정보 변환
        RepaymentInfo repaymentInfoDTO = null;
        if (repaymentInfo != null) {
            repaymentInfoDTO = RepaymentInfo.builder()
                    .repayDate(repaymentInfo.getRepayDate())
                    .repayMethod(repaymentInfo.getRepayMethod())
                    .repayOrgCode(repaymentInfo.getRepayOrgCode())
                    .repayAccountNum(repaymentInfo.getRepayAccountNum())
                    .repayAccountNumMasked(repaymentInfo.getRepayAccountNumMasked())
                    .nextRepayDate(repaymentInfo.getNextRepayDate())
                    .build();
        }
        
        // 계좌 정보 변환
        List<AccountInfoDTO> accountInfoDTOs = accountInfos.stream()
                .map(accountInfo -> AccountInfoDTO.builder()
                        .accountNum(accountInfo.getId().getAccountNum())
                        .accountSeq(accountInfo.getAccountSeq())
                        .build())
                .collect(Collectors.toList());
        
        // 최근 거래 내역 변환
        List<TransactionDTO> transactionDTOs = recentTransactions.stream()
                .map(transaction -> TransactionDTO.builder()
                        .transactionId(transaction.getTransactionId())
                        .transactionDate(transaction.getTransDate())
                        .transactionTime(transaction.getTransTime())
                        .transactionType(transaction.getTransactionType().name())
                        .transactionAmount(transaction.getTransactionAmount())
                        .afterBalance(transaction.getAfterBalance())
                        .transactionSummary(transaction.getTransactionSummary())
                        .build())
                .collect(Collectors.toList());
        
        return LoanDetailDTO.builder()
                .loanId(loanContract.getLoanId())
                .loanAccountNum(loanContract.getLoanAccountNum())
                .loanProductName(loanContract.getLoanProductName())
                .loanAmount(loanContract.getLoanAmount())
                .remainingAmount(loanContract.getRemainingAmount())
                .interestRate(loanContract.getInterestRate())
                .contractDate(loanContract.getContractDate())
                .maturityDate(loanContract.getMaturityDate())
                .repaymentDay(loanContract.getRepaymentDay())
                .loanStatus(loanContract.getLoanStatus().name())
                .loanType(loanContract.getLoanType().name())
                .userSeqNo(loanContract.getUser().getUserSeqNo())
                .userName(loanContract.getUser().getUserName())
                .repaymentInfo(repaymentInfoDTO)
                .accountInfoList(accountInfoDTOs)
                .recentTransactions(transactionDTOs)
                .build();
    }
}