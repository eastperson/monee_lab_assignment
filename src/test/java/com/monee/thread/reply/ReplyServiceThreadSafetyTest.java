package com.monee.thread.reply;

import com.monee.model.Account;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import com.monee.service.ReplyService;
import com.monee.thread.account.service.CreateAccountThreadSafetyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ReplyServiceThreadSafetyTest {
    private static Logger log = LoggerFactory.getLogger(ReplyServiceThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        ReplyService replyService = pool.getReplyService();

        System.out.println("Test start!!");
        log.info("exists " + replyService.findById(10L));

        new Thread(() -> {
            log.info("Thread 1 start");
            int cnt = 0;
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.findByIdWithAccount(10L);
                    if(reply == null || reply.getAuthor() == null){
                        throw new NullPointerException();
                    }
                    if(replyService.findAll() == null){
                        throw new NullPointerException();
                    }
                    reply = replyService.save(30L,30L,"reply : " +i);
                    reply = replyService.findByIdWithAccount(reply.getSeq());
                    if(reply.getAuthor().getSeq() != 30L){
                        throw new NullPointerException();
                    }
                    List<Reply> list = replyService.findByPostSeq(30L);
                    if(list.size() < 0 || list == null || list.isEmpty()){
                        throw new NullPointerException();
                    }

                    Reply updated = replyService.updateContent(30L,"updatedContent");
                    log.info("update : " + updated);
                    if(!updated.getContent().equals("updatedContent")){
                        throw new Exception();
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
            log.info("Thread 2 start");
            for (int i = 0; i< 1000; i++) {
                try {

                    Reply reply = replyService.findByIdWithAccount(10L);
                    if(reply == null || reply.getAuthor() == null){
                        throw new NullPointerException();
                    }
                    if(replyService.findAll() == null){
                        throw new NullPointerException();
                    }
                    reply = replyService.save(30L,30L,"reply : " +i);
                    reply = replyService.findByIdWithAccount(reply.getSeq());
                    if(reply.getAuthor().getSeq() != 30L){
                        throw new NullPointerException();
                    }
                    List<Reply> list = replyService.findByPostSeq(30L);
                    if(list.size() < 0 || list == null || list.isEmpty()){
                        throw new NullPointerException();
                    }

                    Reply updated = replyService.updateContent(30L,"updatedContent");
                    log.info("update : " + updated);
                    if(!updated.getContent().equals("updatedContent")){
                        throw new Exception();
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
            log.info("Thread 3 start");
            for (int i = 0; i< 1000; i++) {
                try {

                    Reply reply = replyService.findByIdWithAccount(10L);
                    if(reply == null || reply.getAuthor() == null){
                        throw new NullPointerException();
                    }
                    if(replyService.findAll() == null){
                        throw new NullPointerException();
                    }
                    reply = replyService.save(30L,30L,"reply : " +i);
                    reply = replyService.findByIdWithAccount(reply.getSeq());
                    if(reply.getAuthor().getSeq() != 30L){
                        throw new NullPointerException();
                    }
                    List<Reply> list = replyService.findByPostSeq(30L);
                    if(list.size() < 0 || list == null || list.isEmpty()){
                        throw new NullPointerException();
                    }

                    Reply updated = replyService.updateContent(30L,"updatedContent");
                    log.info("update : " + updated);
                    if(!updated.getContent().equals("updatedContent")){
                        throw new Exception();
                    }

                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.info("====================================================test3 error 발생");
                    System.exit(0);
                    break;
                }
            }
        }).start();
    }
}
