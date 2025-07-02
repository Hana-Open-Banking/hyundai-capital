package com.hyundai_capital.openbanking.controller;

import com.hyundai_capital.openbanking.dto.user.LoginRequest;
import com.hyundai_capital.openbanking.dto.user.LoginResponse;
import com.hyundai_capital.openbanking.dto.user.UserRegistrationRequest;
import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.service.LoanService;
import com.hyundai_capital.openbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hyundai-capital")
@RequiredArgsConstructor
@Slf4j
public class HyundaiCapitalController {
    
    private final UserService userService;
    private final LoanService loanService;
    
    @PostMapping("/users/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            log.info("사용자 회원가입 요청: {}", request.getUserId());
            
            User user = userService.registerUser(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("user_id", user.getUserId());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("회원가입 오류: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping("/users/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("사용자 로그인 요청: {}", request.getUserId());
            
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("로그인 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/loans/apply")
    public ResponseEntity<Map<String, Object>> applyLoan(
            @RequestParam("user_id") String userId,
            @RequestParam("product_name") String productName,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("interest_rate") BigDecimal interestRate,
            @RequestParam("loan_type") String loanType,
            @RequestParam("maturity_months") Integer maturityMonths,
            @RequestParam("repayment_day") Integer repaymentDay) {
        
        try {
            log.info("대출 신청 요청: 사용자={}, 상품={}, 금액={}", userId, productName, amount);
            
            User user = userService.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            LocalDate maturityDate = LocalDate.now().plusMonths(maturityMonths);
            LoanContract.LoanType type = LoanContract.LoanType.valueOf(loanType);
            
            LoanContract loanContract = loanService.createLoanContract(
                    user, productName, amount, interestRate, maturityDate, repaymentDay, type);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "대출 승인이 완료되었습니다.");
            response.put("loan_account_num", loanContract.getLoanAccountNum());
            response.put("loan_amount", loanContract.getLoanAmount());
            response.put("contract_date", loanContract.getContractDate());
            response.put("maturity_date", loanContract.getMaturityDate());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("대출 신청 오류: {}", e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/loans/my-loans")
    public ResponseEntity<List<LoanContract>> getMyLoans(@RequestParam("user_ci") String userCi) {
        try {
            log.info("내 대출 조회 요청: {}", userCi);
            
            List<LoanContract> loans = loanService.getUserLoanContracts(userCi);
            return ResponseEntity.ok(loans);
            
        } catch (RuntimeException e) {
            log.error("내 대출 조회 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Hyundai Capital OpenBanking API");
        response.put("timestamp", LocalDate.now().toString());
        
        return ResponseEntity.ok(response);
    }
} 