# Podongdong's pay - 간편결제 플랫폼

> MSA + 헥사고날 아키텍처 + DDD 기반 핀테크 플랫폼

사용자가 지갑에 충전하고, 가맹점에서 결제하고, 가맹점은 정산을 받는 간편결제 시스템입니다.

---

## 기술 스택

| 분류 | 기술 | 버전                     |
|---|---|------------------------|
| Language | Java, TypeScript | 25 (LTS)               |
| Backend | Spring Boot, Spring Data JPA | 4.0.3                  |
| Frontend | Vue 3, Pinia, Element Plus, Vite | 3.6 / 3.x / 2.11 / 8.x |
| Database | MySQL (서비스별 분리) | 9.x                    |
| Cache | Redis | 7.x                    |
| Messaging | Apache Kafka (KRaft), RabbitMQ | 4.x / 4.x              |
| Batch | Apache Spark | 4.1.1                  |
| Monitoring | ELK (Elasticsearch + Logstash + Kibana) | 9.0.3                  |
| Metrics | Prometheus + Grafana | latest                 |
| API Gateway | Nginx | alpine                 |
| Testing | JUnit 5, Mockito, Testcontainers, REST Assured | TC 2.x                 |
| Build | Gradle Kotlin DSL | 9.4.1                  |
| Container | Docker Compose | latest                 |

---

## 아키텍처

### 시스템 구성도

```
                        ┌─────────────┐
                        │   Nginx     │
                        │  (API G/W)  │
                        └──────┬──────┘
                               │
         ┌──────────┬──────────┼──────────┬──────────┐
         ▼          ▼          ▼          ▼          ▼
    ┌─────────┐┌─────────┐┌────────┐┌─────────┐┌────────┐
    │ Payment ││ Wallet  ││ Member ││Merchant ││Settlmt │
    │ :8081   ││ :8082   ││ :8083  ││ :8084   ││ :8085  │
    └────┬────┘└────┬────┘└────┬───┘└────┬────┘└───┬────┘
         │          │          │         │         │
    ─────┼──────────┼──────────┼─────────┼─────────┼─────
    │    ▼          ▼          ▼         ▼         ▼    │
    │  ┌──────┐ ┌──────┐ ┌────────┐ ┌────────────────┐ │
    │  │Redis │ │MySQL │ │ Kafka  │ │   RabbitMQ     │ │
    │  │      │ │(x4)  │ │(KRaft) │ │                │ │
    │  └──────┘ └──────┘ └───┬────┘ └────────┬───────┘ │
    │                        │               │         │
    │                   ┌────▼────┐   ┌──────▼───────┐ │
    │                   │ Spark   │   │ Notification │ │
    │                   │ (Batch) │   │   :8086      │ │
    │                   └─────────┘   └──────────────┘ │
    │                                                  │
    │  ┌────────────────────────────────────────────┐  │
    │  │ ELK Stack (Logstash → ES → Kibana)         │  │
    │  └────────────────────────────────────────────┘  │
    │              Infrastructure (Docker Compose)      │
    └──────────────────────────────────────────────────┘
```

### Bounded Context 맵

```
┌──────────────────────────────────────────────────────────┐
│                      PayFlow Platform                     │
│                                                          │
│  ┌─────────┐    Kafka     ┌─────────────┐   RabbitMQ   │
│  │ Payment │─────────────▶│ Settlement  │──────────┐   │
│  │ Context │              │  Context    │          │   │
│  └────┬────┘              └─────────────┘          │   │
│       │ HTTP (동기)                                 ▼   │
│  ┌────▼────┐                              ┌─────────┐  │
│  │ Wallet  │                              │Notific- │  │
│  │ Context │                              │ation    │  │
│  └─────────┘                              └─────────┘  │
│  ┌─────────┐    ┌──────────┐                           │
│  │ Member  │    │ Merchant │                           │
│  │ Context │    │ Context  │                           │
│  └─────────┘    └──────────┘                           │
└──────────────────────────────────────────────────────────┘
```

### 헥사고날 아키텍처 (Ports & Adapters)

```
payment-service/
├── domain/                    # 순수 도메인 (외부 의존성 ZERO)
│   ├── Payment.java           # Aggregate Root
│   ├── PaymentStatus.java     # Value Object (enum)
│   └── Money.java             # Value Object
├── application/
│   ├── PaymentService.java    # Application Service
│   ├── provided/              # Inbound Port (이 서비스가 제공하는 기능)
│   │   ├── RequestPayment.java
│   │   └── CancelPayment.java
│   └── required/              # Outbound Port (이 서비스가 요구하는 의존성)
│       ├── PaymentRepository.java
│       ├── PaymentEventPublisher.java
│       └── WalletClient.java
└── adapter/                   # 인프라 구현체
    ├── in/web/                # REST Controller
    └── out/
        ├── persistence/       # JPA Repository
        ├── messaging/         # Kafka Producer
        └── cache/             # Redis
```

---

## 마이크로서비스

| # | 서비스 | 포트 | DB | 핵심 인프라 | 역할 |
|---|---|---|---|---|---|
| 1 | payment-service | 8081 | mysql-payment (3306) | Kafka, Redis | 결제 요청/승인/취소, 분산락 |
| 2 | wallet-service | 8082 | mysql-wallet (3307) | Redis | 잔액 관리, 충전/차감, 거래 원장 |
| 3 | member-service | 8083 | mysql-member (3309) | - | 회원 가입/조회 |
| 4 | merchant-service | 8084 | mysql-member (3309) | - | 가맹점 등록/관리, 수수료 정책 |
| 5 | settlement-service | 8085 | mysql-settlement (3308) | Kafka, Spark | 정산 배치, 수수료 계산 |
| 6 | notification-service | 8086 | - (ELK only) | RabbitMQ | 알림 발송, DLQ 재처리 |

---

## 기술 선택 근거

| 기술 | 어디서 | 왜 |
|---|---|---|
| **Kafka (KRaft)** | Payment → Settlement | 결제 이벤트의 순서 보장 + 재처리(offset replay) 필요. 정산은 이벤트 유실 시 돈이 안 맞음 |
| **RabbitMQ** | → Notification | 알림은 즉시 전달 + 실패 시 DLQ 재처리가 핵심. 순서 불필요. Kafka는 이 용도에 과도 |
| **Redis** | Wallet 캐시, Payment 분산락 | 잔액 조회 고속화 + 중복결제 방지. DB Lock 대비 성능 우위 |
| **Spark** | Settlement 배치 | 수십만 건 거래 분산 집계. Spring Batch 대비 수평 확장 가능 |
| **ELK** | 전 서비스 로그 | MSA 환경 중앙 집중형 로그. 서비스별 에러 추적 + 어드민 대시보드 역할 |

**Kafka vs RabbitMQ 분리 기준**: 메시지의 생명주기가 다릅니다.
- Kafka: 이벤트 로그 (쓰고, 여러 컨슈머가 각자 읽고, 재처리 가능)
- RabbitMQ: 작업 큐 (한 번 소비하면 끝, 실패 시 DLQ)

---

## 핵심 플로우

### 결제 플로우

```
사용자 ──▶ [Payment Service]
              ├── Redis 분산락 획득 (중복결제 방지)
              ├──▶ [Wallet Service] 잔액 차감 (동기 HTTP)
              ├── 결제 상태 APPROVED 저장
              ├──▶ [Kafka] payment.approved → Settlement Service
              └──▶ [RabbitMQ] notification.exchange → Notification Service
```

### 정산 배치 플로우

```
매일 02:00 → [Settlement Service]
              ├── Kafka 이벤트로 적재된 정산 대상 조회
              ├──▶ [Spark Job] 가맹점별 집계 + 수수료 계산
              ├── 정산 결과 저장
              └──▶ [RabbitMQ] 가맹점 정산 완료 알림
```

### 알림 실패 재처리 (DLQ)

```
notification.queue → 실패 → notification.dlq (TTL 60s)
                           → 재시도 (최대 3회)
                           → notification.failed.queue (수동 확인)
```

---

## 시작하기

### 사전 요구사항

- Java 25+
- Docker & Docker Compose
- Node.js 20+ (프론트엔드)

### 1. 인프라 실행

```bash
docker compose up -d
```

### 2. 서비스 실행

```bash
# 각 서비스를 별도 터미널에서 실행
cd payment    && ./gradlew bootRun --args='--spring.profiles.active=local'
cd wallet     && ./gradlew bootRun --args='--spring.profiles.active=local'
cd member     && ./gradlew bootRun --args='--spring.profiles.active=local'
cd merchant   && ./gradlew bootRun --args='--spring.profiles.active=local'
cd settlement && ./gradlew bootRun --args='--spring.profiles.active=local'
cd notification && ./gradlew bootRun --args='--spring.profiles.active=local'
```

### 3. 확인

| 서비스 | URL |
|---|---|
| Swagger (Payment) | http://localhost:8081/swagger-ui.html |
| Swagger (Wallet) | http://localhost:8082/swagger-ui.html |
| Kibana | http://localhost:5601 |
| RabbitMQ 관리 | http://localhost:15672 (guest/guest) |
| Grafana | http://localhost:3000 (admin/admin) |
| Spark UI | http://localhost:8180 |

### 4. 빌드 & 테스트

```bash
# 전체 빌드
./gradlew clean build

# 단위 테스트만
./gradlew test

# 특정 모듈만
./gradlew :payment:test
```

---

## 프로젝트 구조

```
payflow/
├── payment/                   # 결제 서비스
├── wallet/                    # 지갑 서비스
├── member/                    # 회원 서비스
├── merchant/                  # 가맹점 서비스
├── settlement/                # 정산 서비스
├── notification/              # 알림 서비스
├── nginx/                     # API Gateway 설정
├── logstash/                  # ELK 파이프라인 설정
├── prometheus/                # 메트릭 수집 설정
├── compose.yaml               # Docker 인프라
├── build.gradle.kts           # 루트 빌드 (공통 의존성)
├── settings.gradle.kts        # 멀티 모듈 선언
├── 개발가이드.md               # 아키텍처 규칙, 실행 방법
├── 도메인모델.md               # Aggregate, 상태 흐름, Domain Event
├── 도메인모델.drawio           # ERD 다이어그램 (draw.io)
└── 용어사전.md                 # 비즈니스 용어 ↔ 코드 매핑
```

---

## 테스트 전략

| 계층 | 도구 | 대상 | 인프라 필요 |
|---|---|---|---|
| **단위 테스트** | JUnit 5 + Mockito | Domain, Application Service | 없음 (Port Mock) |
| **통합 테스트** | Testcontainers 2.x | Repository, Kafka/RabbitMQ | Docker 자동 기동 |
| **E2E 테스트** | REST Assured | 결제 → 정산 전체 흐름 | Docker Compose |
| **부하 테스트** | k6 | API 성능 측정 | Docker Compose + Grafana |

```java
// 헥사고날의 장점: 인프라 없이 도메인 로직 테스트
@Test
void 잔액_부족_시_결제_실패() {
    var walletClient = mock(WalletClient.class);
    given(walletClient.deduct(any(), any())).willReturn(INSUFFICIENT_BALANCE);

    assertThatThrownBy(() -> paymentService.requestPayment(request))
        .isInstanceOf(InsufficientBalanceException.class);
}
```

---

## 문서

| 문서 | 설명 |
|---|---|
| [개발가이드.md](개발가이드.md) | 아키텍처 규칙, 패키지 구조, 커밋 컨벤션, 테스트 규칙 |
| [도메인모델.md](도메인모델.md) | Aggregate, Value Object, 상태 흐름, Domain Event |
| [용어사전.md](용어사전.md) | 비즈니스 용어 80개+, 한국어 ↔ 영문 ↔ 코드 매핑 |

---

## 로드맵

- [x] **Phase 1**: 프로젝트 scaffolding + 도메인 모델 + 헥사고날 구조
- [ ] **Phase 1**: 서비스 간 통신 연결 (HTTP Client, Kafka/RabbitMQ 연동)
- [ ] **Phase 1**: ELK 로그 연동
- [ ] **Phase 1**: Vue 3 프론트엔드 (사용자 앱 + 가맹점 대시보드)
- [ ] **Phase 2**: 회원가입 / 로그인 (Spring Security + JWT)
- [ ] **Phase 2**: 소셜 로그인 (OAuth2 - 카카오/네이버/구글)
- [ ] **Phase 2**: 부하 테스트 + Grafana 대시보드
- [ ] **Phase 2**: CI/CD (GitHub Actions + Docker)
