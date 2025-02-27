# 부하 테스트 계획서

## 1. 개요
이커머스 시나리오에서 사용자 수요가 많은 높은 트래픽이 예상되는 상품 조회 API의 성능과 안정성을 검증하기 위한 부하 테스트 계획을 정리한 문서입니다.

---

## 2. 부하 테스트 대상
### 2.1 테스트 대상 API

| **API 명**    | **HTTP Method** | **URL**                    | **설명**                         |
| ------------ | --------------- | -------------------------- |--------------------------------|
| **상품 목록 조회** | `GET`           | `/api/v1/products/selling` | 판매 상태가 "판매중"인 상품의 정보를 조회하는 API |

### 2.2 선정 배경
1. 상품 목록 조회는 대부분의 사용자들이 가장 많이 이용하는 기능 중 하나이며, 트래픽이 집중되는 주요 API입니다.
2. 한 번 조회하고 끝나는 것이 아니라 페이지네이션으로 인해 짧은 시간 내에 반복적인 다수의 요청 발생합니다.
3. 현재 상품 목록 조회 API에는 캐싱이 적용되지 않은 상태이며, 모든 요청이 DB를 직접 조회합니다. 캐시가 적용되지 않은 높은 트래픽에서도 서버가 정상적으로 동작하는지 확인합니다. 
---

## 3. 부하 테스트 시나리오

### 3.1 부하 테스트 시나리오 스크립트
```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    scenarios: {
        // 상품 목록 조회(기본 트래픽 설정)
        get_products_constant_scenario: {
            executor: 'constant-vus',
            vus: 100,  // 꾸준하게 100 VU 발생
            duration: '30s',  // 전체 테스트 지속 시간
            exec: 'getProducts',
        },

        // 상품 목록 조회(스파이크 테스트)
        get_products_spike_scenario: {
            executor: 'ramping-vus',
            startVUs: 200,
            stages: [
                { duration: '10s', target: 200 }, // 10초 동안 100 -> 200 VU 증가
                { duration: '20s', target: 200 }, // 20초 동안 200 VU 유지
                { duration: '10s', target: 0 },   // 10초 동안 200 -> 0 VU 감소
            ],
            exec: 'getProducts',
            startTime: '10s',
        },
    },
};

export function getProducts() {
    const url = 'http://host.docker.internal:8080/api/v1/products/selling';
    const params = {
        headers: {
            'Content-Type': 'application/json'
        }
    };
    const res = http.get(url, params);

    check(res, {
        'status is 200': (r) => r.status === 200
    });

    sleep(1);
}
```

### 3.2 부하 테스트 시나리오 설명
전체 흐름 요약 : 기본 트래픽을 지속적으로 유지하면서, 특정 시점에 급격한 트래픽 증가를 발생시키는 환경에사도 서버가 장애없이 동작하는지 확인합니다. 

1. 상품 목록 조회 (일반 트래픽)
- **목적:** 100명의 가상 유저가 30초 동안 보내는 지속적인 부하에서도 정상 응답(200)을 반환하는지 확인합니다.
- **시나리오 설정:**
   - **Executor:** `constant-vus`(고정된 사용자 수 유지)
   - **가상 사용자 수(VU):** 100명
   - **테스트 지속 시간:** 30초
   - **호출 API:** `/api/v1/products/selling`
   - **검증 항목:** 응답 상태 코드(200)

2. 상품 목록 조회 (스파이크 테스트)
- **목적:** 짧은 시간 동안 급격한 트래픽 증가 상황에서도 정상 응답(200)을 반환하는지 확인합니다.
- **시나리오 설정:**
   - **Executor:** `ramping-vus`(점진적 증가/감소)
   - **초기 VU:** 100명
   - **트래픽 증가 단계:**
      - 10초 동안 100명에서 200명으로 증가
      - 20초 동안 200명 VU 유지
      - 10초 동안 200명에서 0명으로 VU 감소
   - **호출 API:** `/api/v1/products/selling`
   - **시작 시간:** 10초 후
   - **검증 항목:** 응답 상태 코드(200)
---

4. 부하 테스트 목표 및 기대 결과
* 에러율 1% 이하 유지
* 최대 TPS 200에서도 서버가 안정적으로 응답 유지

