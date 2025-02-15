# 이커머스 시나리오 Index 적용 보고서

----
# 목차 
* [개요](#개요)
    * [작성 배경](#작성-배경)
    * [Index](#index)
        * [Index란?](#index란)
        * [인덱스 Column 의 기준](#인덱스-column-의-기준)
        * [인덱스 종류](#인덱스-종류)
        * [인덱싱 하기 적절한 케이스](#인덱싱-하기-적절한-케이스)

* [이커머스 시나리오에서 Index 적용](#이커머스-시나리오에서-index-적용)
    * [적용 쿼리 리스트](#적용-쿼리-리스트)

* [각 쿼리의 인덱스 적용 테스트 결과 분석](#각-쿼리의-인덱스-적용-테스트-결과-분석)
    * [1. Point pointRepository.findByUserId(userId)](#1-point-pointrepositoryfindbyuseriduserid)
    * [2. Page<StockDto> getPagedProducts(Pageable pageable)](#2-pagestockdto-getpagedproductspageable-pageable)
    * [3. List<TopSellingProductDto> getTopSellingProducts(LocalDate todayDate, int limit)](#3-listsellingproductdto-gettopsellingproductslocaldate-todaydate-int-limit)
    * [3번 추가 테스트 - product 테이블 Hint 사용하여 조회 테스트](#3번-추가-테스트---product-테이블-hint-사용하여-조회-테스트)
    * [4. Page<IssuedCoupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable)](#4-pageissuedcoupon-getpagedusercouponslong-userid-localdatetime-currenttime-pageable-pageable)

* [결론](#결론)

* [출처](#출처)
----


# 개요
## 작성 배경
이커머스 시나리오에서 자주 조회되거나, 복잡한 쿼리에 대해 Index를 적용할 부분에 대해 분석해보고 Index를 적용하여, Index를 적용하지 않고 조회했을 때와 성능 차이를 비교하고 정리하기 위해 작성했다.

## Index
### Index란?
인덱스는 데이터베이스 테이블에 대한 검색 성능의 속도를 높여주는 자료 구조를 뜻한다.</br>
즉, 데이터를 빨리 찾기 위해 **특정 컬럼**을 기준으로 **미리 정렬**해놓은 표이다.

#### 장점
* 테이블을 조회하는 속도와 그에 따른 성능을 향상시킬 수 있다.
* 전반적인 시스템의 부하를 줄일 수 있다.

#### 단점
* 인덱스를 관리하기 위해 DB의 약 10%에 해당하는 저장공간이 필요하다.
* 인덱스를 항상 최신의 정렬 상태로 관리하기 위해 인덱스가 적용된 컬럼에 INSERT, UPDATE, DELETE가 수행된다면 추가 작업이 필요하고 부하가 발생한다.
* 인덱스를 잘못 사용할 경우 오히려 성능이 저하되는 역효과가 발생할 수 있다.

### 인덱스 Column 의 기준
인덱스 대상 컬럼의 핵심은 높은 카디널리티(Cardinality). 즉, 데이터의 중복이 적어 특정 값을 조회했을 때 적은 수의 레코드만 반환될수록 인덱스 효율이 높아진다.

### 인덱스 종류
#### 단일 인덱스
하나의 컬럼만을 기준으로 생성된 데이터베이스 인덱스를 의미한다.
```sql
CREATE INDEX idx_single ON point(user_id);
```

#### 복합 인덱스
2개 이상의 컬럼으로 구성된 데이터베이스 인덱스를 의미한다. 일반적으로 카디널리티가 높은 순으로 배치한다.
```sql
CREATE INDEX idx_multi ON order_detail(order_id, product_id, quantity);
```
* 복합 인덱스를 설정할 때 주의 사항

    * 복합 Index 의 경우 조건 순서와 일치시킬수록 높은 성능을 가진다.
    * 반드시 첫번째 인덱스 조건 은 조회 조건 중 하나에 포함되어야 한다.

#### UNIQUE INDEX(유니크 인덱스)
특정 컬럼(또는 컬럼 조합)에 대해 중복된 값을 허용하지 않도록 보장하는 인덱스이다.

```sql
CREATE UNIQUE INDEX idx_user_id_unique ON point(user_id);
```

#### 커버링 인덱스
인덱스만으로 테이블에 직접 접근하지 않고 필요한 데이터를 모두 가져올 수 있는 인덱스를 의미한다.

### 인덱싱 하기 적절한 케이스
* 데이터 중복이 적은 컬럼
  카디널리티(Cardinality)가 높은, 즉 중복이 적은 컬럼일수록 검색 성능 향상에 유리하다.
  선택도가 높아 특정 조건의 데이터를 빠르게 찾을 수 있기 때문이다.

* 데이터 삽입, 수정이 적은 컬럼
  인덱스는 데이터 변경(INSERT, UPDATE, DELETE)이 발생할 때마다 함께 갱신되므로,
  변경이 잦은 컬럼에 인덱스를 설정하면 성능 저하가 발생할 수 있다.

* 조회에 자주 사용되는 컬럼
  자주 조회하거나 WHERE 조건, JOIN, ORDER BY 등에 사용되는 컬럼에 인덱스를 설정하면
  쿼리 성능을 극대화할 수 있다.

* 인덱스 또한 공간을 차지함
  인덱스도 결국 데이터로 관리되기 때문에, 인덱스가 많아지면
  디스크 공간 사용량 증가 및 데이터 변경 시 오버헤드 발생으로 이어질 수 있다.
  따라서 최소한의 효율적인 인덱스만 설정하는 것이 중요하다.

------

# 이커머스 시나리오에서 Index 적용
## 적용 쿼리 리스트
포인트
* `Point pointRepository.findByUserId(userId)` : 유저의 보유 포인트 조회

상품
* `Page<StockDto> getPagedProducts(Pageable pageable)` : 판매중인 상품 리스트를 페이징 처리하여 조회
* `List<TopSellingProductDto> getTopSellingProducts(LocalDate todayDate, int limit)` : 최근 3일간 가장 많이 팔린 인기 상품 조회

유저 쿠폰
* `Page<IssuedCoupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable)` : 유저가 보유한 쿠폰 리스트 페이징 처리하여 조회


----
## 각 쿼리의 인덱스 적용 테스트 결과 분석

### 1. Point pointRepository.findByUserId(userId)

#### 테스트 쿼리

```sql 
select 
	   user_id
     , point
from point 
where user_id = 3456345
```
쿼리 설명 : userId로 유저의 보유 포인트를 조회한다.

#### 테스트 데이터 개수
point 테이블 : 데이터 1000만건

#### 인덱스 적용 전
* 수행 시간 : 1.804646s
* Explain 실행 계획

| id  | select_type | table | type | partitions | possible_keys | key  | key_len | ref  | rows    | filtered | Extra        |
|-----|--------------|--------|-------|-------------|----------------|------|----------|-------|----------|----------|---------------|
| 1   | SIMPLE       | point  | ALL   | [NULL]      | [NULL]         | [NULL] | [NULL]  | [NULL] | 9,705,439 | 10       | Using where |

#### 실행 계획 핵심 컬럼 해석

| 항목            | 값        | 해석                                                    |
|-----------------|-----------|----------------------------------------------------------|
| type            | ALL        | 풀 테이블 스캔      |
| rows            | 9,705,439  | 약 970만 건 탐색                                   |
| possible_keys   | [NULL]     | 사용 가능한 인덱스 없음                                 |
| key             | [NULL]     | 사용된 인덱스 없음                                      |
| Extra           | Using where| where 조건을 사용해 필터링                        |
#### 인덱스 적용 개선 방안 고려
where절에 사용한 `user_id` 컬럼에 인덱스 설정을 고려한다.


#### 인덱스 적용 후

* **단일 인덱스 적용**

```sql
CREATE UNIQUE INDEX idx_user_id_unique ON point(user_id);
```
* 인덱스 설명 :
  유저 한 명당 포인트 정보는 한개씩만 가질 수 있으므로 User와 Point 테이블은 1대1 매핑 관계이다.</br>
  point 테이블의 user_id 컬럼은 중복 값을 허용하지 않으므로 Unique Index를 적용하였다.

* 수행 시간 : 0.000764s
* Explain 실행 계획

| id  | select_type | table | type  | partitions | possible_keys   | key                | key_len | ref   | rows | filtered | Extra |
|-----|--------------|--------|-------|-------------|------------------|--------------------|----------|--------|-------|----------|--------|
| 1   | SIMPLE       | point  | const | [NULL]      | idx_user_id_unique | idx_user_id_unique | 8        | const | 1     | 100      | [NULL] |

#### 실행 계획 핵심 컬럼 해석

| 항목            | 값                  | 해석                                                   |
|-----------------|----------------------|---------------------------------------------------------|
| type            | const                | 단일 행 조회 |
| rows            | 1                    | 1건 조회                                           |
| possible_keys   | idx_user_id_unique   | 사용 가능한 인덱스 존재                                 |
| key             | idx_user_id_unique   | idx_user_id_unique 인덱스 사용                          | 


#### 결과 분석
인덱스 적용 전 1.804646s가 걸렸던 조회가 단일 인덱스를 적용 후 0.000764s로 감소했다. 단일 인덱스를 적용하여 조회 성능이 99.96% 개선되었다.

---

### 2. Page&lt;StockDto&gt; getPagedProducts(Pageable pageable)

#### 테스트 쿼리
```sql
select 
            p.id
	  , p.name 
	  , p.price 
	  , ps.quantity 
from product p
join product_stock ps
on p.id = ps.product_id 
where p.selling_status = "SELLING"
order by p.id 
limit 0, 100;  
```
쿼리 설명 : 판매중인 상품의 정보를 페이징 처리하여 조회한다.

#### 테스트 데이터 개수
product 테이블 : 데이터 1000만건</br>
product_stock 테이블 : 데이터 1000만건

#### 인덱스 적용 전
* 수행 시간 : 11.353469s
* Explain 실행 계획

| id  | select_type | table | type   | partitions | possible_keys | key     | key_len | ref                                 | rows    | filtered | Extra                           |
|-----|--------------|--------|--------|-------------|----------------|---------|----------|---------------------------------------|----------|----------|-----------------------------------|
| 1   | SIMPLE       | ps    | ALL    | [NULL]      | [NULL]         | [NULL]  | [NULL]  | [NULL]                              | 9,965,466 | 100      | Using temporary; Using filesort |
| 1   | SIMPLE       | p     | eq_ref | [NULL]      | PRIMARY        | PRIMARY | 8        | none_index_db.ps.product_id          | 1        | 10       | Using where                     |

#### 실행 계획 핵심 컬럼 해석
* product_stock 테이블

| 항목            | 값                                      | 해석                                                    |
|-----------------|------------------------------------------|----------------------------------------------------------|
| type            | ALL                                      | 풀 테이블 스캔       |
| rows            | 9,965,466                                | 약 996만 건 탐색                                    |
| possible_keys   | [NULL]                                   | 사용 가능한 인덱스 없음                                  |
| key             | [NULL]                                   | 사용된 인덱스 없음                                       |
| Extra           | Using temporary; Using filesort           | 임시 테이블 생성 및 파일 정렬 발생하여 성능 저하 발생 가능       |

* product 테이블

| 항목            | 값        | 해석                                                    |
|-----------------|-----------|----------------------------------------------------------|
| type            | eq_ref    | 기본키 또는 유니크 인덱스 기반 조인         |
| rows            | 1         | 1건 조회                                             |
| possible_keys   | PRIMARY   | 사용 가능한 인덱스 존재                                  |
| key             | PRIMARY   | PRIMARY KEY 인덱스 사용                                  |
| Extra           | Using where | where 조건으로 필터링                               |

#### 인덱스 적용 개선 방안 고려
* 문제점
  product_stock 테이블에 인덱스가 없어 풀 테이블 스캔이 발생한다.
* 개선 방법
  product 테이블의 id 컬럼과 join하는 product_stock 테이블의 product_id 컬럼에 인덱스를 설정한다.


#### 인덱스 적용 후

* **단일 인덱스, 유니크 인덱스 적용**

```sql
CREATE INDEX idx_selling_status ON product(selling_status);
CREATE UNIQUE INDEX idx_product_id_unique ON product_stock(product_id);
 ```
* 인덱스 설명
    * 상품 목록 조회 시 판매중인 상품만 조회하므로 `product`는 `selling_status`를 항상 조건으로 걸기 때문에 `selling_status`를 단일 인덱스로 생성하였다.
    * 상품과 상품의 재고는 1대1로 맵핑된다. `product_stock`의 `product_id`는 `product`의 PK인 `id`와 조인되고, product_stock의 `product_id`는 중복 값을 허용하지 않으므로 Unique Index를 적용하였다.

* 수행 시간 : 0.007859s
* Explain 실행 계획</br>
  p : product 테이블</br>
  ps : product_stock 테이블

| id  | select_type | table | type  | partitions | possible_keys                    | key                        | key_len | ref                    | rows    | filtered | Extra |
|-----|--------------|--------|-------|-------------|------------------------------------|-----------------------------|----------|-------------------------|----------|----------|--------|
| 1   | SIMPLE       | p     | ref   | [NULL]      | PRIMARY, idx_product_selling_status | idx_product_selling_status | 1022     | const                  | 4,844,468 | 100      | [NULL] |
| 1   | SIMPLE       | ps    | eq_ref| [NULL]      | idx_product_id_unique             | idx_product_id_unique       | 8        | index_db.p.id          | 1        | 100      | [NULL] |

#### 실행 계획 핵심 컬럼 해석

* product 테이블

| 항목            | 값                          | 해석                                                    |
|-----------------|------------------------------|----------------------------------------------------------|
| type            | ref                          | 인덱스를 활용한 조건 검색         |
| rows            | 4,844,468                    | 약 480만 건 탐색  |
| possible_keys   | PRIMARY, idx_product_selling_status | 사용 가능한 인덱스 존재                          |
| key             | idx_product_selling_status   | idx_product_selling_status 인덱스 사용                    |

* product_stock 테이블

| 항목            | 값                          | 해석                                                    |
|-----------------|------------------------------|----------------------------------------------------------|
| type            | eq_ref                       | 기본키 또는 유니크 인덱스 기반 조인      |
| rows            | 1                            | 1건 조회                                             |
| possible_keys   | idx_product_id_unique         | 사용 가능한 인덱스 존재                                  |
| key             | idx_product_id_unique         | idx_product_id_unique 인덱스 사용                        |  


#### 결과 분석
인덱스 적용 전 product_stock에 적용 가능한 인덱스가 없어 풀 스캔 테이블 데이터 조회(약 996만)가 인덱스 적용 후 1건으로 조회된다.</br>

인덱스 적용 전 11.353469s 였던 수행 시간을 인덱스 적용 후 0.007859s로 감소시켜 조회 성능이 약 99.93% 개선되었다.
  
---

### 3. List&lt;TopSellingProductDto&gt; getTopSellingProducts(LocalDate todayDate, int limit)

#### 테스트 쿼리

```sql
select
        p.id,
        p.name,
        p.price,
        sum(od.quantity) as total_quantity
    from
        `order` o
    join
        order_detail od 
            on o.id = od.order_id 
    join
        product p 
            on od.product_id = p.id 
    where
    	p.selling_status = "SELLING"
        and o.status = "PAID" 
        and o.created_at between "2025-02-12 00:00:00" and "2025-02-14 23:59:59" 
    group by
        p.id,
        p.name,
        p.price 
    order by
        total_quantity desc 
    limit 5;
```  
쿼리 설명 : 최근 3일(오늘 기준 3일 전 00시부터 1일 전 23시 59분 59초까지)동안 판매된 상품중
판매량이 가장 많은 상위 5개 상품을 구하는 쿼리

#### 테스트 데이터 개수
product 테이블 : 데이터 1000만건</br>
product_stock 테이블 : 데이터 1000만건</br>
order 테이블 : 데이터 1000만건</br>
order_detail 테이블 : 데이터 1500만건

#### 인덱스 적용 전
* 수행 시간 : 44.690238s
* Explain 실행 계획</br>
  od : order_detail 테이블</br>
  o : order 테이블</br>
  p : product 테이블

| id  | select_type | table | type   | partitions | possible_keys | key     | key_len | ref                                 | rows    | filtered | Extra                                    |
|-----|--------------|--------|--------|-------------|----------------|---------|----------|---------------------------------------|----------|----------|--------------------------------------------|
| 1   | SIMPLE       | od    | ALL    | [NULL]      | [NULL]         | [NULL]  | [NULL]  | [NULL]                              | 14,922,312 | 100      | Using where; Using temporary; Using filesort |
| 1   | SIMPLE       | o     | eq_ref | [NULL]      | PRIMARY        | PRIMARY | 8        | none_index_db.od.order_id            | 1        | 5        | Using where                              |
| 1   | SIMPLE       | p     | eq_ref | [NULL]      | PRIMARY        | PRIMARY | 8        | none_index_db.od.product_id          | 1        | 10       | Using where                              |

#### 실행 계획 핵심 컬럼 해석
* order_detail 테이블

| 항목            | 값                                           | 해석                                                    |
|-----------------|----------------------------------------------|----------------------------------------------------------|
| type            | ALL                                           | 풀 테이블 스캔(Full Table Scan), 비효율적 접근 방식       |
| rows            | 14,922,312                                    | 약 1,492만 건 탐색 예상                                  |
| possible_keys   | [NULL]                                        | 사용 가능한 인덱스 없음                                  |
| key             | [NULL]                                        | 사용된 인덱스 없음                                       |
| Extra           | Using where; Using temporary; Using filesort  | WHERE 조건 필터링, 임시 테이블 생성 및 파일 정렬 발생 → 성능 저하 |

* order 테이블

| 항목            | 값        | 해석                                                    |
|-----------------|-----------|----------------------------------------------------------|
| type            | eq_ref    | 기본키 또는 유니크 인덱스 기반 조인, 매우 효율적         |
| rows            | 1         | 1건 조회 예상                                             |
| possible_keys   | PRIMARY   | 사용 가능한 인덱스 존재                                  |
| key             | PRIMARY   | PRIMARY KEY 인덱스 사용                                  |
| Extra           | Using where | WHERE 조건 필터링 수행                                   |
* product 테이블

| 항목            | 값        | 해석                                                    |
|-----------------|-----------|----------------------------------------------------------|
| type            | eq_ref    | 기본키 또는 유니크 인덱스 기반 조인, 매우 효율적         |
| rows            | 1         | 1건 조회 예상                                             |
| possible_keys   | PRIMARY   | 사용 가능한 인덱스 존재                                  |
| key             | PRIMARY   | PRIMARY KEY 인덱스 사용                                  |
| Extra           | Using where | WHERE 조건 필터링 수행                                   |

#### 인덱스 적용 개선 방안 고려
order_detail 테이블에 풀 테이블 스캔 및 Using temporary, Using filesort 발생하여 성능이 저하된다.</br>
order_detail 테이블 where 절이나 join 조건에 맞는 인덱스 추가를 고려한다.

#### 인덱스 적용 후
🛑 테스트 전제 설명 : product, order 테이블의 인덱싱 조건은 동일하다.
```sql
CREATE INDEX idx_product_selling_status ON product(selling_status);
CREATE INDEX idx_order_multi ON `order`(status, created_at);
```
order_detail의 멀티 인덱스를 (order_id, product_id), (order_id, product_id, quantity)로 각각 적용하여 테스트하여 비교하였다.

#### 1. order_detail(order_id, product_id)
```sql
CREATE INDEX idx_order_detail_multi ON order_detail(order_id, product_id);
```

* 수행 시간 : 50.864944s
* Explain 실행 계획

| id  | select_type | table | type  | partitions | possible_keys                  | key                     | key_len | ref                         | rows    | filtered | Extra                                        |
|-----|--------------|--------|-------|-------------|----------------------------------|--------------------------|----------|------------------------------|----------|----------|------------------------------------------------|
| 1   | SIMPLE       | o     | index | [NULL]      | PRIMARY, idx_order_multi        | idx_order_multi          | 1030     | [NULL]                      | 9,734,122 | 50       | Using where; Using index; Using temporary; Using filesort |
| 1   | SIMPLE       | od    | ref   | [NULL]      | idx_order_detail_multi          | idx_order_detail_multi   | 9        | index_db.o.id               | 1        | 100      | [NULL]                                       |
| 1   | SIMPLE       | p     | eq_ref| [NULL]      | PRIMARY, idx_product_selling_status | PRIMARY                | 8        | index_db.od.product_id      | 1        | 50       | Using where                                  |

#### 실행 계획 핵심 컬럼 해석

* order 테이블

| 항목            | 값                                           | 해석                                                       |
|-----------------|----------------------------------------------|-------------------------------------------------------------|
| type            | index                                         | 인덱스 전체 스캔(Index Full Scan), 불필요한 데이터 읽기로 비효율적 |
| rows            | 9,734,122                                     | 약 973만 건 탐색                                     |
| possible_keys   | PRIMARY, idx_order_multi                      | 사용 가능한 인덱스 존재                                     |
| key             | idx_order_multi                               | idx_order_multi 인덱스 사용                                 |
| Extra           | Using where; Using index; Using temporary; Using filesort | where 조건 필터링, 인덱스 스캔이지만 임시 테이블 생성 및 파일 정렬 발생하여 성능 저하 가능성 |

* order_detail 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | ref                    | 인덱스를 활용한 조건 검색                 |
| rows            | 1                      | 1건 조회 예상                                             |
| possible_keys   | idx_order_detail_multi | 사용 가능한 인덱스 존재                                  |
| key             | idx_order_detail_multi | idx_order_detail_multi 인덱스 사용                       |

* product 테이블

| 항목            | 값                                      | 해석                                                    |
|-----------------|------------------------------------------|----------------------------------------------------------|
| type            | eq_ref                                   | 기본키 또는 유니크 인덱스 기반 조인      |
| rows            | 1                                        | 1건 조회 예상                                             |
| possible_keys   | PRIMARY, idx_product_selling_status      | 사용 가능한 인덱스 존재                                  |
| key             | PRIMARY                                  | PRIMARY KEY 인덱스 사용                                  |
| Extra           | Using where                              | where 조건 필터링 수행  

#### 2. order_detail(order_id, product_id, quantity)
```sql
CREATE INDEX idx_order_detail_multi ON order_detail(order_id, product_id, quantity);
```

* 수행 시간 : 31.902848s
* Explain 실행 계획

| id  | select_type | table | type  | partitions | possible_keys                  | key                     | key_len | ref                         | rows    | filtered | Extra                                        |
|-----|--------------|--------|-------|-------------|----------------------------------|--------------------------|----------|------------------------------|----------|----------|------------------------------------------------|
| 1   | SIMPLE       | o     | index | [NULL]      | PRIMARY, idx_order_multi        | idx_order_multi          | 1030     | [NULL]                      | 9,734,122 | 50       | Using where; Using index; Using temporary; Using filesort |
| 1   | SIMPLE       | od    | ref   | [NULL]      | idx_order_detail_multi          | idx_order_detail_multi   | 9        | index_db.o.id               | 1        | 100      | Using index                                   |
| 1   | SIMPLE       | p     | eq_ref| [NULL]      | PRIMARY, idx_product_selling_status | PRIMARY                | 8        | index_db.od.product_id      | 1        | 50       | Using where                                  |

#### 실행 계획 핵심 컬럼 해석

* order 테이블

| 항목            | 값                                           | 해석                                                       |
|-----------------|----------------------------------------------|-------------------------------------------------------------|
| type            | index                                         | 인덱스 전체 스캔(Index Full Scan), 불필요한 데이터 읽기로 비효율적 |
| rows            | 9,734,122                                     | 약 973만 건 탐색                                      |
| possible_keys   | PRIMARY, idx_order_multi                      | 사용 가능한 인덱스 존재                                     |
| key             | idx_order_multi                               | idx_order_multi 인덱스 사용                                 |
| Extra           | Using where; Using index; Using temporary; Using filesort | where 조건 필터링, 인덱스 스캔이지만 임시 테이블 생성 및 파일 정렬 발생하여 성능 저하 가능성 존재 |

* order_detail 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | ref                    | 인덱스를 활용한 조건 검색, 비교적 효율적                 |
| rows            | 1                      | 1건 조회 예상                                             |
| possible_keys   | idx_order_detail_multi | 사용 가능한 인덱스 존재                                  |
| key             | idx_order_detail_multi | idx_order_detail_multi 인덱스 사용                       |
| Extra           | Using index            | 커버링 인덱스 사용 |

* product 테이블

| 항목            | 값                                      | 해석                                                    |
|-----------------|------------------------------------------|----------------------------------------------------------|
| type            | eq_ref                                   | 기본키 또는 유니크 인덱스 기반 조인        |
| rows            | 1                                        | 1건 조회                                            |
| possible_keys   | PRIMARY, idx_product_selling_status      | 사용 가능한 인덱스 존재                                  |
| key             | PRIMARY                                  | PRIMARY KEY 인덱스 사용                                  |
| Extra           | Using where                              | where 조건 필터링                                 |


> Q. 1번의 order_detail 멀티 인덱스와 2번의 order_detail 멀티 인덱스의 차이는 order_detail 멀티 인덱스 마지막에 quantity가 추가된건데 인덱싱을 타고 안타고의 차이가 있다?</br>
A. 1번처럼 (order_id, product_id)로 멀티 인덱스를 생성하면 (order_id, product_id)로 데이터를 필터링한 후, quantity 값을 찾으러 테이블에 접근을 해야한다.</br>
2번처럼 quantity까지 포함하여 (order_id, product_id, quantity) 멀티 인덱스를 생성하면 커버링 인덱스가 되어, 테이블에 접근하지 않고도 인덱스에서 quantity를 사용하여 테이블에 접근하지 않으므로 조회 성능이 빨라진다.


### 3번 추가 테스트 - product 테이블 Hint 사용하여 조회 테스트

| id  | select_type | table | type  | partitions | possible_keys                  | key                     | key_len | ref                         | rows    | filtered | Extra                                        |
|-----|--------------|--------|-------|-------------|----------------------------------|--------------------------|----------|------------------------------|----------|----------|------------------------------------------------|
| 1   | SIMPLE       | o     | index | [NULL]      | PRIMARY, idx_order_multi        | idx_order_multi          | 1030     | [NULL]                      | 9,734,122 | 50       | Using where; Using index; Using temporary; Using filesort |
| 1   | SIMPLE       | od    | ref   | [NULL]      | idx_order_detail_multi          | idx_order_detail_multi   | 9        | index_db.o.id               | 1        | 100      | Using index                                   |
| 1   | SIMPLE       | p     | eq_ref| [NULL]      | PRIMARY, idx_product_selling_status | PRIMARY                | 8        | index_db.od.product_id      | 1        | 50       | Using where|

배경 : Explain 실행 계획을 보면 가장 하단의 product의 인덱스 PRIMARY와 idx_product_selling_status 두 가지 possible_keys중에 옵티마이저가 자동으로 PRIMARY 적용시키는 것을 알 수 있다. idx_product_selling_status 강제로 적용하도록 Hint를 사용해서 테스트를 진행해보았다.


테스트 전제 조건은 3번과 동일하고 product에 인덱스를 idx_product_selling_status를 타도록 강제한 부분만 다르다.

#### 실행 쿼리
```sql
select
    p.id,
    p.name,
    p.price,
    sum(od.quantity) as total_quantity
from
    `order` o
join
    order_detail od
    on o.id = od.order_id
join
    product p FORCE INDEX (idx_product_selling_status) 
    on od.product_id = p.id
where
    p.selling_status = 'SELLING'
    and o.status = 'PAID'
    and o.created_at between '2025-02-12 00:00:00' and '2025-02-14 23:59:59'
group by
    p.id,
    p.name,
    p.price
order by
    total_quantity desc
limit 5;  
```
쿼리 설명 : 최근 3일(오늘 기준 3일 전 00시부터 1일 전 23시 59분 59초까지)동안 판매된 상품중
판매량이 가장 많은 상위 5개 상품을 구하는 쿼리 + **product에 hint 사용**

#### 1. order_detail(order_id, product_id)
```sql
CREATE INDEX idx_order_detail_multi ON order_detail(order_id, product_id);
```
* 수행 시간 : 41.972579s
* Explain 실행 계획

| id  | select_type | table | type  | partitions | possible_keys                  | key                     | key_len | ref                         | rows    | filtered | Extra                                        |
|-----|--------------|--------|-------|-------------|----------------------------------|--------------------------|----------|------------------------------|----------|----------|------------------------------------------------|
| 1   | SIMPLE       | o     | index | [NULL]      | PRIMARY, idx_order_multi        | idx_order_multi          | 1030     | [NULL]                      | 9,734,122 | 50       | Using where; Using index; Using temporary; Using filesort |
| 1   | SIMPLE       | od    | ref   | [NULL]      | idx_order_detail_multi          | idx_order_detail_multi   | 9        | index_db.o.id               | 1        | 100      | [NULL]                                       |
| 1   | SIMPLE       | p     | eq_ref| [NULL]      | idx_product_selling_status      | idx_product_selling_status | 1030   | const, index_db.od.product_id | 1        | 100      | [NULL]                                       |

#### 실행 계획 핵심 컬럼 해석
1. order 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | index                  | 인덱스 전체 스캔(Index Full Scan), 불필요한 데이터 읽기로 비효율적 |
| rows            | 9,734,122              | 약 973만 건 탐색 예상                                     |
| possible_keys   | PRIMARY, idx_order_multi | 사용 가능한 인덱스 존재                                  |
| key             | idx_order_multi         | idx_order_multi 인덱스 사용                              |
| Extra           | Using where; Using index; Using temporary; Using filesort | 임시 테이블, 파일 정렬 발생하여 성능 저하 발생 가능성 |

2. order_detail 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | ref                    | 인덱스를 활용한 조건 검색, 비교적 효율적                 |
| rows            | 1                      | 1건 조회 예상                                             |
| possible_keys   | idx_order_detail_multi  | 사용 가능한 인덱스 존재                                  |
| key             | idx_order_detail_multi  | idx_order_detail_multi 인덱스 사용                       |

3. product 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | eq_ref                 | 기본키 또는 유니크 인덱스 기반 조인, 매우 효율적         |
| rows            | 1                      | 1건 조회 예상                                             |
| possible_keys   | idx_product_selling_status | 사용 가능한 인덱스 존재                               |
| key             | idx_product_selling_status | idx_product_selling_status 인덱스 사용                  |


#### 2. order_detail(order_id, product_id, quantity)
```sql
CREATE INDEX idx_order_detail_multi ON order_detail(order_id, product_id, quantity);
```
* 수행 시간 : 37.666322s
* Explain 실행 계획

| id  | select_type | table | type  | partitions | possible_keys                  | key                     | key_len | ref                         | rows    | filtered | Extra                                        |
|-----|--------------|--------|-------|-------------|----------------------------------|--------------------------|----------|------------------------------|----------|----------|------------------------------------------------|
| 1   | SIMPLE       | o     | index | [NULL]      | PRIMARY, idx_order_multi        | idx_order_multi          | 1030     | [NULL]                      | 9,734,122 | 50       | Using where; Using index; Using temporary; Using filesort |
| 1   | SIMPLE       | od    | ref   | [NULL]      | idx_order_detail_multi          | idx_order_detail_multi   | 9        | index_db.o.id               | 1        | 100      | Using index                                   |
| 1   | SIMPLE       | p     | eq_ref| [NULL]      | idx_product_selling_status      | idx_product_selling_status | 1030   | const, index_db.od.product_id | 1        | 100      | [NULL]                                       |

#### 실행 계획 핵심 컬럼 해석

1. order 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | index                  | 인덱스 전체 스캔(Index Full Scan), 불필요한 데이터 읽기로 비효율적 |
| rows            | 9,734,122              | 약 973만 건 탐색 예상                                     |
| possible_keys   | PRIMARY, idx_order_multi | 사용 가능한 인덱스 존재                                  |
| key             | idx_order_multi         | idx_order_multi 인덱스 사용                              |
| Extra           | Using where; Using index; Using temporary; Using filesort | 임시 테이블, 파일 정렬 발생하여 성능 저하 발생 가능성              |

2. order_detail 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | ref                    | 인덱스를 활용한 조건 검색, 비교적 효율적                 |
| rows            | 1                      | 1건 조회 예상                                             |
| possible_keys   | idx_order_detail_multi  | 사용 가능한 인덱스 존재                                  |
| key             | idx_order_detail_multi  | idx_order_detail_multi 인덱스 사용                       |
| Extra           | Using index             | 커버링 인덱스 사용 |

3. product 테이블

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | eq_ref                 | 기본키 또는 유니크 인덱스 기반 조인, 매우 효율적         |
| rows            | 1                      | 1건 조회 예상                                             |
| possible_keys   | idx_product_selling_status | 사용 가능한 인덱스 존재                               |
| key             | idx_product_selling_status | idx_product_selling_status 인덱스 사용                  |

#### 결과 분석
테스트 결과 인기상품 조회 쿼리에 인덱스를 적용해도 인덱스 적용 전과 성능이 크게 개선되지 않은 것을 확인할 수 있다. 이는, 대량의 order, order_detail, product 테이블이 조인되면서 결과 집합이 과도하게 커지고, 이후 GROUP BY, SUM, ORDER BY, LIMIT와 같은 집계 및 정렬 작업이 이루어지는데, 이 작업들에서 병목이 발생했다고 예상한다.

이 문제를 해결하기 위해서 추후에 개선 방향으로 통계 테이블을 생성하여 배치를 활용하여 통계 데이터를 생성하는 방법을 통해 개선할 수 있을것 같다.

---

### 4. Page&lt;IssuedCoupon&gt; getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable)

#### 테스트 쿼리
```sql
  select
        id,
        coupon_id,
        created_at,
        discount_amt,
        discount_type,
        issued_at,
        coupon_name,
        status,
        updated_at,
        used_at,
        user_id,
        valid_ended_at,
        valid_started_at 
    from
        issued_coupon
    where
        user_id=1
        and status="UNUSED" 
        and valid_started_at >= "2024-01-01 00:00:00" 
        and valid_ended_at < "2025-02-12 00:00:00"  
    limit 0, 30;
```
쿼리 설명 : 유저가 보유한 쿠폰 중 사용 가능한 쿠폰만 페이징처리 하여 조회하는 쿼리

#### 테스트 데이터 개수
* issued_coupon 테이블 : 데이터 1500만건
* 1500만건 중 유저 1번에 대해 발행한 쿠폰 개수 200개

#### 인덱스 적용 전
* 수행 시간 : 0.006825s
* Explain 실행 계획

| id  | select_type | table         | type | partitions | possible_keys | key  | key_len | ref  | rows    | filtered | Extra        |
|-----|--------------|---------------|------|-------------|----------------|------|----------|-------|----------|----------|---------------|
| 1   | SIMPLE       | issued_coupon | ALL  | [NULL]      | [NULL]         | [NULL] | [NULL]  | [NULL] | 14,881,981 | 0.11     | Using where |

#### 실행 계획 핵심 컬럼 해석

| 항목            | 값                    | 해석                                                    |
|-----------------|------------------------|----------------------------------------------------------|
| type            | ALL                    | 풀 테이블 스캔(Full Table Scan), 비효율적 |
| rows            | 14,881,981             | 약 1,488만 건 탐색 예상                                   |
| possible_keys   | [NULL]                  | 사용 가능한 인덱스 없음                                   |
| key             | [NULL]                  | 사용된 인덱스 없음                                        |
| Extra           | Using where             | where 조건을 사용해 필터링                           |

#### 성능 개선 고려 사항
issued_coupon 테이블에서 풀 테이블 스캔 발생한다. where절에 사용하는 컬럼에 인덱스 추가 고려한다.


#### 인덱스 적용 후
````
CREATE INDEX idx_issued_coupon_multi ON issued_coupon(user_id, status, valid_started_at);
````

* 수행 시간 : 0.006525s
* Explain 실행 계획

| id  | select_type | table         | type  | partitions | possible_keys           | key                     | key_len | ref  | rows | filtered | Extra                                |
|-----|--------------|---------------|-------|-------------|---------------------------|--------------------------|----------|-------|------|----------|----------------------------------------|
| 1   | SIMPLE       | issued_coupon | range | [NULL]      | idx_issued_coupon_multi | idx_issued_coupon_multi | 1038     | [NULL] | 131  | 33.33    | Using index condition; Using where |

#### 실행 계획 핵심 컬럼 해석
| 항목            | 값                        | 해석                                                    |
|-----------------|----------------------------|----------------------------------------------------------|
| type            | range                      | 인덱스 범위 스캔(Index Range Scan), 범위 조건으로 부분 탐색 |
| rows            | 131                        | 약 131건 탐색 예상                                        |
| possible_keys   | idx_issued_coupon_multi     | 사용 가능한 인덱스 존재                                   |
| key             | idx_issued_coupon_multi     | idx_issued_coupon_multi 인덱스 사용                       |
| Extra           | Using index condition; Using where | 인덱스 조건 검색 및 where 필터링                     |


#### 결과 분석
예상과 달리 인덱스 적용 전후의 조회 성능 차이가 크지 않았다. 유저 1번이 보유한 쿠폰 개수가 200건 정도에 불과해, 결국 조회 대상이 200건으로 한정되다 보니 인덱스를 사용하든 테이블 전체를 스캔하든 실제 성능 차이가 크지 않았던 것으로 예상한다.

---

# 결론
인덱스 테스트를 통해 인덱스가 데이터 조회 성능에 미치는 영향이 상당히 크다는 점을 확인할 수 있었습니다.
다만, 단순히 인덱스를 추가하는 것만으로 무조건적인 성능 향상을 기대할 수는 없으며,
컬럼의 데이터 분포, 조건에 맞는 적합한 인덱스 구성 여부에 따라 성능 차이가 발생할 수 있음을 알 수 있었습니다.

또한, 인덱스 설정 이후에도 데이터 증가 및 접근 패턴 변화에 따라 인덱스 성능이 달라질 수 있으므로,
정기적인 모니터링과 실행 계획 점검(EXPLAIN 분석 등)을 통해 인덱스 효율성을 지속적으로 검토하고 최적화하는 과정이 필수적이라는 점을 확인했습니다.

----

# 출처
* [코딩팩토리 - 데이터베이스 인덱스(Index) 란 무엇인가?](https://coding-factory.tistory.com/746)
* [망나니개발자 - 인덱스(index)란?](https://mangkyu.tistory.com/96)
