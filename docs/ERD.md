# ERD 설계 

![ERD](/assets/images/erd/erd.png)

----

## 테이블
### 테이블명: user
유저 정보를 담는 유저 마스터 테이블이다.

| 컬럼명   | 데이터 타입  | 제약 조건   |
|---------------|------------|---------------|
| id            | int        | PK   |
| name          | varchar    |               |
| point         | int        |               |
| created_at    | timestamp  |               |
| updated_at    | timestamp  |               |

### 테이블명: point_history
포인트 충전, 사용 이력을 쌓는 테이블이다.

| 컬럼명   | 데이터 타입  | 제약 조건   |
|---------------|------------|---------------|
| id            | int        | PK   |
| user_id       | int        | FK |
| type          | varchar    |               |
| created_at    | timestamp  |               |

### 테이블명: user_coupon
유저가 보유하고 있는 쿠폰 정보를 담는 테이블이다.

| 컬럼명    | 데이터 타입  | 제약 조건   |
|----------------|------------|---------------|
| id             | int        | PK   |
| user_id        | int        | FK |
| coupon_id      | int        | FK |
| coupon_name    | varchar    |               |
| discount_type  | varchar    |               |
| discount_amt   | int        |               |
| valid_start_at | date       |               |
| valid_end_at   | date       |               |
| status         | varchar    |               |
| created_at     | timestamp  |               |
| updated_at     | timestamp  |               |

### 테이블명: product
상품 정보를 담고 있는 상품 마스터 테이블이다.

| 컬럼명   | 데이터 타입  | 제약 조건   |
|---------------|------------|---------------|
| id            | int        | PK   |
| name          | varchar    |               |
| status        | varchar    |               |
| stock         | int        |               |
| price         | int        |               |
| created_at    | timestamp  |               |
| updated_at    | timestamp  |               |

### 테이블명: order
주문 정보를 담고 있는 주문 마스터 테이블이다.

| 컬럼명    | 데이터 타입  | 제약 조건   |
|----------------|------------|---------------|
| id             | int        | PK   |
| user_id        | int        | FK |
| status         | varchar    |               |
| net_amt        | int        |               |
| discount_amt   | int        |               |
| total_amt      | int        |               |
| coupon_id      | int        | FK |
| created_at     | timestamp  |               |
| updated_at     | timestamp  |               |

### 테이블명: order_detail
주문 상품 정보를 담고 있는 테이블이다. 

| 컬럼명   | 데이터 타입  | 제약 조건 |
|---------------|------------|-------|
| id            | int        | PK    |
| order_id      | int        | FK    |
| product_id    | int        | FK    |
| quantity      | int        |       |
| created_at    | timestamp  |       |

### 테이블명: payment
결제 정보를 담고 있는 테이블이다. 

| 컬럼명   | 데이터 타입  | 제약 조건 |
|---------------|------------|-------|
| id            | int        | PK    |
| order_id      | int        | FK    |
| status        | varchar    |       |
| total_amt     | int        |       |
| created_at    | timestamp  |       |
| updated_at    | timestamp  |       |

### 테이블명: coupon
쿠폰 정보를 담고 있는 쿠폰 마스터 테이블이다. 

| 컬럼명    | 데이터 타입  | 제약 조건   |
|----------------|------------|---------------|
| id             | int        | PK   |
| name           | varchar    |               |
| discount_type  | varchar    |               |
| discount_amt   | int        |               |
| valid_start_at | date       |               |
| valid_end_at   | date       |               |
| created_at     | timestamp  |               |
| updated_at     | timestamp  |               |

### 테이블명: coupon_history
어떤 사용자가 어떤 쿠폰을 받급 받았는지 이력을 쌓는 테이블이다. 

| 컬럼명   | 데이터 타입  | 제약 조건   |
|---------------|------------|---------------|
| id            | int        | PK   |
| coupon_id     | int        | FK |
| user_id       | int        | FK |
| created_at    | timestamp  |               |

---- 

## 테이블의 관계
* user(1) -> order(N): 유저는 여러 개의 주문을 생성할 수 있다.
* user(1) -> user_coupon(N): 유저는 여러 개의 쿠폰을 소유할 수 있다. 
* user(1) -> point_history(N): 유저는 여러 개의 포인트 충전, 사용 이력을 가질 수 있다.


* coupon(1) -> user_coupon(N): 하나의 쿠폰은 여러 사용자 쿠폰에 참조될 수 있다.
* coupon(1) -> coupon_history(N): 하나의 쿠폰은 여러 개의 쿠폰 발급 이력을 가질 수 있다.


* order(1) -> order_detail(N): 하나의 주문은 여러 개의 주문 상세(주문 상품)를 가질 수 있다.  
* order(1) -> payment(1): 하나의 주문은 하나의 결제를 참조한다.


* product(1) -> order_detail(N): 하나의 상품은 여러 주문 상세에 포함될 수 있다.  