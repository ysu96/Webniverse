
# 웨비나 게시판 
![background4](https://user-images.githubusercontent.com/55823937/139771543-d98711a3-df65-4c97-ae66-ee448eaca110.jpg)


## 프로젝트 상세 기능
1. 회원
	- 회원가입
	- 로그인, 로그아웃(세션기반, Spring Security)
	- 내 정보 보기/수정(이름, 아이디, 비밀번호, 이메일)
	- 내가 쓴 글, 권한 보기
	- 개인 정보 암호화, BCryptPasswordEncoder를 사용
	- 회원탈퇴
	- 권한관리(관리자, 사용자)
	- 
2. 게시판
	 - 일반 게시판, 게시글 및 댓글 CRUD
		 - 게시글, 댓글, 검색 결과 페이징 처리
		 - 권한을 가진 사용자만 게시판을 관리하는 기능(관리자 또는 내가 쓴 글만 수정/삭제 할 수 있음)
		 - 글의 정보에는 게시글 번호, 제목, 내용, 글쓴이, 시간, 조회수 표시
		 - 계증형 댓글 기능(최대 깊이 4)
		 - 게시글 검색(제목, 내용, 작성자, 제목+내용)
	- 첨부파일 업로드, 다운로드 기능
	- 다양한 종류(공지사항, 익명, 갤러리)의 게시판
	- 임시저장 기능(예정)
	- 댓글 알람(예정)
	- 
3. 구루미와 연동하여 웨비나 기능을 추가
	- OpenAPI 연동흐름도, OpenAPI 연동규격서를 참고하여 연동
	- 회의 개설
	- OTP 발급
	- 회의 입장
	- 회의 종료
	- 입장 로그 보기, 다운로드
	- 참가자 권한 설정

## 기술 스펙
- SpringBoot 
	- Java 11
	- Jquery
	- Spring Security
	- Spring Data JPA
	- Lombok
	- JSP, JSTL
	- Maven
	- Bootstrap
	- Slf4j
	- IntelliJ
- Database
	- MySQL 8.0.26
	- MySQL workbench
	
- Git
	- Bitbucket
	- Jira
	- Sourcetree
	- Github
	 	
## 산출물
- ![캡처](https://user-images.githubusercontent.com/55823937/139770614-228b6e54-dfa9-49b0-8aac-c6498f5aa24a.PNG)

