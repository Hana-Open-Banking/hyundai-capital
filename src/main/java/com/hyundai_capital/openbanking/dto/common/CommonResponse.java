package com.hyundai_capital.openbanking.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    
    @JsonProperty("api_tran_id")
    private String apiTranId;           // API 거래고유번호
    
    @JsonProperty("api_tran_dtm")
    private String apiTranDtm;          // API 거래일시
    
    @JsonProperty("rsp_code")
    private String rspCode;             // 응답코드
    
    @JsonProperty("rsp_message")
    private String rspMessage;          // 응답메시지
    
    @JsonProperty("res_list")
    private T resList;                  // 응답목록
    
    public static <T> CommonResponse<T> success(String apiTranId, String apiTranDtm, T data) {
        return CommonResponse.<T>builder()
                .apiTranId(apiTranId)
                .apiTranDtm(apiTranDtm)
                .rspCode("A0000")
                .rspMessage("정상처리")
                .resList(data)
                .build();
    }
    
    public static <T> CommonResponse<T> error(String apiTranId, String apiTranDtm, String errorCode, String errorMessage) {
        return CommonResponse.<T>builder()
                .apiTranId(apiTranId)
                .apiTranDtm(apiTranDtm)
                .rspCode(errorCode)
                .rspMessage(errorMessage)
                .build();
    }
} 