package com.monee.controller;

import com.google.gson.Gson;
import com.monee.CoreApplication;
import com.monee.model.Account;
import com.monee.utils.ResultApi;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AccountControllerTest {

    private static final int PORT = 8080;
    private static ClientAndServer mockServer;
    private static Logger log = LoggerFactory.getLogger(AccountControllerTest.class);
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


    @DisplayName("로그인 성공")
    @Test
    void account_login_correct() throws IOException {

        log.info("test start");

        Map<String,String> map = new HashMap<>();
        map.put("email","kjuioqqq@naver.com");
        map.put("password","123123");
        Gson gson = new Gson();
        String loginRequestJson = gson.toJson(map);
        log.info("login request json : " + loginRequestJson);

        URL url = new URL("http://localhost:8080/api/account/login");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(loginRequestJson.length()));
        con.setRequestProperty("Accept","application/json");

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(loginRequestJson.getBytes("euc-kr"));
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

    @DisplayName("로그인 실패(비밀번호 오류)")
    @Test
    void account_login_error() throws IOException {

        log.info("test start");

        Map<String,String> map = new HashMap<>();
        map.put("email","kjuioqqq@naver.com");
        map.put("password","error");
        Gson gson = new Gson();
        String loginRequestJson = gson.toJson(map);
        log.info("login request json : " + loginRequestJson);

        URL url = new URL("http://localhost:8080/api/account/login");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(loginRequestJson.length()));
        con.setRequestProperty("Accept","application/json; charset=utf-8");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        con.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(loginRequestJson.getBytes());
        os.flush();
        os.close();

        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {

            br = new BufferedReader(new InputStreamReader( con.getInputStream(),"UTF-8"));
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                responseBody += strCurrentLine;
                log.info(strCurrentLine);
            }
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
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
        log.info("test complete");

        assertTrue(responseCode == 400);
    }

    @DisplayName("회원가입 성공")
    @Test
    void signup_correct() throws IOException {

        log.info("test start");

        Map<String,String> map = new HashMap<>();
        map.put("email", UUID.randomUUID().toString().substring(0,10)+"@naver.com");
        map.put("nickname","eastperson");
        map.put("password","123123");
        Map<String,Map<String,String>> obj = new HashMap<>();
        obj.put("request",map);

        Gson gson = new Gson();
        String signupRequestJson = gson.toJson(obj);
        log.info("login request json : " + signupRequestJson);

        URL url = new URL("http://localhost:8080/api/account/signup");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(signupRequestJson.length()));
        con.setRequestProperty("Accept","application/json");

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(signupRequestJson.getBytes("euc-kr"));
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
        ResultApi<Account> result = gson.fromJson(responseBody,ResultApi.class);

        assertTrue(responseCode == 201);
        assertTrue(result.getSuccess());
        log.info("test complete");
    }

    @DisplayName("GraphQL - 회원 목록 찾기 성공")
    @Test
    void graphql_all_accounts_correct() throws IOException {

        String query = "{\"query\" : \"{allAccounts{success,status,data{seq,email,nickname,seq}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
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

    @Test
    void test(){
        TestResult testResult=  new TestResult();
        ResultApi<Account> result = new ResultApi<>();
        result.setStatus(200);
        result.setSuccess(true);
        result.setData(null);
        testResult.setAllAccounts(result);

        Gson gson = new Gson();

        String str = gson.toJson(testResult);

        log.info("result : "+ str);

        Map<String,Object> map = new HashMap<>();
        map.put("result","rest");
        log.info("result2 : " + map);

    }

    class TestResult{
        private ResultApi<Account> allAccounts;

        public ResultApi getResult(){
            return this.allAccounts;
        }

        public void setAllAccounts(ResultApi<Account> allAccounts) {
            this.allAccounts = allAccounts;
        }

        @Override
        public String toString() {
            return "TestResult{" +
                    "allAccounts=" + allAccounts +
                    '}';
        }
    }

    @DisplayName("GraphQL - 회원목록 실패(토큰오류)")
    @Test
    void graphql_all_accounts_error() throws IOException {

        String query = "{\"query\" : \"{allAccounts{success,status,data{seq,email,nickname,seq}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        //con.setRequestProperty("Authorization","Bearer " + TOKEN);

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

        assertTrue(responseCode == 405);
        log.info("test complete");
    }

    @DisplayName("GraphQL - 회원 찾기 by id - 성공")
    @Test
    void graphql_findById_correct() throws IOException {

        String query = "{\"query\" : \"{Account(seq:1){success,status,data{seq,email,nickname,password}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
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
    @DisplayName("GraphQL - 회원 찾기 by id - 실패(토큰 오류)")
    @Test
    void graphql_findById_error() throws IOException {

        String query = "{\"query\" : \"{Account(seq:1){success,status,data{seq,email,nickname,password}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 헤더 등록
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        //con.setRequestProperty("Authorization","Bearer " + TOKEN);

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

        assertTrue(responseCode == 405);
        log.info("test complete");
    }

    @DisplayName("GraphQL - 회원 가입 - 성공")
    @Test
    void graphql_createAccount_correct() throws IOException {

        String query = "{\"query\": \"mutation{createAccount(email:\\\""+ UUID.randomUUID().toString().substring(0,10)+"@com.com\\\",nickname:\\\"eastperson\\\",password:\\\"123123\\\"){success,status,data{seq,email,nickname,password}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
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

    @DisplayName("GraphQL - 닉네임 변경 - 성공")
    @Test
    void graphql_update_nickname_correct() throws IOException {

        String query = "{\"query\": \"mutation {updateAccount(seq:1,nickname:\\\"eastperson2\\\"){success,status,data{seq,nickname,password}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
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

    @DisplayName("GraphQL - 패스워드 변경 - 성공")
    @Test
    void graphql_update_password_correct() throws IOException {

        String query = "{\"query\": \"mutation {updateAccount(seq:1,password:\\\"aaabbbcccc\\\"){success,status,data{seq,nickname,password}}}\"}";

        URL url = new URL("http://localhost:8080/api/account");
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
