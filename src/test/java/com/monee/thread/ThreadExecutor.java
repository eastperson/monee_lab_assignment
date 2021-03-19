package com.monee.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadExecutor implements Runnable{

    private static Logger log = LoggerFactory.getLogger(ThreadExecutor.class);
    // 21년 4월 19일 만료
    private final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJpc3N1ZXIiLCJuaWNrbmFtZSI6ImVhc3RwZXJzb24iLCJleHAiOjE2MTg3MTEyMzEsImlhdCI6MTYxNjExOTIzMSwidXNlcktleSI6MSwiZW1haWwiOiJranVpb3FxcUBuYXZlci5jb20ifQ.PHm_En1LGzBgrcWGAdiKvvh28qxYli5ROx2pNxyZCvyEZ5OBHHwNS-XaVqxsbFyJ5CR1ePQ-mh2uX6iFZyLapQ";
    private String threadName = "";


    public ThreadExecutor(){}
    public ThreadExecutor(String threadName){
        this.threadName = threadName;
    }

    @Override
    public void run() {

        log.info("=====================run : " + threadName);
        log.info("=====================end : " + threadName);
        log.info("test start");
        long startTime = System.currentTimeMillis();



        try{
            Thread.sleep((int) Math.random() * 10000);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String query = "{\"query\" : \"{allAccounts{success,status,data{seq,email,nickname,seq}}}\"}";

        URL url = null;
        try {
            url = new URL("http://localhost:8080/api/account");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 헤더 등록
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        con.setRequestProperty("Content-type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(query.length()));
        con.setRequestProperty("Accept","application/json");
        con.setRequestProperty("Authorization","Bearer " + TOKEN);

        // 리퀘스트 바디 등록
        con.setDoOutput(true);
        OutputStream os = null;
        try {
            os = con.getOutputStream();
            os.write(query.getBytes("euc-kr"));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 리스폰스 바디 등록
        //con.setDoInput(true);
        BufferedReader br = null;
        String responseBody = "";
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        int responseCode = 0;

        try {
            responseCode = con.getResponseCode();
            String responseMsg = con.getResponseMessage();
            log.info("response code : " + responseCode);
            log.info("reponse msg : " + responseMsg);
            log.info("response body : " + responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(responseCode == 200);

        long endTime = System.currentTimeMillis();

        log.info(threadName + " 경과시간 : " + (endTime - startTime));

        log.info(threadName + " : test complete");

    }
}
