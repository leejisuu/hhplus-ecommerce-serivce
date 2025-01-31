# 이커머스 서비스 구축 과제

## 프로젝트 설계 문서 자료

-----

* [Milestone](https://github.com/leejisuu/hhplus-ecommerce-serivce/milestone/1) 작성
* 시나리오 요구사항 별 분석 자료 작성
  * [플로우 차트](https://chip-parmesan-fbe.notion.site/16f0183f52a7808fa4f3de7c8ccd4859?pvs=4) 작성
  * [시퀀스 다이어그램](docs/SequenceDiagram.md) 작성
* [ERD](docs/ERD.md) 작성
* [API 명세서](docs/ApiDocs.md) 작성 (Swagger-ui)
* [Chapter2 회고](docs/review.md)
* [동시성 제어 방식 보고서](https://velog.io/@jsks0826/%ED%95%AD%ED%95%B4-%ED%94%8C%EB%9F%AC%EC%8A%A4%EC%9D%B4%EC%BB%A4%EB%A8%B8%EC%8A%A4-%EC%8B%9C%EB%82%98%EB%A6%AC%EC%98%A4-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4-%EB%B0%A9%EC%8B%9D-%EB%B9%84%EA%B5%90)
---

## Getting Started
### Prerequisites
#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d