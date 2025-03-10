# 부하 테스트 결과 보고서

## 목차 
- [1. 개요](#1-개요)
- [2. 테스트 결과](#2-테스트-결과)
    - [k6 수행 결과](#k6-수행-결과)
    - [중요 데이터 분석](#중요-데이터-분석)
- [3. 결과](#3-결과)
- [4. 추후 개선 방향](#4-추후-개선-방향)


## 1. 개요 
이커머스 시나리오에서 사용자 수요가 많은 높은 트래픽이 예상되는 상품 조회 API의 부하 테스트를 수행한 결과를 분석한 보고서입니다. 

--- 

## 2. 테스트 결과 
### k6 수행 결과 
![all_list](/assets/images/stress/stress-test.png)

### 중요 데이터 분석
- **p95 (95th Percentile)**
    - **필드 의미:** 전체 요청 중 95%가 이 시간 이하로 응답되었음을 의미합니다.
    - **해석:** http_req_duration의 p95가 `33.9ms`이므로, 전체 요청의 95%가 `33.9ms` 이내에 응답되었습니다.

- **p99 (99th Percentile)**
    - **필드 의미:** 전체 요청 중 99%가 이 시간 이하로 응답되었음을 의미합니다.
    - **해석:** 제공된 데이터에는 p99 값이 없지만, 일반적으로 서버의 최악의 응답 속도를 분석하는 데 사용됩니다.

- **http_req_duration (HTTP 요청 소요 시간)**
    - **필드 의미:** HTTP 요청이 시작되어 응답이 완료되기까지 걸린 시간을 나타냅니다.
    - **해석:** 평균 응답 시간이 `50.54ms`, 최소 `1.44ms`, 최대 `4.57s`이며, 95%의 요청이 `33.9ms` 이내에 처리되었습니다.

- **http_reqs (총 HTTP 요청 수)**
    - **필드 의미:** 테스트 중 발생한 전체 HTTP 요청의 개수를 나타냅니다.
    - **해석:** 총 `9190`개의 요청이 발생하였으며, 초당 `181.95`개의 요청이 처리되었습니다.

- **vus (현재 활성 가상 유저 수)**
    - **필드 의미:** 테스트 실행 시점에서 동작 중인 가상 유저(Virtual User)의 수를 의미합니다.
    - **해석:** 테스트 도중 `11`명의 가상 유저가 동시에 실행되었습니다.

- **vus_max (최대 가상 유저 수)**
    - **필드 의미:** 테스트 중 기록된 최대 동시 실행 가상 유저 수를 의미합니다.
    - **해석:** 테스트 과정에서 최대 `300`명의 가상 유저가 동시에 실행되었습니다.

- **http_req_failed (실패한 HTTP 요청 비율)**
    - **필드 의미:** 전체 요청 중 실패한 HTTP 요청의 비율을 나타냅니다.
    - **해석:** 실패율이 `0.00%`로 측정되었으며, 모든 요청이 정상적으로 처리되었습니다.  

--- 

## 3. 결과 
총 `9190`건의 요청이 발생하였으며, 초당 `181.95`건이 처리되었습니다.  
평균 응답 시간은 `50.54ms`, p95 응답 시간은 `33.9ms`로 양호한 수준입니다.  
최대 응답 시간은 `4.57s`로 일부 요청에서 지연이 발생하였으며, HTTP 요청 실패율은 `0.00%`로 모든 요청이 정상적으로 처리되었습니다.  

--- 

## 4. 추후 개선 방향
1. **캐싱 적용을 통한 부하 분산**
    - 현재는 캐싱이 적용되어 있지 않아 임시 유저 수가 `1000명`을 초과하면 커넥션 에러가 발생합니다.
    - 캐시를 적용하여 DB 부하를 줄여 더 많은 트래픽을 처리할 수 있도록 개선이 필요합니다.

2. **최대 응답 시간 단축**
    - 일부 요청에서 `4.57s`의 지연이 발생하였으며, 이를 해결하기 위해 비효율적인 DB 쿼리를 최적화하고, 비동기 처리 적용을 검토합니다.

3. **부하 테스트 환경 확장**
    - 현재 `300`명의 VU를 사용하였지만, 더 높은 부하 환경에서의 테스트를 진행하여 실제 서비스에서의 안정성 검토가 필요합니다. 

