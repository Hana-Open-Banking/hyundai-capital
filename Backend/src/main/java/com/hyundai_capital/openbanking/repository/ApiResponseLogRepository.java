package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.ApiResponseLog;
import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiResponseLogRepository extends JpaRepository<ApiResponseLog, String> {

    Optional<ApiResponseLog> findByApiTranId(String apiTranId);
    
    List<ApiResponseLog> findByUser(User user);
    
    List<ApiResponseLog> findByUserAndRspCode(User user, String rspCode);
    
    List<ApiResponseLog> findByBankTranId(String bankTranId);
    
    List<ApiResponseLog> findByBankTranDate(String bankTranDate);
}