package com.monee.controller;

import com.google.gson.Gson;
import com.monee.CoreApplication;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostControllerTest {

    private static final int PORT = 8080;
    private static ClientAndServer mockServer;
    private static Logger log = LoggerFactory.getLogger(PostControllerTest.class);
    // 21년 4월 19일 만료
    private final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJpc3N1ZXIiLCJuaWNrbmFtZSI6ImVhc3RwZXJzb24iLCJleHAiOjE2MTg3MTEyMzEsImlhdCI6MTYxNjExOTIzMSwidXNlcktleSI6MSwiZW1haWwiOiJranVpb3FxcUBuYXZlci5jb20ifQ.PHm_En1LGzBgrcWGAdiKvvh28qxYli5ROx2pNxyZCvyEZ5OBHHwNS-XaVqxsbFyJ5CR1ePQ-mh2uX6iFZyLapQ";

    @BeforeAll
    static void setUp() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        Runnable coreApplication = new CoreApplication();
        Thread serverThread = new Thread(coreApplication);
        serverThread.start();
        log.info("server thread start");
    }

    @DisplayName("GraphQL - 포스트 목록 - 성공")
    @Test
    void graphql_find_all_correct() throws IOException {

        String query = "{\"query\": \"{allPosts{success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - 포스트 찾기 by id - 성공")
    @Test
    void graphql_find_by_id_correct() throws IOException {

        String query = "{\"query\": \"{Post(seq:1){success,status,data{seq,title,content,revwCnt}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - 포스트 찾기 by id with account - 성공")
    @Test
    void graphql_findByIdWithAccount_correct() throws IOException {

        String query = "{\"query\": \"{Post(seq:1){success,status,data{seq,title,content,revwCnt,author{seq,nickname,email}}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - 포스트 찾기 by id with account - 성공")
    @Test
    void graphql_findByIdWithReplyList_correct() throws IOException {

        String query = "{\"query\": \"{Post(seq:1){success,status,data{seq,title,content,revwCnt,author{nickname},replyList{seq,content,author{seq,nickname,email}}}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - create post - 성공")
    @Test
    void graphql_create_post_correct() throws IOException {

        String query = "{\"query\": \"mutation {createPost(title:\\\"title\\\",content:\\\"content\\\",author_seq : \\\"1\\\"){success,status,data{seq,title,content,revwCnt}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - update post title - 성공")
    @Test
    void graphql_updatePostTitle_correct() throws IOException {

        String query = "{\"query\": \"mutation {updatePost(seq:1,title:\\\"제목 수정\\\"){success,status,data{seq,title,content}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - update post content - 성공")
    @Test
    void graphql_updatePostContent_correct() throws IOException {

        String query = "{\"query\": \"mutation {updatePost(seq:1,content:\\\"내용 수정\\\"){success,status,data{seq,title,content}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - delete post - 성공")
    @Test
    void graphql_delete_post_correct() throws IOException {

        String query = "{\"query\": \"mutation {deletePost(seq:17){success,status,data{seq,title,content,revwCnt}}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - like add post - 성공")
    @Test
    void graphql_add_like_correct() throws IOException {

        String query = "{\"query\": \"mutation {addLikePost(account_seq:\\\"1\\\",post_seq:\\\"1\\\",isAdd:true){success,status}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - like remove post - 성공")
    @Test
    void graphql_remove_like_correct() throws IOException {

        String query = "{\"query\": \"mutation {addLikePost(account_seq:\\\"1\\\",post_seq:\\\"1\\\",isAdd:false){success,status}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - like remove post - 성공")
    @Test
    void graphql_isLike_correct() throws IOException {

        String query = "{\"query\": \"mutation {addLikePost(account_seq:\\\"1\\\",post_seq:\\\"2\\\"){success,status}}\"}";

        URL url = new URL("http://localhost:8080/api/post");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(query.getBytes("euc-kr"));
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        }

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @AfterEach
    void tearDown(){

        log.info("server stop");
    }
}
