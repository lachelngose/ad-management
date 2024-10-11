# ad-management

## Description
- 영상 삽입 광고를 관리하는 서비스입니다.
- 삽입 광고의 노출수, 조회수, 비용 등의 성과를 확인할 수 있습니다.
- 광고가 노출되는 영상에 대해 노출 여부를 지정할 수 있습니다.

## 지원 end point 목록
- [POST] /api/users/register
  사용자 등록

- [POST] /api/campaigns  
  캠페인 생성

- [GET] /api/campaigns  
  캠페인 목록 조회

- [GET] /api/campaigns/{id}  
  캠페인 조회

- [DELETE] /api/campaigns/{id}  
  캠페인 삭제

- [GET] /api/campaigns/{id}/impressions  
  광고 노출 조회

- [GET] /api/campaigns/{id}/impressions/{videoId}  
  특정 영상에 대한 광고 노출 성과 조회

- [GET] /api/campaigns/summary
  캠페인 성과 집계 조회

- [POST] /api/payments/info  
  결제 정보 생성

- [POST] /api/payments/submit  
  결제 제출

- [GET] /api/payments/history/{customerId}
  결제 내역 조회

## ERD
![ERD](/doc/erd.png)

## 실행 방법
1. intellij IDEA를 통해 프로젝트를 열고, Spring Boot Application으로 실행합니다.
2. 실행 시 환경 변수로 DB_NAME, DB_PASSWORD, DB_URL, DB_USERNAME을 설정합니다.
3. `http://localhost:8080/swagger-ui.html` 으로 접속합니다.
4. Swagger UI를 통해 API를 테스트합니다.