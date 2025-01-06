# ERD 설계

![ERD](/assets/images/erd/erd.png)

----

## 테이블
### 테이블명: user
유저 정보를 담는 유저 마스터 테이블이다.

| 컬럼명        | 데이터 타입 | 설명                            |
|---------------|-------------|---------------------------------|
| id            | bigint      | 유저 고유 식별자 (Primary Key)   |
| name          | varchar     | 유저 이름                       |
| point         | int         | 유저가 보유한 포인트             |
| created_at    | date        | 유저 데이터 생성일               |
| updated_at    | date        | 유저 데이터 수정일               |

### 테이블명: point_history
포인트 충전, 사용 이력을 쌓는 테이블이다.

| 컬럼명        | 데이터 타입 | 설명                        |
|---------------|-------------|---------------------------|
| id            | bigint      | 포인트 이력 고유 식별자 (Primary Key) |
| user_id       | bigint      | 유저 ID                     |
| type          | varchar     | 포인트 발생 유형 (CHARGE-충전/USE) |
| created_at    | datetime    | 포인트 이력 생성일                |

### 테이블명: coupon
쿠폰 정보를 담고 있는 쿠폰 마스터 테이블이다.

| 컬럼명              | 데이터 타입   | 설명                                        |
|------------------|----------|-------------------------------------------|
| id               | bigint   | 쿠폰 고유 식별자 (Primary Key)                   |
| name             | varchar  | 쿠폰 이름                                     |
| discount_type    | varchar  | 쿠폰 할인 유형 (PERCENTAGE-정률/FIXED_AMOUNT-정액)  |
| discount_amt     | int      | 할인 금액                                     |
| max_capacity     | int      | 최대 발급 허용 개수                               |
| valid_started_at | datetime | 쿠폰 유효 시작일                                 |
| valid_ended_at   | datetime | 쿠폰 유효 종료일                                 |
| status           | varchar  | 쿠폰 상태(ACTIVE-발급 및 사용 가능/DEACTIVATED-관리자 비활성화) |
| created_at       | datetime | 쿠폰 데이터 생성일                                |
| updated_at       | datetime | 쿠폰 데이터 수정일                                |

### 테이블명: issued_coupon
유저가 보유하고 있는 쿠폰 정보를 담는 테이블이다.

| 컬럼명              | 데이터 타입 | 설명                                       |
|------------------|-------------|------------------------------------------|
| id               | bigint      | 발급 쿠폰의 고유 식별자 (Primary Key)              |
| coupon_id        | bigint      | 쿠폰 마스터 테이블의 쿠폰 ID                        |
| user_id          | bigint      | 쿠폰을 발급받은 유저 ID                           |
| coupon_name      | varchar     | 쿠폰 이름                                    |
| discount_type    | varchar     | 쿠폰 할인 유형 (PERCENTAGE-정률/FIXED_AMOUNT-정액) |
| discount_amt     | int         | 할인 금액                                    |
| issued_at        | datetime    | 쿠폰 발급일                                   | 
| valid_started_at | datetime    | 쿠폰 유효 시작일                                |
| valid_ended_at   | datetime    | 쿠폰 유효 종료일                                |
| used_at          | datetime    | 쿠폰 사용일                                   |
| status           | varchar     | 쿠폰 상태 (USED-사용/UNUSED-미사용/DEACTIVATED-관리자 비활성화)              |
| created_at       | datetime    | 발급 쿠폰 데이터 생성일                            |
| updated_at       | datetime    | 발급 쿠폰 데이터 수정일                            |

### 테이블명: product
상품 정보를 담고 있는 상품 마스터 테이블이다.

| 컬럼명        | 데이터 타입 | 설명                                |
|---------------|-------------|-----------------------------------|
| id            | bigint      | 상품 고유 식별자 (Primary Key)           |
| name          | varchar     | 상품 이름                             |
| status        | varchar     | 상품 상태 (SELLING-판매중/STOPPED-판매 종료) |
| price         | int         | 상품 가격                             |
| created_at    | datetime    | 상품 데이터 생성일                        |
| updated_at    | datetime    | 상품 데이터 수정일                        |

### 테이블명: product_stock 테이블
상품의 재고 정보를 담고 있는 재고 테이블이다.

| 컬럼명       | 데이터 타입 | 설명                     |
|--------------|-------------|--------------------------|
| id           | bigint      | 재고 고유 식별자 (Primary Key) |
| product_id   | bigint      | 상품 ID                  |
| quantity     | int         | 재고 수량                |
| created_at   | datetime    | 재고 데이터 생성일         |
| updated_at   | datetime    | 재고 데이터 수정일         |

### 테이블명: order
주문 정보를 담고 있는 주문 마스터 테이블이다.

| 컬럼명        | 데이터 타입 | 설명                             |
|---------------|-------------|--------------------------------|
| id            | bigint      | 주문 고유 식별자 (Primary Key)        |
| user_id       | bigint      | 주문한 유저 ID                      |
| status        | varchar     | 주문 상태 (COMPLETED-완료/FAILED-실패/CANCELED-취소) |
| net_amt       | int         | 할인 전 총 금액(순수 구매 금액)            |
| discount_amt  | int         | 할인 금액                          |
| total_amt     | int         | 최종 결제 금액(순수 구매 금액 - 할인 금액)     |
| coupon_id     | bigint      | 사용된 쿠폰 ID                      |
| created_at    | datetime    | 주문 데이터 생성일                     |
| updated_at    | datetime    | 주문 데이터 수정일                     |

### 테이블명: order_detail
주문 상품 정보를 담고 있는 테이블이다.

| 컬럼명        | 데이터 타입 | 설명                         |
|---------------|-------------|----------------------------|
| id            | bigint      | 주문 상세 고유 식별자 (Primary Key) |
| order_id      | bigint      | 주문 테이블의 주문 ID              |
| product_id    | bigint      | 주문한 상품 ID                  |
| quantity      | int         | 주문한 상품 수량                  |
| price         | int         | 상품 가격                      |
| created_at    | datetime    | 주문 상세 데이터 생성일              |

### 테이블명: payment
결제 정보를 담고 있는 테이블이다.

| 컬럼명        | 데이터 타입 | 설명                                         |
|---------------|-------------|--------------------------------------------|
| id            | bigint      | 결제 고유 식별자 (Primary Key)                    |
| order_id      | bigint      | 결제와 연결된 주문 ID                              |
| status        | varchar     | 결제 상태 (COMPLETED-완료/FAILED-실패/CANCELED-취소) |
| total_amt     | int         | 최종 결제 금액                                  |
| created_at    | datetime    | 결제 데이터 생성일                                 |
| updated_at    | datetime    | 결제 데이터 수정일                                 |

---- 

## 테이블의 관계
* user(1) -> order(N): 유저는 여러 개의 주문을 생성할 수 있다.
* user(1) -> issued_coupon(N): 유저는 여러 개의 쿠폰을 소유할 수 있다.
* user(1) -> point_history(N): 유저는 여러 개의 포인트 충전, 사용 이력을 가질 수 있다.


* coupon(1) -> issued_coupon(N): 하나의 쿠폰은 여러 사용자 쿠폰에 참조될 수 있다.


* order(1) -> order_detail(N): 하나의 주문은 여러 개의 주문 상세(주문 상품)를 가질 수 있다.
* order(1) -> payment(1): 하나의 주문은 하나의 결제를 참조한다.


* product(1) -> product_stock(1) : 하나의 상품은 하나의 재고 정보를 가진다.
* product(1) -> order_detail(N): 하나의 상품은 여러 주문 상세에 포함될 수 있다.  