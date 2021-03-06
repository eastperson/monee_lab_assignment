package com.monee.controller;

import com.monee.CoreApplication;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.ReplyService;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
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
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplyControllerTest {

    private static final int PORT = 8080;
    private static ClientAndServer mockServer;
    private static Logger log = LoggerFactory.getLogger(ReplyControllerTest.class);
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

    @DisplayName("GraphQL - reply find by seq - 성공")
    @Test
    void graphql_reply_correct() throws IOException {

        String query = "{\"query\": \"{Reply(seq:6902){success,status,data{seq,content,author{seq,email,nickname,password}}}}\"}";

        URL url = new URL("http://localhost:8080/api/reply");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        String responseBody = getResponseBody(query, con);

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - reply find all - 성공")
    @Test
    void graphql_findAll_correct() throws IOException {

        String query = "{\"query\": \"{allReplys{success,status,data{seq,content,author{seq,email,nickname}}}}\"}";

        URL url = new URL("http://localhost:8080/api/reply");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        String responseBody = getResponseBody(query, con);

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - reply find by post id - 성공")
    @Test
    void graphql_findByPostId_correct() throws IOException {

        String query = "{\"query\": \"{allReplys(post_seq:\\\"1725\\\"){success,status,data{seq,content,author{seq,email,nickname}}}}\"}";

        URL url = new URL("http://localhost:8080/api/reply");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        String responseBody = getResponseBody(query, con);

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - create reply - 성공")
    @Test
    void graphql_create_correct() throws IOException {

        String query = "{\"query\": \"mutation{createReply(author_seq:\\\"1\\\",post_seq:\\\"1720\\\",content:\\\"댓글 내용\\\"){success,status,data{seq,content,author{seq,email,nickname}}}}\"}";

        URL url = new URL("http://localhost:8080/api/reply");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        String responseBody = getResponseBody(query, con);

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - update reply content - 성공")
    @Test
    void graphql_updateContent_correct() throws IOException {

        String query = "{\"query\": \"mutation{updateReply(seq:6902,content:\\\"댓글 수정\\\"){success,status,data{seq,content,author{seq,email,nickname}}}}\"}";

        URL url = new URL("http://localhost:8080/api/reply");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        String responseBody = getResponseBody(query, con);

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @DisplayName("GraphQL - delete reply - 성공")
    @Test
    void graphql_delete_correct() throws IOException, SQLException {

        List<Reply> list = ObjectPool.getInstance().getReplyService().findAll();
        Reply reply = list.get(list.size()-1);

        String query = "{\"query\": \"mutation{deleteReply(seq:"+reply.getSeq()+"){success,status,data{seq,content,author{seq,email,nickname}}}}\"}";

        URL url = new URL("http://localhost:8080/api/reply");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);
        String responseBody = getResponseBody(query, con);

        int responseCode = con.getResponseCode();
        String responseMsg= con.getResponseMessage();


        log.info("response code : " + responseCode);
        log.info("reponse msg : " + responseMsg);
        log.info("response body : " + responseBody);

        assertTrue(responseCode == 200);
        log.info("test complete");
    }

    @NotNull
    private String getResponseBody(String query, HttpURLConnection con) throws IOException {
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
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
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
        return responseBody;
    }

    @AfterEach
    void tearDown(){

        log.info("server stop");
    }
}
