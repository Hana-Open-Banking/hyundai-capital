package com.hyundai_capital.openbanking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(999) // DataInitializer 이후에 실행되도록 설정
public class StartupLogger implements CommandLineRunner {
    
    @Value("${server.port:8085}")
    private int serverPort;
    
    @Override
    public void run(String... args) throws Exception {
        String serverUrl = "http://localhost:" + serverPort;
        
        log.info("===========================================================");
        log.info("🚀 현대캐피탈 오픈뱅킹 서버가 성공적으로 시작되었습니다!");
        log.info("===========================================================");
        log.info("📊 서버 정보:");
        log.info("   - 서버 URL: {}", serverUrl);
        log.info("   - 프로필: {}", getActiveProfiles());
        log.info("");
        log.info("🌐 API 엔드포인트:");
        log.info("   - 헬스체크: {}/api/hyundai-capital/health", serverUrl);
        log.info("   - 회원가입: {}/api/hyundai-capital/users/register", serverUrl);
        log.info("   - 로그인: {}/api/hyundai-capital/users/login", serverUrl);
        log.info("   - 대출신청: {}/api/hyundai-capital/loans/apply", serverUrl);
        log.info("   - 내 대출조회: {}/api/hyundai-capital/loans/my-loans", serverUrl);
        log.info("");
        log.info("🏦 오픈뱅킹 API (금융결제원 연동):");
        log.info("   - 대출거래조회: {}/v2.0/loan/transaction_list/search", serverUrl);
        log.info("   - 대출계약조회: {}/v2.0/loan/contract/search", serverUrl);
        log.info("");
        log.info("📖 API 문서 (Swagger):");
        log.info("   - Swagger UI: {}/swagger-ui.html", serverUrl);
        log.info("   - API Docs: {}/api-docs", serverUrl);
        log.info("");
        log.info("🗄️ 데이터베이스:");
        log.info("   - H2 Console: {}/h2-console", serverUrl);
        log.info("   - JDBC URL: jdbc:h2:mem:testdb");
        log.info("   - Username: sa");
        log.info("   - Password: (비어있음)");
        log.info("");
        log.info("🧪 테스트 데이터:");
        log.info("   - 테스트 사용자: testuser001 / password123");
        log.info("   - 대출계좌: 자동생성됨 (HC + 타임스탬프)");
        log.info("");
        log.info("💡 사용법:");
        log.info("   1. 회원가입 또는 로그인");
        log.info("   2. 대출 신청");
        log.info("   3. 오픈뱅킹 API로 대출 정보 조회");
        log.info("   4. Swagger UI에서 API 테스트");
        log.info("===========================================================");
        log.info("✅ 모든 서비스가 준비되었습니다!");
        log.info("===========================================================");
    }
    
    private String getActiveProfiles() {
        String profiles = System.getProperty("spring.profiles.active");
        return profiles != null ? profiles : "default";
    }
} 