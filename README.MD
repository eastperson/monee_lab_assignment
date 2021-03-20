# Monee Lab 프로젝트 과제

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

* 통신/서버 구축 : HttpServer, HttpHandler <br/>
* DB 구축 : MySQL </br>
* DB Connection : 순수 JDBC <br/>
* GraphQL : DataFetcher, graphqls schema <br/>
* 인증/인가 : Filter, JWT, BCrypt <br/>
* Test : JUnit5, HttpURLConnection, Postman, Apache JMeter <br/>

<h2> 서비스 구현 </h2>
Account - 회원가입, 로그인, 비밀번호 암호화, 계정 리스트, 계정 정보 (인증처리) <br/>
Post - CRUD, 좋아요, 댓글 합계 (인증처리) <br/>
Reply - CRUD (인증처리) <br/>

<h3>ERD</h3>
![image](https://user-images.githubusercontent.com/66561524/111755024-fedc5500-88db-11eb-93d9-2da6f4f0a82f.png)
<a href="https://aquerytool.com:443/aquerymain/index/?rurl=206bcb5e-ea1a-4a77-9599-43dbb58a08b4">ERD 확인(읽기 전용)</a></br>
pw : 1jvwt8

<h3>MVC 패턴 구현</h3>
<p>
Spring과 같이 MVC 패턴을 구현하기 위한 제작을 하였다. Spring Bean Container와 유사한 역할을 하는 ObjectPool을 만들었고, 컨트롤러의 경우는 코드의 복잡도가 높아져
서버의 요청을 처리해주는 로직과 파라미터를 바인딩 받아 로직을 처리하는 로직을 어노테이션을 제작하여 분리하였다. 클라이언트 요청의 흐름은 다음과 같이 구현하였다.
</p>
<p>HtppServer를 통해 Client의 Request를 받는다. 요청의 Header와 RequestBody, URI를 읽고 Controller Pool에 있는 Controller Method와 맵핑을 한다. Controller는 
어노테이션으로 등록된 컨트롤러와 메서드에 각 URI 마다 맵핑한다. 컨트롤러는 받은 요청의 파라미터를 바인딩해주고 Service가 처리된 로직의 결과값을 반환한다.
Service는 로직처리를 해주며 로직처리가 된 값은 DAO를 통해 DB와 Connect한다. </p>
<p>
Controller를 제외한 모든 객체는 서버가 시작될 때 ObjectPool에서 생성이 되며 싱글톤으로 구현이 되어있어, 로직을 사용할 때마다 서버에서 꺼내어 객체를 사용한다.

</p>

![image](https://user-images.githubusercontent.com/66561524/111868890-e8a7c500-89bf-11eb-8e6f-dc00a7cbb439.png)

<h3>DB Connection</h3>

<p>
순수 JDBC를 사용하여 DB Connection을 생성한다. 쿼리는 용도에 따라 정적으로 구현을 하였다. 커넥션을 사용하는 로직은 동기화 처리가 되어있어 멀티 쓰레드 환경에서 생기는
오류를 방지하였다. 
</p>

<h3>GraphQL API 개발 </h3>

<p>
GraphQL은 단일한 엔드 포인트에 쿼리에 따라 적절한 결과값을 반환해주는 로직을 처리한다. graphqls로 작성한 Schema에 쿼리와 기대 결과값을 작성한다. ServiceGraphQLProvider는 
schema에서 작성한 쿼리와 이를 처리하는 DataFetcher를 작성해준다. Query와 Mutation은 로직을 구분하여 작성한다. DataFetcher는
schema의 쿼리/뮤테이션마다 하나씩 작성하며 해당하는 데이터를 가져오는 로직을 구현한다.
</p>

<p>
응답은 success, status code, object를 필드로 갖는 ResultApi 객체를 만들어서 반환한다. 응답 내용을 쉽게 Client에서 확인할 수 있으며 응답 객체에 따라 유연하게
활용할 수 있도록  구성하였다. API의 엔드포인트에 따른 필요 Parameter와 기대 응답은 최하단에 기재하였다.
</p>

<h3>인증/인가</h3>
<p>
API 로직에 적합한 Jwt를 사용하여 로그인처리를 구현하였다. 로그인 처리가 완료되면 access_token이 발급된다. JWT 검증은 Controller 맵핑을 하기 전인 Filter를 통해 구현하였다.
로그인과 회원가입을 제외한 요청들은 Request Header에 있는 Authorization을 확인하여 JWT 클레임에 있는 회원정보와 토큰 만료기간을 확인한다. 토큰이 만료가 되어있으면
405 Error를 Response하여 로그인을 재요청한다. JWT 토큰의 회원정보와 Client Key가 DB와 일치하지 않으면 역시 405 에러가 발생한다.
로그아웃은 해당하는 토큰을 제거하는 로직을 구현해야 하기 때문에 토큰을 저장하고 삭제할 수 있는 Client의 역할로 설계하였다.
</p>

<h3>테스트 환경</h3>
<p>
GraphQL로 제작한 Api Controller를 테스트하기 위해 Postman과 HttpURLConnection을 사용했다. 서버를 구동시켜 Postman으로 
쿼리를 테스트하였다. 또한 Application을 Thread로 생성하여 HttpURLConnection으로 JUnit 테스트 환경에서 요청과 응답을 확인하였다.
</p>

<p>

</p>

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

<h2>계정 API</h2>
<h3>▶️ Request</h3>

```
POST /account HTTP/1.1
Host: localhost:8080
Authorization: Bearer {ACCESS_TOKEN}
```

<h3>Query</h3>
<table>
	<thead>
		<tr>
			<th>Query Name</th>
            <th>Parameter Name</th>
			<th>Parameter</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td rowspan="2">Account</td>
			<td><code>seq</code></td>
			<td><code>Long</code></td>
			<td>검색할 Account의 시퀀스</td>
            <td>O</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query" : "{Account(seq:1){success,status,data{seq,email,nickname}}}"}</code></td>
        </tr>
		<tr>
			<td rowspan="3">allAccounts</td>
			<td><code>title</code></td>
			<td><code>String</code></td>
			<td>검색할 Account의 이메일</td>
            <td>X</td>
		</tr>
		<tr>
			<td><code>nickname</code></td>
			<td><code>String</code></td>
			<td>검색할 Account의 닉네임</td>
            <td>X</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query" : "{allAccounts{success,status,data{seq,email,nickname}}}"}</code></td>
        </tr>
	</tbody>
</table>
<h3>Mutation</h3>
<table>
	<thead>
		<tr>
			<th>Query Name</th>
            <th>Parameter Name</th>
			<th>Parameter</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td rowspan="4">createAccount</td>
			<td><code>email</code></td>
			<td><code>String</code></td>
			<td>회원가입 할 이메일</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>nickname</code></td>
			<td><code>String</code></td>
			<td>회원가입 할 닉네임</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>password</code></td>
			<td><code>String</code></td>
			<td>회원가입 할 비밀번호</td>
            <td>O</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query": "mutation{createAccount(email:\"email@google.com\",nickname:\"eastperson\",password:\"123123\"){success,status,data{seq,email,nickname,password}}}"}</code></td>
        </tr>
		<tr>
			<td rowspan="4">updateAccount</td>
			<td><code>seq</code></td>
			<td><code>Long</code></td>
			<td>수정할 회원의 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>nickname</code></td>
			<td><code>String</code></td>
			<td>수정할 Account의 닉네임</td>
            <td>X</td>
		</tr>
		<tr>
			<td><code>password</code></td>
			<td><code>String</code></td>
			<td>수정할 Account의 패스워드</td>
            <td>X</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query": "mutation {updateAccount(seq:1,nickname:\"eastperson2\"){success,status,data{seq,nickname,password}}}"}</code></td>
        </tr>
	</tbody>
</table>

<h3>▶️ Response</h3>

**Response**
<table>
	<thead>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>success</td>
			<td><code>boolean</code></td>
			<td>요청이 성공을 했는지</td>
			<td>O</td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td>status</td>
			<td><code>int</code></td>
			<td>응답 상태 코드</td>
			<td>O</td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td>data</td>
			<td><code>Account</code></td>
			<td>계정 정보</td>
			<td></td>
		</tr>
	</tbody>
</table>


<h3>⏺️ Sample</h3>

**Request**

```json
curl -X POST "http://localhost:8080/account" 
     -d '{"query" : "{allAccounts{success,status,data{seq,email,nickname}}}"}'
     -H "Authorization: Bearer {ACCESS_TOKEN}"
```

**Response**

```json
{
  Account={
      success=true,
      status=200,
      data={
              seq=1,
              email=5980f6a9-908b-42e6-8@email.com,
              nickname=eastperson2
      }
   }
}
```

<h2>게시글 API</h2>
<h3>▶️ Request</h3>

```
POST /post HTTP/1.1
Host: localhost:8080
Authorization: Bearer {ACCESS_TOKEN}
```

<h3>Query</h3>
<table>
	<thead>
		<tr>
			<th>Query Name</th>
            <th>Parameter Name</th>
			<th>Parameter</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td rowspan="2">Post</td>
			<td><code>seq</code></td>
			<td><code>Long</code></td>
			<td>검색할 Post의 시퀀스</td>
            <td>O</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query": "{Post(seq:1){success,status,data{seq,title,content,revwCnt}}}"}</code></td>
        </tr>
		<tr>
			<td rowspan="3">allPosts</td>
			<td><code>title</code></td>
			<td><code>String</code></td>
			<td>검색할 Post의 제목</td>
            <td>X</td>
		</tr>
		<tr>
			<td><code>content</code></td>
			<td><code>String</code></td>
			<td>검색할 Post의 콘텐츠</td>
            <td>X</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query": "{allPosts{success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}"}</code></td>
        </tr>
	</tbody>
</table>
<h3>Mutation</h3>
<table>
	<thead>
		<tr>
			<th>Query Name</th>
            <th>Parameter Name</th>
			<th>Parameter</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td rowspan="4">createPost</td>
			<td><code>title</code></td>
			<td><code>String</code></td>
			<td>게시글 제목</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>content</code></td>
			<td><code>String</code></td>
			<td>게시글 내용</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>author_Seq</code></td>
			<td><code>String</code></td>
			<td>계정 시퀀스</td>
            <td>O</td>
		</tr>
        <tr>
        <td colspan="5"><code>{"query": "mutation {createPost(title:\"title\",content:\"content\",author_seq : \"1\"){success,status,data{seq,title,content,revwCnt}}}"}</code></td>
        </tr>
		<tr>
			<td rowspan="4">updatePost</td>
			<td><code>seq</code></td>
			<td><code>Long</code></td>
			<td>수정할 게시글의 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>title</code></td>
			<td><code>String</code></td>
			<td>수정할 Post 제목</td>
            <td>X</td>
		</tr>
		<tr>
			<td><code>content</code></td>
			<td><code>String</code></td>
			<td>수정할 Post 내용</td>
            <td>X</td>
		</tr>
        <tr>
            <td colspan="5"><code>{"query": "mutation {updatePost(seq:1,title:\"제목 수정\"){success,status,data{seq,title,content}}}"}</code></td>
        </tr>
		<tr>
            <td>deletePost</td>
			<td>seq</td>
			<td><code>Long</code></td>
			<td>삭제할 Post 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
            <td rowspan="4">addLikePost</td>
			<td>account_seq</td>
			<td><code>Long</code></td>
			<td>좋아요 할 계정의 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td>post_seq</td>
			<td><code>Long</code></td>
			<td>좋아요 할 게시글 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td>isAdd</td>
			<td><code>Boolean</code></td>
			<td>좋아요 여부</td>
            <td>X</td>
		</tr>
        <tr>
            <td colspan="5"><code>{"query": "mutation {addLikePost(account_seq:\"1\",post_seq:\"1\",isAdd:true){success,status}}"}</code></td>
        </tr>
	</tbody>
</table>

<h3>▶️ Response</h3>

**Response**
<table>
	<thead>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>success</td>
			<td><code>boolean</code></td>
			<td>요청이 성공을 했는지</td>
			<td>O</td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td>status</td>
			<td><code>int</code></td>
			<td>응답 상태 코드</td>
			<td>O</td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td>data</td>
			<td><code>Post</code></td>
			<td>계정 정보</td>
			<td>X</td>
		</tr>
	</tbody>
</table>


<h3>⏺️ Sample</h3>

**Request**

```json
curl -X POST "http://localhost:8080/post" 
     -d '{"query": "{Post(seq:1){success,status,data{seq,title,content,revwCnt}}}"}'
     -H "Authorization: Bearer {ACCESS_TOKEN}"
```

**Response**

```json
{
  Post={
      success=true, 
      status=200, 
      data={
          seq=1, 
          title=제목, 
          content=내용, 
          revwCnt=41
        }
    }
}
```

<h2>댓글 API</h2>
<h3>▶️ Request</h3>

```
POST /reply HTTP/1.1
Host: localhost:8080
Authorization: Bearer {ACCESS_TOKEN}
```

<h3>Query</h3>
<table>
	<thead>
		<tr>
			<th>Query Name</th>
            <th>Parameter Name</th>
			<th>Parameter</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td rowspan="2">Reply</td>
			<td><code>seq</code></td>
			<td><code>Long</code></td>
			<td>검색할 댓글의 시퀀스</td>
            <td>O</td>
		</tr>
        <tr>
            <td colspan="5"><code>{"query": "{Reply(seq:1){success,status,data{seq,content,author{seq,email,nickname,password}}}}"}</code></td>
        </tr>
		<tr>
			<td rowspan="2">allReplys</td>
			<td><code>post_seq</code></td>
			<td><code>String</code></td>
			<td>검색할 Reply List의 시퀀스</td>
            <td>O</td>
		</tr>
        <tr>
            <td colspan="5"><code>{"query": "{allPosts{success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}"}</code></td>
        </tr>
	</tbody>
</table>
<h3>Mutation</h3>
<table>
	<thead>
		<tr>
			<th>Query Name</th>
            <th>Parameter Name</th>
			<th>Parameter</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td rowspan="4">createReply</td>
			<td><code>author_seq</code></td>
			<td><code>String</code></td>
			<td>댓글 작성 계정 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>post_seq</code></td>
			<td><code>String</code></td>
			<td>게시글 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>content</code></td>
			<td><code>String</code></td>
			<td>댓글 내용</td>
            <td>O</td>
		</tr>
        <tr>
            <td colspan="5"><code>{"query": "mutation{createReply(author_seq:\"1\",post_seq:\"1\",content:\"댓글 내용\"){success,status,data{seq,content,author{seq,email,nickname}}}}"}</code></td>
        </tr>
		<tr>
			<td rowspan="4">createReply</td>
			<td><code>author_seq</code></td>
			<td><code>String</code></td>
			<td>댓글 작성 계정 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>post_seq</code></td>
			<td><code>String</code></td>
			<td>게시글 시퀀스</td>
            <td>O</td>
		</tr>
		<tr>
			<td><code>content</code></td>
			<td><code>String</code></td>
			<td>수정할 댓글 내용</td>
            <td>O</td>
		</tr>
        <tr>
            <td colspan="5"><code>{"query": "mutation{updateReply(seq:1,content:\"댓글 수정\"){success,status,data{seq,content,author{seq,email,nickname}}}}"}</code></td>
        </tr>
		<tr>
            <td rowspan="2">deleteReply</td>
			<td>seq</td>
			<td><code>Long</code></td>
			<td>삭제할 Reply 시퀀스</td>
            <td>O</td>
		</tr>
        <tr>
            <td colspan="4"><code>{\"query\": \"mutation{deleteReply(seq:"+reply.getSeq()+"){success,status,data{seq,content,author{seq,email,nickname}}}}\"}</code></td>
        </tr>
	</tbody>
</table>

<h3>▶️ Response</h3>

**Response**
<table>
	<thead>
		<tr>
			<th>Name</th>
			<th>Type</th>
			<th>Description</th>
			<th>Required</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>success</td>
			<td><code>boolean</code></td>
			<td>요청이 성공을 했는지</td>
			<td>O</td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td>status</td>
			<td><code>int</code></td>
			<td>응답 상태 코드</td>
			<td>O</td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td>data</td>
			<td><code>Reply</code></td>
			<td>계정 정보</td>
			<td>X</td>
		</tr>
	</tbody>
</table>


<h3>⏺️ Sample</h3>

**Request**

```json
curl -X POST "http://localhost:8080/reply" 
     -d '{"query": "{Reply(seq:1){success,status,data{seq,content,author{seq,email,nickname,password}}}}"}'
     -H "Authorization: Bearer {ACCESS_TOKEN}"
```

**Response**

```json
{
  Reply={
          success=true, 
          status=200, 
          data={
                seq=1, 
                content=댓글 내용 
                author={
                        seq=1, 
                        email=5980f6a9-908b-42e6-8@email.com, 
                        nickname=eastperson2
                        }
                }
          }
}
```