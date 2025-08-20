# KUIT_KAC

## Git Commit Convention

Git 커밋 메시지를 작성할 때 다음 컨벤션을 따르는 것을 권장합니다.  
각 커밋 메시지는 **타입(Type)** 과 **설명(Description)** 으로 구성됩니다.  

```
<Type>: <설명>
```

### Type (타입)

|Type (타입)|설명|
|---|---|
|**Feat**|새로운 기능 추가|
|**Fix**|버그 수정 또는 오타(typo) 수정|
|**Refactor**|코드 리팩토링|
|**Design**|CSS 등 사용자 UI 디자인 변경|
|**Comment**|필요한 주석 추가 및 변경|
|**Style**|코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우|
|**Test**|테스트 코드 추가, 수정, 삭제 (비즈니스 로직에 변경이 없는 경우)|
|**Chore**|위에 해당하지 않는 기타 변경사항 (빌드 스크립트 수정, assets image, 패키지 매니저 등)|
|**Init**|프로젝트 초기 생성|
|**Rename**|파일 또는 폴더명 수정 및 이동|
|**Remove**|파일 삭제 작업만 수행하는 경우|

### Description (설명)

- 설명은 변경 사항에 대한 간결하고 명확한 요약을 제공합니다.
    
- 명령문으로 작성하고, 끝에 마침표를 찍지 않습니다.
    
- 가능하면 50자 이내로 작성합니다.



# 🍎 냠코치 : 다이어트 코칭 Native APP

> 데모데이 서버 제출 자료 (8/20 23:59까지 제출 필수)  
> 📌 포함 항목: **ERD**, **API 명세서**, **아키텍처 다이어그램**

---

## 1️⃣ 기술 스택 개요
- **Backend**: Spring Boot (JPA/Hibernate), JWT 인증/인가
- **Infra**: AWS EC2 + RDS, Docker, Docker Compose
- **DB**: MySQL 8.0.41
- **CI/CD**: GitHub, GitHub Actions
- **Client**: Android (Compose UI), Retrofit + OkHttp, DataStore 토큰 저장

---

## 2️⃣ 클라이언트 ↔ 서버 통신
- **네트워크**: Retrofit / OkHttp
- **JWT 저장 위치**: Android DataStore (access / refresh)
- **헤더 처리**: OkHttp Interceptor → `Authorization: Bearer {access}` 자동 첨부

---

## 3️⃣ 인증/인가 흐름 (요약)
1. 로그인 성공 시 **Access / Refresh JWT 발급**
2. 토큰 만료 시 → `401/403` 응답
3. 클라이언트가 **Refresh 토큰**으로 재발급 후 재요청

---

## 4️⃣ 기능별 API & DB 매핑

### 🏠 홈화면
- **클라이언트**: `GET /home/summary`
- **서버**:
  - 사용자 BMR/목표/최근 체중 조회 및 계산
  - 오늘 섭취/남은 칼로리, 운동 요약 집계
  - 외식/공복/술자리 패턴 리포트
- **DB(READ)**: `user`, `user_information`, `weight`, `diet(+diet_food/aifood)`, `routine(+exercise, set)`

---

### 🍚 식단 기록
- **생성**: `POST /diets/records`
  - → `diet` 생성 + `diet_food`/`diet_aifood` 저장
- **조회**: `GET /diets/records/profiles`
  - → 하루 식단 묶음 조회 (join)
- **비고**: 공복/요약 계산 시 `diet_entry_type`, `diet_time` 활용

---

### 🤖 AI 식단 추천
- **예측**: `POST /ai/diets`
  - 입력: 날짜/끼니/활동(외식/술자리)
  - DB(READ): 기존 식단 + 음식
  - 출력: 날짜별 추천 식단(음식명, 영양소, 점수)
- **생성**: `POST /ai/diets/create`
  - 입력: 예측 결과 중 선택한 계획(`plans`)
  - DB(WRITE): `diet(diet_entry_type=AI_PLAN)` + `diet_food` / `diet_aifood`
  - 출력: 실제 기록된 식단 정보(총칼로리 포함)

---

### 📑 나만의 식단 (템플릿)
- **저장**: `POST /diet-templates` → `diet(diet_type=TEMPLATE)`
- **적용**: `POST /diet-templates/{id}/apply` → TEMPLATE 복사 → RECORD 생성
- **DB**: `diet` + `diet_food`/`diet_aifood` (템플릿/실사용 구분)

---

### 🏋️ 운동 기록
- **생성**: `POST /routines/records`
  - → `routine(RECORD)` + `routine_exercise` + `routine_detail` + `routine_set` 저장
- **조회**: `GET /routines/template/profiles` → 루틴/세트 상세 반환
- **비고**: 칼로리 소모량 = `exercise.met_value` × 세트 데이터

---

### 📝 나만의 운동 (루틴)
- **저장**: `POST /routines/templates` → `routine(TEMPLATE)` + 하위 엔티티
- **적용**: TEMPLATE 복사 → `routine(RECORD)` 운동 기록 생성
- **DB**: 동일 구조(`routine`/`exercise`/`set`), 타입만 다름

---

### 👤 온보딩
- **생성**: `POST /onboarding` (gender, age, height, targetWeight, appetiteType, eatingOutCount, dietVelocity 등)
  - → `user_information`, `weight` 저장
- **상태 조회**: `GET /onboarding/status` → `{ completed: true|false }`

---

## 5️⃣ 포트 & 배포
- **App**: Spring Boot → 8080
- **DB**: MySQL → 3306
- **배포**: Docker Compose 기반 (App + DB)

---

## 📌 제출 자료
- **ERD**: <img width="931" height="488" alt="ERD diagram cut" src="https://github.com/user-attachments/assets/aa164ec6-1a9e-47d6-a52e-e20d047d49ce" />
- **API 명세서**: (http://15.165.158.249:8080/swagger-ui/index.html#/%EC%8B%9D%EB%8B%A8%20%EA%B4%80%EB%A6%AC/createRecordDiet)
- **아키텍처 다이어그램**: 
<img width="2792" height="1914" alt="wmremove-transformed (3)" src="https://github.com/user-attachments/assets/c6dbad3f-67dd-412c-929b-e43faab21499" />


