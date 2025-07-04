package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loan_inquiry_context")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanInquiryContext {

    @Id
    @Column(name = "user_seq_no", length = 10)
    private String userSeqNo;  // 사용자 ID

    @Column(name = "next_page_yn", length = 1)
    private String nextPageYn;  // 다음페이지 존재여부

    @Column(name = "befor_inquiry_trace_info", length = 20)
    private String beforInquiryTraceInfo;  // 이전 trace

    @Column(name = "page_record_cnt")
    private Integer pageRecordCnt;  // 조회 레코드 수

    @Column(name = "loan_cnt")
    private Integer loanCnt;  // 대출건수

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_seq_no")
    private User user;  // 사용자
}