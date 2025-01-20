INSERT INTO user (id, name, created_at, updated_at) VALUES
 (1, '이항해', NOW(), NOW()),
 (2, '김항해', NOW(), NOW()),
 (3, '박항해', NOW(), NOW()),
 (4, '최항해', NOW(), NOW()),
 (5, '서항해', NOW(), NOW());

insert into point (id, user_id, point, created_at, updated_at) values
(1, 1, 2500, NOW(), NOW()),
(2, 2, 0, NOW(), NOW()),
(3, 3, 50000, NOW(), NOW()),
(4, 4, 3500, NOW(), NOW()),
(5, 5, 30000, NOW(), NOW());

INSERT INTO product (id, name, selling_status, price, created_at, updated_at)VALUES
(1, '레몬 사탕', 'SELLING', 2500, NOW(), NOW()),
(2, '딸기 사탕', 'STOPPED', 2990, NOW(), NOW()),
(3, '청포도 젤리', 'SELLING', 3200, NOW(), NOW()),
(4, '콜라 젤리', 'SELLING', 2800, NOW(), NOW()),
(5, '오렌지 초콜릿', 'SELLING', 3500, NOW(), NOW()),
(6, '바나나 초콜릿', 'STOPPED', 4000, NOW(), NOW()),
(7, '블루베리 쿠키', 'SELLING', 4500, NOW(), NOW()),
(8, '초코칩 쿠키', 'SELLING', 3800, NOW(), NOW()),
(9, '카라멜 마카롱', 'SELLING', 5000, NOW(), NOW()),
(10, '코코넛 마카롱', 'STOPPED', 5200, NOW(), NOW());

INSERT INTO product_stock (id, product_id, quantity, created_at, updated_at) VALUES
(1, 1, 9999, NOW(), NOW()),
(2, 2, 2, NOW(), NOW()),
(3, 3, 500, NOW(), NOW()),
(4, 4, 1500, NOW(), NOW()),
(5, 5, 800, NOW(), NOW()),
(6, 6, 0, NOW(), NOW()),
(7, 7, 300, NOW(), NOW()),
(8, 8, 700, NOW(), NOW()),
(9, 9, 120, NOW(), NOW()),
(10, 10, 50, NOW(), NOW());

INSERT INTO coupon (id, name, discount_type, discount_amt, max_capacity, remain_capacity, valid_started_at, valid_ended_at, status, created_at, updated_at) VALUES
(1, '생일 쿠폰', 'FIXED_AMOUNT', 3000, 3, 3, '2025-01-01 00:00:00', '2025-01-31 23:59:59', 'ACTIVE', NOW(), NOW()),
(2, 'S 브랜드 할인 쿠폰', 'PERCENTAGE', 10, 3, 0, '2025-01-10 00:00:00', '2025-02-28 23:59:59', 'ACTIVE', NOW(), NOW()),
(3, '첫 구매 감사 쿠폰', 'FIXED_AMOUNT', 5000, 3, 3, '2025-01-05 00:00:00', '2025-03-31 23:59:59', 'ACTIVE', NOW(), NOW()),
(4, 'VIP 회원 한정 쿠폰', 'PERCENTAGE', 20, 3, 3, '2025-01-15 00:00:00', '2025-06-30 23:59:59', 'ACTIVE', NOW(), NOW()),
(5, '겨울 시즌 할인 쿠폰', 'PERCENTAGE', 15, 3, 3, '2025-01-01 00:00:00', '2025-02-28 23:59:59', 'ACTIVE', NOW(), NOW()),
(6, '특가 상품 추가 할인 쿠폰', 'FIXED_AMOUNT', 2000, 3, 3, '2025-01-10 00:00:00', '2025-04-30 23:59:59', 'DEACTIVATED', NOW(), NOW()),
(7, '이벤트 참여 감사 쿠폰', 'PERCENTAGE', 5, 3, 3, '2025-01-01 00:00:00', '2025-07-31 23:59:59', 'ACTIVE', NOW(), NOW()),
(8, '블랙프라이데이 쿠폰', 'PERCENTAGE', 30, 3, 3, '2025-01-05 00:00:00', '2025-01-10 00:00:01', 'ACTIVE', NOW(), NOW()),
(9, '한정판 출시 기념 쿠폰', 'FIXED_AMOUNT', 10000, 3, 3, '2025-01-12 00:00:00', '2025-03-15 23:59:59', 'ACTIVE', NOW(), NOW()),
(10, '멤버십 가입 축하 쿠폰', 'FIXED_AMOUNT', 7000, 3, 3, '2025-01-07 00:00:00', '2025-03-31 23:59:59', 'DEACTIVATED', NOW(), NOW());

INSERT INTO issued_coupon (id, coupon_id, user_id, coupon_name, discount_type, discount_amt, issued_at, valid_started_at, valid_ended_at, used_at, status, created_at, updated_at) VALUES
(1, 2, 1, 'S 브랜드 할인 쿠폰', 'PERCENTAGE', 10, '2025-01-10 00:00:00', '2025-01-10 00:00:00', '2025-02-28 23:59:59', null, 'UNUSED', NOW(), NOW()),
(2, 2, 2, 'S 브랜드 할인 쿠폰', 'PERCENTAGE', 10, '2025-01-10 00:00:00', '2025-01-10 00:00:00', '2025-02-28 23:59:59', null, 'UNUSED', NOW(), NOW()),
(3, 2, 4, 'S 브랜드 할인 쿠폰', 'PERCENTAGE', 10, '2025-01-10 00:00:00', '2025-01-10 00:00:00', '2025-02-28 23:59:59', '2025-01-17 10:00:00', 'USED', NOW(), NOW()),
(4, 8, 1, '블랙프라이데이 쿠폰', 'PERCENTAGE', 30, '2025-01-10 00:00:00', '2025-01-05 00:00:00', '2025-01-10 00:00:01', null, 'UNUSED', NOW(), NOW());


