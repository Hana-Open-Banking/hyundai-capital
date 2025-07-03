# 현대캐피탈 오픈뱅킹 백엔드 프로젝트

현대캐피탈 오픈뱅킹 API를 위한 백엔드 서버 프로젝트입니다.

## 프로젝트 구조

```
src/main/java/com/hyundai_capital/openbanking/
├── config/                  # 설정 클래스
├── controller/              # API 컨트롤러
├── dto/                     # 데이터 전송 객체
├── entity/                  # 엔티티 클래스
├── repository/              # 데이터 접근 계층
├── service/                 # 비즈니스 로직 계층
└── OpenbankingApplication.java  # 메인 애플리케이션 클래스
```

## 기술 스택

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- MySQL 8.0
- Docker & Docker Compose
- Swagger/OpenAPI 3.0

## 데이터베이스 ERD

```mermaid
erDiagram
    user ||--o{ loan_contract : "has"
    user ||--o{ oauth_token : "has"
    loan_contract ||--o{ loan_transaction : "has"
    oauth_client ||--o{ oauth_token : "issues"

    user {
        varchar(10) user_seq_no PK "사용자일련번호"
        varchar(100) user_ci UK "CI"
        varchar(20) user_name "이름"
        varchar(13) user_reg_num "주민등록번호(해싱)"
        varchar(6) gender "성별"
        varchar(100) password "비밀번호"
        varchar(20) phone_number "연락처"
        varchar(100) user_email UK "이메일"
        varchar(100) address "주소"
        varchar(100) user_di "중복가입확인정보"
        varchar(10) user_status "상태"
        datetime created_at "생성일시"
        datetime updated_at "수정일시"
    }

    loan_contract {
        varchar(20) loan_id PK "대출계약 ID"
        varchar(20) loan_account_num UK "대출계좌번호"
        varchar(10) user_seq_no FK "사용자일련번호"
        varchar(100) loan_product_name "대출상품명"
        decimal(15,2) loan_amount "대출금액"
        decimal(15,2) remaining_amount "잔액"
        decimal(5,2) interest_rate "금리"
        char(8) contract_date "계약일자"
        char(8) maturity_date "만기일자"
        int repayment_day "상환일(매월)"
        varchar(10) loan_status "계약상태 코드"
        varchar(20) loan_type "대출종류"
        datetime created_at "생성일시"
        datetime updated_at "수정일시"
    }

    loan_transaction {
        varchar(20) transaction_id PK "거래 ID"
        varchar(20) transaction_unique_no UK "거래고유번호"
        varchar(20) loan_id FK "대출계약 ID"
        datetime transaction_date "거래일시"
        decimal(15,2) transaction_amount "거래금액"
        decimal(15,2) after_balance "거래후잔액"
        varchar(20) transaction_type "거래구분"
        varchar(100) transaction_summary "거래적요"
        varchar(50) branch_name "점명"
        datetime created_at "생성일시"
    }

    oauth_token {
        varchar(100) access_token_id PK "엑세스 토큰 ID"
        varchar(10) client_id FK "클라이언트 ID"
        varchar(10) user_seq_no FK "사용자일련번호"
        int expires_in "만료 시간 (초)"
        varchar(100) refresh_token "리프레시 토큰"
        varchar(50) scope "권한 범위"
        datetime issued_at "생성 시간"
        datetime refreshed_at "갱신 시간"
        varchar(10) token_status "토큰 상태"
    }

    oauth_client {
        varchar(10) client_id PK "이용기관 ID"
        varchar(100) client_secret "이용기관 비밀키"
        varchar(100) client_name "이용기관 이름"
        varchar(200) redirect_uri "리다이렉트 URI"
        varchar(10) client_status "클라이언트 상태"
        datetime created_at "생성 시간"
    }
```

## 실행 방법

### 로컬 개발 환경 설정

1. MySQL 데이터베이스 설치 및 설정
   ```bash
   # MySQL 서버 실행
   docker run --name hyundai-capital-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=hyundai_capital_db -p 3306:3306 -d mysql:8.0
   
   # 스키마 및 데이터 초기화
   mysql -h localhost -P 3306 -u root -p hyundai_capital_db < src/main/resources/schema.sql
   mysql -h localhost -P 3306 -u root -p hyundai_capital_db < src/main/resources/data.sql
   ```

2. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

### Docker Compose를 사용한 실행

1. Docker와 Docker Compose 설치

2. 프로젝트 빌드 및 실행
   ```bash
   # 프로젝트 루트 디렉토리에서
   docker-compose up -d
   ```

3. 서비스 접속
   - API 서버: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

## API 문서

API 문서는 Swagger UI를 통해 확인할 수 있습니다.
- URL: http://localhost:8080/swagger-ui.html

## 주요 API 엔드포인트

### OAuth2 인증 API
- POST /oauth/token - 액세스 토큰 발급/갱신

### 사용자 관리 API
- POST /api/auth/login - 사용자 로그인
- POST /api/hyundai-capital/users/register - 사용자 회원가입

### 대출 관리 API
- POST /api/hyundai-capital/loans/apply - 대출 신청
- GET /api/hyundai-capital/loans/my-loans - 내 대출 조회

### 오픈뱅킹 API
- POST /v2.0/loan/transaction_list/search - 대출 거래내역 조회
- POST /v2.0/loan/contract/search - 대출 계약 조회

## 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.