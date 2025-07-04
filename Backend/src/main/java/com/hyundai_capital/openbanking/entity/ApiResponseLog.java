package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "api_response_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseLog {

    @Id
    @Column(name = "api_tran_id", length = 40)
    private String apiTranId;  // API 거래고유번호

    @Column(name = "api_tran_dtm", length = 17, nullable = false)
    private String apiTranDtm;  // 거래일시(ms)

    @Column(name = "rsp_code", length = 5, nullable = false)
    private String rspCode;  // 응답코드

    @Column(name = "rsp_message", length = 300)
    private String rspMessage;  // 응답메시지

    @Column(name = "bank_tran_id", length = 20)
    private String bankTranId;  // 참가기관 거래 ID

    @Column(name = "bank_tran_date", length = 8)
    private String bankTranDate;  // 거래일자

    @Column(name = "bank_code_tran", length = 3)
    private String bankCodeTran;  // 기관코드

    @Column(name = "bank_rsp_code", length = 3)
    private String bankRspCode;  // 기관 응답코드

    @Column(name = "bank_rsp_message", length = 100)
    private String bankRspMessage;  // 기관 응답메시지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq_no", nullable = false)
    private User user;  // 사용자
}