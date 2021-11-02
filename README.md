
# 토이프로젝트 계층형 게시판 구현


## 프로젝트 상세 기능
1. 회원
	- 회원가입
	- 로그인, 로그아웃(세션기반)
	- 내 정보 보기/수정(이름, 닉네임)
	- 내가 쓴 글 보기
	- 개인 정보 암호화, BCryptPasswordEncoder(bcrypt 해시알고리즘)를 사용
	- 회원탈퇴
	- 권한관리(관리자, 사용자)
2. 게시판
	 - 일반 게시판, 게시글 및 댓글 CRUD
		 - 게시글, 댓글 페이징 처리
		 - 권한을 가진 사용자만 게시판을 관리하는 기능(관리자 또는 내가 쓴 글만 수정/삭제 할 수 있어야 함)
		 - 글의 정보에는 게시글 번호, 제목, 내용, 글쓴이, 시간, 조회수가 필수적으로 들어감(추가 가능)
		 - 계증형 댓글 기능(대댓글 까지)
		 - 게시글 검색(제목, 내용, 작성자)
	- 이미지(jpg, png, gif) 업로드 기능 (선택)
	- 다양한 종류(공지사항, 익명, 갤러리)의 게시판 (선택)
	- 임시저장 기능(선택)
	- 댓글 알람(선택)
3. 구루미와 연동하여 방 생성 & 방 입장
	- API 문서 제공 예정

## 기술 스펙
- 필수 스펙 
	- Java 8+
	- SpringBoot 2.x, Spring Security, ORM(Hibernate, JPA 등)
	- Git
	- MySQL
	- Maven or Gradle
	- Log4j, Log4j2, Slf4j, Logback 등
	- IntelliJ(https://www.jetbrains.com/ko-kr/community/education/#students)
- 선택 스펙
	- Vue.js (시간이 부족하면 바닐라 또는 Jquery 무관)
	- Element-UI, BootStrap, Vuetify
	- Lombok
	- JSP, JSTL
	- MyBatis
	- 프론트엔드 빌드 툴 grunt, webpack etc..
	- 다양한 오픈소스 및 프레임워크

## 산출물
- ERD
- 요청 흐름 응답 다이어그램(브라우저 <-> 서버)
- 사용한 라이브러리 목록