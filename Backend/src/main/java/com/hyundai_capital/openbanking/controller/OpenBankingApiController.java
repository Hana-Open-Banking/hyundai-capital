package com.hyundai_capital.openbanking.controller;

import com.hyundai_capital.openbanking.dto.common.CommonResponse;
import com.hyundai_capital.openbanking.dto.loan.LoanDetailDTO;
import com.hyundai_capital.openbanking.dto.loan.LoanTransactionInquiryRequest;
import com.hyundai_capital.openbanking.dto.loan.LoanTransactionInquiryResponse;
import com.hyundai_capital.openbanking.service.LoanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/v2.0")
@RequiredArgsConstructor
@Slf4j
public class OpenBankingApiController {

    private final LoanService loanService;

    @PostMapping("/loan/transaction_list/search")
    public ResponseEntity<CommonResponse<LoanTransactionInquiryResponse>> getLoanTransactions(
            @RequestHeader("Authorization") String authorization,
            @RequestBody LoanTransactionInquiryRequest request) {

        try {
            log.info("대출거래조회 요청: {}", request.getLoanAccountNum());

            // 토큰 검증 (간단한 버전)
            if (!isValidToken(authorization)) {
                return ResponseEntity.badRequest().body(
                    CommonResponse.error(
                        request.getBankTranId(),
                        getCurrentDateTime(),
                        "A0001",
                        "유효하지 않은 액세스 토큰입니다."
                    )
                );
            }

            LoanTransactionInquiryResponse response = loanService.getLoanTransactions(request);

            return ResponseEntity.ok(
                CommonResponse.success(
                    request.getBankTranId(),
                    getCurrentDateTime(),
                    response
                )
            );

        } catch (RuntimeException e) {
            log.error("대출거래조회 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                CommonResponse.error(
                    request.getBankTranId(),
                    getCurrentDateTime(),
                    "A9999",
                    e.getMessage()
                )
            );
        }
    }

    @PostMapping("/loan/contract/search")
    public ResponseEntity<CommonResponse<Object>> getLoanContracts(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("user_ci") String userCi) {

        try {
            log.info("대출계약조회 요청: {}", userCi);

            // 토큰 검증
            if (!isValidToken(authorization)) {
                return ResponseEntity.badRequest().body(
                    CommonResponse.error(
                        UUID.randomUUID().toString(),
                        getCurrentDateTime(),
                        "A0001",
                        "유효하지 않은 액세스 토큰입니다."
                    )
                );
            }

            var contracts = loanService.getUserLoanContracts(userCi);

            return ResponseEntity.ok(
                CommonResponse.success(
                    UUID.randomUUID().toString(),
                    getCurrentDateTime(),
                    contracts
                )
            );

        } catch (RuntimeException e) {
            log.error("대출계약조회 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                CommonResponse.error(
                    UUID.randomUUID().toString(),
                    getCurrentDateTime(),
                    "A9999",
                    e.getMessage()
                )
            );
        }
    }

    private boolean isValidToken(String authorization) {
        // 간단한 토큰 검증 (실제로는 더 복잡한 검증 필요)
        return authorization != null && authorization.startsWith("Bearer ");
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 대출 상세 조회 API
     * 특정 대출의 상세 정보를 조회합니다. 액세스 토큰이 필요합니다.
     * 
     * @param authorization 인증 헤더 (Bearer 토큰)
     * @param loanId 대출 ID
     * @return 대출 상세 정보
     */
    @GetMapping("/loan/detail")
    public ResponseEntity<CommonResponse<LoanDetailDTO>> getLoanDetail(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("loan_id") String loanId) {

        try {
            log.info("대출상세조회 요청: {}", loanId);

            // 토큰 검증
            if (!isValidToken(authorization)) {
                return ResponseEntity.badRequest().body(
                    CommonResponse.error(
                        UUID.randomUUID().toString(),
                        getCurrentDateTime(),
                        "A0001",
                        "유효하지 않은 액세스 토큰입니다."
                    )
                );
            }

            // 대출 상세 정보 조회
            LoanDetailDTO loanDetail = loanService.getLoanDetail(loanId);

            return ResponseEntity.ok(
                CommonResponse.success(
                    UUID.randomUUID().toString(),
                    getCurrentDateTime(),
                    loanDetail
                )
            );

        } catch (RuntimeException e) {
            log.error("대출상세조회 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                CommonResponse.error(
                    UUID.randomUUID().toString(),
                    getCurrentDateTime(),
                    "A9999",
                    e.getMessage()
                )
            );
        }
    }
}
