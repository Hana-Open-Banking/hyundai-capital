package com.hyundai_capital.openbanking.controller;

import com.hyundai_capital.openbanking.dto.loan.LoanContractDTO;
import com.hyundai_capital.openbanking.dto.loan.LoanDetailDTO;
import com.hyundai_capital.openbanking.dto.user.LoginRequest;
import com.hyundai_capital.openbanking.dto.user.LoginResponse;
import com.hyundai_capital.openbanking.dto.user.UserRegistrationRequest;
import com.hyundai_capital.openbanking.entity.AccessToken;
import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.repository.AccessTokenRepository;
import com.hyundai_capital.openbanking.service.LoanService;
import com.hyundai_capital.openbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final AccessTokenRepository accessTokenRepository;

    @PostMapping("/users/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            log.info("사용자 회원가입 요청: {}", request.getUserId());

            User user = userService.registerUser(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("user_id", user.getUserSeqNo());

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

            User user = userService.findByUserSeqNo(userId)
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
    public ResponseEntity<List<LoanContractDTO>> getMyLoans(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("user_seq_no") String userSeqNo) {
        try {
            log.info("내 대출 조회 요청: 사용자 일련번호={}", userSeqNo);

            // 토큰 검증 (토큰이 유효하고 요청한 user_seq_no와 일치하는지 확인)
            if (!isValidTokenForUser(authorization, userSeqNo)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // user_seq_no를 이용하여 대출 목록 DTO 조회
            List<LoanContractDTO> loans = loanService.getUserLoanDTOsByUserSeqNo(userSeqNo);
            return ResponseEntity.ok(loans);

        } catch (RuntimeException e) {
            log.error("내 대출 조회 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean isValidTokenForUser(String authorization, String userSeqNo) {
        // 토큰 형식 검증
        System.out.println("isvalidtokenforuser: " + authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return false;
        }

        // 토큰 추출
        String accessTokenId = authorization.substring(7);
        System.out.println("acessTokenId: " + accessTokenId);
        // AccessTokenRepository를 통해 토큰 조회
        java.util.Optional<AccessToken> tokenOpt = accessTokenRepository.findByAccessTokenId(accessTokenId);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        AccessToken token = tokenOpt.get();

        // 토큰 만료 여부 확인
        if (token.isExpired() || token.getTokenStatus() != AccessToken.TokenStatus.ACTIVE) {
            return false;
        }

        // 토큰의 사용자와 요청한 user_seq_no가 일치하는지 확인
        return token.getUser().getUserSeqNo().equals(userSeqNo);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Hyundai Capital OpenBanking API");
        response.put("timestamp", LocalDate.now().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * 대출 상세 조회 API
     * 특정 대출의 상세 정보를 조회합니다. 액세스 토큰이 필요합니다.
     * 
     * @param authorization 인증 헤더 (Bearer 토큰)
     * @param loanId 대출 ID
     * @param userSeqNo 사용자 일련번호
     * @return 대출 상세 정보
     */
    @GetMapping("/loans/detail")
    public ResponseEntity<?> getLoanDetail(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("loan_id") String loanId,
            @RequestParam("user_seq_no") String userSeqNo) {

        try {
            log.info("대출 상세 조회 요청: 대출 ID={}, 사용자 일련번호={}", loanId, userSeqNo);

            // 토큰 검증 (토큰이 유효하고 요청한 user_seq_no와 일치하는지 확인)
            if (!isValidTokenForUser(authorization, userSeqNo)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // 대출 상세 정보 조회
            LoanDetailDTO loanDetail = loanService.getLoanDetail(loanId);

            // 요청한 사용자와 대출의 소유자가 일치하는지 확인
            if (!loanDetail.getUserSeqNo().equals(userSeqNo)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(loanDetail);

        } catch (RuntimeException e) {
            log.error("대출 상세 조회 오류: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
} 
