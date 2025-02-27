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
            startVUs: 100,
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