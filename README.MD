#Monee Lab 프로젝트 과제

<h1>1. 개요</h1>

[요구사항]
* Facebook Feed처럼 글을 올리고 그 글에 댓글과 좋아요를 눌러 사용자간 소통이 가능한 소셜피드 서비스를 구현하고자 한다.
* 사용자 정보를 가지고 있는 사용자 API를 제공한다.
* 회원가입, 로그인이 가능한 인증 API를 제공한다.
* 소셜피드에 올라가는 글과 그 글에 연관된 댓글, 좋아요를 위한 적절한 API를 제공한다.

[제약사항]
* grade 또는 maven 기반 프로젝트로 구현
* POJO 형태로 구현
* api는 graphql로 통신
* Thread-Safety 준수 여부 및 '멀티 쓰레드 기반'의 테스트 코드 (데모 가능한 수준)
* README.md 파일에 문제해결 전략 및 프로젝트 빌드, 실행 방법 명시
* GitHub에 repository를 생성하고 소스 코드를 commit한 뒤 repository의 URL 제출
* 객체 라이프 사이클 관리 등을 위해 Spring 등의 서드파티 프레임워크 사용 불가
* 단, 데이터 관리를 위한 구조체, 알고리즘 또는 저장소에 대한 서드파티 라이브러리는 자유롭게 사용

<h2>개발 환경</h2>

통신/서버 구축 : HttpServer/HttpHandler <br/>
DB 구축 : MySQL </br>
DB Connection : 순수 JDBC <br/>
GraphQL : DataFetcher, graphqls schema <br/>
인증/인가 : Filter, JWT, BCrypt <br/>
Test : JUnit5, HttpURLConnection, Postman, Apache JMeter <br/>

<h2> 서비스 구현 </h2>
Account - 회원가입, 로그인, 비밀번호 암호화, 계정 리스트, 계정 정보 (인증처리) <br/>
Post - CRUD, 좋아요 (인증처리) <br/>
Reply - CRUD (인증처리) <br/>

<h3>ERD</h3>
![image](https://user-images.githubusercontent.com/66561524/111755024-fedc5500-88db-11eb-93d9-2da6f4f0a82f.png)
<a href="https://aquerytool.com:443/aquerymain/index/?rurl=206bcb5e-ea1a-4a77-9599-43dbb58a08b4">ERD 확인(읽기 전용)</a></br>
pw : 1jvwt8

<h3>MVC 패턴 구현</h3>

<h3>DB Connection</h3>


<h3>싱글톤 패턴</h3>

ObjectPool

<h3>GraphQL API 개발</h3>

엔드포인트

<h3>인증/인가</h3>

filter

jwt 라이브러리 사용

<h3>테스트 환경</h3>

HttpURLConnection,
PostMan


<table>
    <thead>
        <tr>
            <th>환경</th>
            <th>기술</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Server</td>
            <td>Tomcat 9.0</td>
        </tr>
        <tr>
            <td>DB</td>
            <td>Maria DB</td>
        </tr>
        <tr>
            <td>IDE</td>
            <td>Intellij Ultimate</td>
        </tr>
    </tbody>
</table>

<h1>2. 프로젝트 빌드</h1>

<h1>3. 문제해결 전략</h1>