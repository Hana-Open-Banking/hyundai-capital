package com.hyundai_capital.openbanking.config;

import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.LoanTransaction;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.repository.LoanContractRepository;
import com.hyundai_capital.openbanking.repository.LoanTransactionRepository;
import com.hyundai_capital.openbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // StartupLogger보다 먼저 실행
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final LoanContractRepository loanContractRepository;
    private final LoanTransactionRepository loanTransactionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("데이터 초기화 시작...");
        
        // 테스트 사용자 생성
        if (userRepository.count() == 0) {
            User testUser = User.builder()
                    .userId("testuser001")
                    .password("HASHED_" + "password123".hashCode())
                    .name("김현대")
                    .email("test@hyundai-capital.com")
                    .phoneNumber("010-1234-5678")
                    .citizenNumber("HASHED_" + "1234567890123".hashCode())
                    .address("서울시 강남구 테헤란로 123")
                    .userCi("CI_TEST_" + UUID.randomUUID().toString().substring(0, 8))
                    .userDi("DI_TEST_" + UUID.randomUUID().toString().substring(0, 8))
                    .status(User.UserStatus.ACTIVE)
                    .build();
            
            userRepository.save(testUser);
            log.info("테스트 사용자 생성 완료: {}", testUser.getUserId());
            
            // 테스트 대출 계약 생성
            LoanContract testLoan = LoanContract.builder()
                    .loanAccountNum("HC" + System.currentTimeMillis())
                    .loanProductName("개인신용대출")
                    .loanAmount(new BigDecimal("10000000"))
                    .remainingAmount(new BigDecimal("8500000"))
                    .interestRate(new BigDecimal("4.5"))
                    .contractDate(LocalDate.now().minusMonths(6))
                    .maturityDate(LocalDate.now().plusMonths(30))
                    .repaymentDay(15)
                    .loanType(LoanContract.LoanType.PERSONAL_LOAN)
                    .status(LoanContract.LoanStatus.ACTIVE)
                    .user(testUser)
                    .build();
            
            loanContractRepository.save(testLoan);
            log.info("테스트 대출 계약 생성 완료: {}", testLoan.getLoanAccountNum());
            
            // 테스트 거래 내역 생성
            createTestTransactions(testLoan);
            
            // 두 번째 대출 계약 생성 (자동차 대출)
            LoanContract autoLoan = LoanContract.builder()
                    .loanAccountNum("HC" + (System.currentTimeMillis() + 1))
                    .loanProductName("자동차대출")
                    .loanAmount(new BigDecimal("25000000"))
                    .remainingAmount(new BigDecimal("20000000"))
                    .interestRate(new BigDecimal("3.8"))
                    .contractDate(LocalDate.now().minusMonths(12))
                    .maturityDate(LocalDate.now().plusMonths(48))
                    .repaymentDay(25)
                    .loanType(LoanContract.LoanType.AUTO_LOAN)
                    .status(LoanContract.LoanStatus.ACTIVE)
                    .user(testUser)
                    .build();
            
            loanContractRepository.save(autoLoan);
            log.info("테스트 자동차 대출 계약 생성 완료: {}", autoLoan.getLoanAccountNum());
            
            createTestTransactions(autoLoan);
        }
        
        log.info("데이터 초기화 완료. 총 사용자 수: {}, 총 대출 계약 수: {}", 
                userRepository.count(), loanContractRepository.count());
    }
    
    private void createTestTransactions(LoanContract loanContract) {
        // 대출 실행 거래
        LoanTransaction execution = LoanTransaction.builder()
                .transactionUniqueNo("TXN_" + UUID.randomUUID().toString().substring(0, 8))
                .transactionDate(loanContract.getContractDate().atTime(10, 0))
                .transactionAmount(loanContract.getLoanAmount())
                .afterBalance(loanContract.getLoanAmount())
                .transactionType(LoanTransaction.TransactionType.LOAN_EXECUTION)
                .transactionSummary("대출실행")
                .branchName("강남지점")
                .loanContract(loanContract)
                .build();
        
        loanTransactionRepository.save(execution);
        
        // 월별 상환 거래들 생성
        LocalDateTime currentDate = loanContract.getContractDate().atTime(10, 0).plusMonths(1);
        BigDecimal remainingAmount = loanContract.getLoanAmount();
        BigDecimal monthlyPayment = new BigDecimal("500000");
        
        for (int i = 1; i <= 6; i++) {
            remainingAmount = remainingAmount.subtract(monthlyPayment);
            
            LoanTransaction payment = LoanTransaction.builder()
                    .transactionUniqueNo("TXN_" + UUID.randomUUID().toString().substring(0, 8))
                    .transactionDate(currentDate)
                    .transactionAmount(monthlyPayment)
                    .afterBalance(remainingAmount)
                    .transactionType(LoanTransaction.TransactionType.TOTAL_PAYMENT)
                    .transactionSummary("정기상환 " + i + "회차")
                    .branchName("온라인")
                    .loanContract(loanContract)
                    .build();
            
            loanTransactionRepository.save(payment);
            currentDate = currentDate.plusMonths(1);
        }
        
        log.info("테스트 거래 내역 생성 완료: {}", loanContract.getLoanAccountNum());
    }
} 