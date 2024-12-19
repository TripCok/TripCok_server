# TripCok_Server

TripCokServer는 여행지, 모임, 게시글, 댓글 등 여행 플랫폼에서 필요한 핵심 기능을 제공하는 Spring Boot 기반의 서버입니다. 이 서버는 사용자들에게 최적의 여행 경험을 제공하기 위해 회원 관리, 모임 생성 및 관리, 여행지 정보 조회, 게시글 및 댓글 작성과 같은 다양한 기능을 지원합니다.

또한, TripCokServer는 서버에서 발생하는 사용자 활동 및 시스템 로그를 효과적으로 수집하기 위해 Apache Kafka를 활용합니다. 사용자의 행동 데이터를 실시간으로 Kafka로 전송하여 중앙 집중형 로그 시스템을 구축하고, 이를 통해 유의미한 데이터의 집계와 분석이 가능하도록 설계되었습니다. 이러한 설계는 데이터 기반 의사결정과 서비스 개선을 지원하며, 로그 데이터를 활용해 사용자 행동 분석, 트렌드 파악, 서비스 최적화를 수행할 수 있습니다.

TripCokServer는 확장성과 안정성을 고려하여 설계되었으며, 향후 추가 기능 구현과 데이터 분석을 위한 탄탄한 기반을 제공합니다.

![napkin-selection-2](https://github.com/user-attachments/assets/3510c137-7c25-4c0f-a48a-d961c8f8cd56)

## 요구사항
- Java 17이상
- Spring boot

## 핵심 설정
- .env 파일
  ```yml
  AWS_ACCESS_KEY_ID={AWS_KEY_ID}
  AWS_SECRET_ACCESS_KEY={AWS_SECRET_KEY}
  
  DB_HOST_IP={DATABASE_HOST}
  DB_HOST_PORT={DATABASE_PORT}
  DB_HOST_DATABASE={DATABASE_NAME}
  DB_HOST_USERNAME={DATABASE_USER_NAME}
  DB_HOST_PASSWORD={DATABASE_PASSWORD}
  
  MAIL_USER_NAME={NAVER_EMAIL}
  MAIL_USER_PASSWORD={NAVER_PASSWORD}
  
  KAFKA_BOOTSTRAP_SERVERS={KAFAKA_BOOTSTRAP_SERVER}
  ```

## 핵심 기능

### 집계를 위한 Logging System
- 추후 기입

## API 명세

<details><summary>사용자</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| M-01 | 회원가입 | POST | `/api/v1/member/register` |
| M-02 | 회원가입 이메일 인증 요청 | GET | `/api/v1/member/register/{email}` |
| M-03 | 이메일 인증번호 검증 | GET | `/api/v1/member/register/email/check` |
| M-04 | 로그인 | PUT | `/api/v1/member/login` |
| M-05 | 비동기 로그인 | PUT | `/api/v1/member/login/async` |
| M-06 | 선호 카테고리 저장 | PUT | `/api/v1/member/prefer/category` |
| M-07 | 선호 카테고리 선택 건너뛰기 | PUT | `/api/v1/member/prefer/category/skip` |
| M-08 | 특정 회원 정보 조회 | POST | `/api/v1/member/find/{memberId}` |
| M-09 | 회원 목록 조회 | GET | `/api/v1/member/finds` |
| M-10 | 회원 정보 수정 | PUT | `/api/v1/member/{memberId}` |
| M-11 | 회원 프로필 이미지 수정 | PUT | `/api/v1/member/{memberId}/profile-image` |
| M-12 | 회원 프로필 이름 수정 | PUT | `/api/v1/member/{memberId}/profile-name` |
| M-13 | 회원 삭제 | DELETE | `/api/v1/member/{memberId}` |
</details>

<details><summary>모임</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| G-01 | 모임 생성 | POST | `/api/v1/group` |
| G-02 | 단일 모임 조회 | GET | `/api/v1/group/{id}` |
| G-03 | 모임 목록 조회 | GET | `/api/v1/group/all` |
| G-04 | 내가 가입된 모임 조회 | GET | `/api/v1/group/my` |
| G-05 | 모임 카테고리 추가 | PUT | `/api/v1/group/category/{id}` |
| G-06 | 모임 카테고리 삭제 | DELETE | `/api/v1/group/category/{id}` |
| G-07 | 모임 수정 | PUT | `/api/v1/group/{id}` |
| G-08 | 모임 삭제 | DELETE | `/api/v1/group/{id}` |
| G-09 | 모임 구인 상태 변경 | PUT | `/api/v1/group/{groupId}/recruiting` |
| G-10 | 모임 초대 | POST | `/api/v1/group/invite` |
| G-11 | 모임 초대 수락 | POST | `/api/v1/group/accept-invite` |
</details>

<details><summary>여행지</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| PLC-01 | 여행지 생성 | POST | `/api/v1/place` |
| PLC-02 | 여행지 상세 조회 | GET | `/api/v1/place/{placeId}` |
| PLC-03 | 모든 여행지 조회 | GET | `/api/v1/place` |
| PLC-04 | 여행지 수정 | PUT | `/api/v1/place/{placeId}` |
| PLC-05 | 여행지 이미지 삭제 | DELETE | `/api/v1/place/images` |
| PLC-06 | 여행지 삭제 | DELETE | `/api/v1/place/{placeId}` |
| PLC-07 | 좌표 내 여행지 찾기 | GET | `/api/v1/place/placeInRegion` |
</details>

<details><summary>모임 여행지</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| GP-01 | 모임에 여행지 추가 | POST | `/api/v1/group/place` |
| GP-02 | 모임의 여행지 조회 (모두) | GET | `/api/v1/group/place/{groupId}/all` |
| GP-03 | 모임에 여행지 삭제 | DELETE | `/api/v1/group/place` |
| GP-04 | 모임에 여행지 순서 변경 | PUT | `/api/v1/group/place/orders` |
</details>

<details><summary>파일</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| FU-01 | 파일 업로드 | POST | `/api/files/upload` |
| F-01 | 이미지 파일 반환 | GET | /api/v1/file |
</details>

<details><summary>신청</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| A-01 | 모임 신청 | POST | `/api/v1/application` |
| A-02 | 모임 신청 조회 | GET | `/api/v1/application/group/{groupId}` |
| A-03 | 모임 신청 취소 | DELETE | `/api/v1/application/{applicationId}` |
| A-04 | 모임 신청 수락 | PUT | `/api/v1/application` |
</details>

<details><summary>카테고리</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| PC-01 | 여행지 카테고리 생성 | POST | `/api/v1/place/category` |
| PC-02 | 모든 여행지 카테고리 조회 | GET | `/api/v1/place/category/all` |
| PC-03 | 특정 여행지 카테고리 조회 | GET | `/api/v1/place/category` |
</details>

<details><summary>게시글</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| P-01 | 게시글 조회 (단일) | GET | `/api/v1/post/{postId}` |
| P-02 | 게시글 조회 (복수) | GET | `/api/v1/posts` |
| P-03 | 게시글 삭제 | DELETE | `/api/v1/post/{postId}` |
| P-04 | 게시글 수정 | PUT | `/api/v1/post/{postId}` |
| P-05 | 모임 게시글 작성 | POST | `/api/v1/group/post` |
| P-06 | 모임 공지사항 작성 | POST | `/api/v1/group/notice` |
</details>

<details><summary>게시글 댓글</summary>
  
| **기능 번호** | **기능명** | **HTTP 메서드** | **엔드포인트** |
| --- | --- | --- | --- |
| PCMT-01 | 모임 게시글 댓글 작성 | POST | `/api/v1/postComment` |
| PCMT-02 | 댓글 조회 (단일) | GET | `/api/v1/postComment/{postCommentId}` |
| PCMT-03 | 댓글 조회 (복수) | GET | `/api/v1/postComments` |
| PCMT-04 | 댓글 삭제 | DELETE | `/api/v1/postComment` |
| PCMT-05 | 댓글 수정 | PUT | `/api/v1/postComment/{postCommentId}` |
