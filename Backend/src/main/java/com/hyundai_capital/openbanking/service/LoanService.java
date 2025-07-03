package com.hyundai_capital.openbanking.service;

import com.hyundai_capital.openbanking.dto.loan.LoanContractDTO;
import com.hyundai_capital.openbanking.dto.loan.LoanTransactionInquiryRequest;
import com.hyundai_capital.openbanking.dto.loan.LoanTransactionInquiryResponse;
import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.LoanTransaction;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.repository.LoanContractRepository;
import com.hyundai_capital.openbanking.repository.LoanTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoanService {

    private final LoanContractRepository loanContractRepository;
    private final LoanTransactionRepository loanTransactionRepository;

    @Transactional(readOnly = true)
    public LoanTransactionInquiryResponse getLoanTransactions(LoanTransactionInquiryRequest request) {
        // 대출계좌 조회
        LoanContract loanContract = loanContractRepository.findByLoanAccountNum(request.getLoanAccountNum())
                .orElseThrow(() -> new RuntimeException("대출계좌를 찾을 수 없습니다."));

        // 날짜 범위 설정
        LocalDateTime startDate = parseDate(request.getInquiryStartDate());
        LocalDateTime endDate = parseDate(request.getInquiryEndDate()).plusDays(1); // 종료일 포함

        // 거래내역 조회
        List<LoanTransaction> transactions = loanTransactionRepository
                .findByLoanAccountNumAndDateRange(request.getLoanAccountNum(), startDate, endDate);

        // 정렬
        if ("A".equals(request.getSortOrder())) {
            transactions.sort((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()));
        } else {
            transactions.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
        }

        // DTO 변환
        List<LoanTransactionInquiryResponse.LoanTransactionDetail> tranList = transactions.stream()
                .map(this::convertToTransactionDetail)
                .collect(Collectors.toList());

        return LoanTransactionInquiryResponse.builder()
                .loanAccountNum(loanContract.getLoanAccountNum())
                .loanProdName(loanContract.getLoanProductName())
                .loanBalanceAmt(loanContract.getRemainingAmount().toString())
                .tranList(tranList)
                .build();
    }

    public LoanContract createLoanContract(User user, String productName, BigDecimal amount, 
                                          BigDecimal interestRate, LocalDate maturityDate, Integer repaymentDay,
                                          LoanContract.LoanType loanType) {
        String loanAccountNum = generateLoanAccountNumber();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LoanContract loanContract = LoanContract.builder()
                .loanAccountNum(loanAccountNum)
                .loanProductName(productName)
                .loanAmount(amount)
                .remainingAmount(amount)
                .interestRate(interestRate)
                .contractDate(LocalDate.now().format(formatter))
                .maturityDate(maturityDate.format(formatter))
                .repaymentDay(repaymentDay)
                .loanType(loanType)
                .loanStatus(LoanContract.LoanStatus.ACTIVE)
                .user(user)
                .build();

        return loanContractRepository.save(loanContract);
    }

    @Transactional(readOnly = true)
    public List<LoanContract> getUserLoanContracts(String userCi) {
        return loanContractRepository.findActiveLoansByUserCi(userCi);
    }

    @Transactional(readOnly = true)
    public List<LoanContract> getUserLoanContractsByUserSeqNo(String userSeqNo) {
        return loanContractRepository.findActiveLoansByUserSeqNo(userSeqNo);
    }

    @Transactional(readOnly = true)
    public List<LoanContractDTO> getUserLoanDTOsByUserSeqNo(String userSeqNo) {
        List<LoanContract> contracts = loanContractRepository.findActiveLoansByUserSeqNo(userSeqNo);
        return contracts.stream()
                .map(LoanContractDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private LocalDateTime parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date.atStartOfDay();
    }

    private LoanTransactionInquiryResponse.LoanTransactionDetail convertToTransactionDetail(LoanTransaction transaction) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        return LoanTransactionInquiryResponse.LoanTransactionDetail.builder()
                .tranDate(transaction.getTransactionDate().format(dateFormatter))
                .tranTime(transaction.getTransactionDate().format(timeFormatter))
                .tranType(getTransactionTypeCode(transaction.getTransactionType()))
                .tranTypeName(getTransactionTypeName(transaction.getTransactionType()))
                .tranAmt(transaction.getTransactionAmount().toString())
                .afterBalanceAmt(transaction.getAfterBalance().toString())
                .branchName(transaction.getBranchName() != null ? transaction.getBranchName() : "온라인")
                .tranMemo(transaction.getTransactionSummary())
                .tranUniqueNo(transaction.getTransactionUniqueNo())
                .build();
    }

    private String getTransactionTypeCode(LoanTransaction.TransactionType type) {
        switch (type) {
            case LOAN_EXECUTION: return "1";
            case PRINCIPAL_PAYMENT: return "2";
            case INTEREST_PAYMENT: return "3";
            case TOTAL_PAYMENT: return "4";
            case FEE_PAYMENT: return "5";
            case EARLY_REPAYMENT: return "6";
            default: return "0";
        }
    }

    private String getTransactionTypeName(LoanTransaction.TransactionType type) {
        switch (type) {
            case LOAN_EXECUTION: return "대출실행";
            case PRINCIPAL_PAYMENT: return "원금상환";
            case INTEREST_PAYMENT: return "이자납부";
            case TOTAL_PAYMENT: return "원리금상환";
            case FEE_PAYMENT: return "수수료납부";
            case EARLY_REPAYMENT: return "중도상환";
            default: return "기타";
        }
    }

    private String generateLoanAccountNumber() {
        return "HC" + System.currentTimeMillis();
    }
} 
