package com.monee.thread;

import com.monee.CoreApplication;
import com.monee.controller.AccountControllerTest;
import com.monee.model.Account;
import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import com.monee.service.ReplyService;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeAll;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadSafetyTest {

    private static final int PORT = 8080;
    private static ClientAndServer mockServer;
    private static Logger log = LoggerFactory.getLogger(ThreadSafetyTest.class);
    // 21년 4월 19일 만료
    private final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJpc3N1ZXIiLCJuaWNrbmFtZSI6ImVhc3RwZXJzb24iLCJleHAiOjE2MTg3MTEyMzEsImlhdCI6MTYxNjExOTIzMSwidXNlcktleSI6MSwiZW1haWwiOiJranVpb3FxcUBuYXZlci5jb20ifQ.PHm_En1LGzBgrcWGAdiKvvh28qxYli5ROx2pNxyZCvyEZ5OBHHwNS-XaVqxsbFyJ5CR1ePQ-mh2uX6iFZyLapQ";

    private String mHero;

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        ReplyService replyService = pool.getReplyService();

        log.info("new reply : "+replyService.save(30L,30L,"thread test 0"));

        System.out.println("Test start!!");
        new Thread(() -> {
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.save(30L,30L,"thread 1 test : " + i);
                    Thread.sleep((int) (Math.random() * 1000));
                    if(!reply.getContent().equals("thread 1 test : " + i)){
                        log.info("====================================================test1 error 발생");
                        log.info("error reply : " + reply);
                        System.exit(0);
                        break;
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test1 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.save(30L,30L,"thread 2 test : " + i);
                    Thread.sleep((int) (Math.random() * 1000));
                    if(!reply.getContent().equals("thread 2 test : " + i)){
                        log.info("====================================================test2 error 발생");
                        log.info("error reply : " + reply);
                        System.exit(0);
                        break;
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test2 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i< 1000; i++) {
                try {
                    System.out.println(replyService);
                    System.out.println("Thread 3 : " + i);
                    Reply reply = replyService.save(30L,30L,"thread 3 test : " + i);
                    Thread.sleep((int) (Math.random() * 1000));
                    if(!reply.getContent().equals("thread 3 test : " + i)){
                        log.info("====================================================test3 error 발생");
                        log.info("error reply : " + reply);
                        System.exit(0);
                        break;
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test3 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();

        System.out.println("Test end!");

    }
}
