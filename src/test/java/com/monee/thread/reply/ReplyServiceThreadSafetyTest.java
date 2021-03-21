package com.monee.thread.reply;

import com.monee.errors.ThreadException;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ReplyServiceThreadSafetyTest {
    private static Logger log = LoggerFactory.getLogger(ReplyServiceThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        ReplyService replyService = pool.getReplyService();
        Long seq = replyService.findAll().get(0).getSeq();
        PostService postService = ObjectPool.getInstance().getPostService();
        Long postSeq = postService.findAll().get(0).getSeq();

        System.out.println("Test start!!");
        log.info("exists " + replyService.findById(seq));

        new Thread(() -> {
            log.info("Thread 1 start");
            int cnt = 0;
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.findByIdWithAccount(seq);
                    if(reply == null || reply.getAuthor() == null){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(replyService.findAll() == null){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    reply = replyService.save(30L,postSeq,"reply : " +i);
                    reply = replyService.findByIdWithAccount(reply.getSeq());
                    if(reply.getAuthor().getSeq() != 30L){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    List<Reply> list = replyService.findByPostSeq(postSeq);
                    if(list.size() < 0 || list == null || list.isEmpty()){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    Reply updated = replyService.updateContent(seq,"updatedContent");
                    if(!updated.getContent().equals("updatedContent")){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.error("====================================================test1 error 발생");
                    log.error(throwables.getMessage());
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            log.info("Thread 2 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.findByIdWithAccount(seq);
                    if(reply == null || reply.getAuthor() == null){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(replyService.findAll() == null){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    reply = replyService.save(30L,postSeq,"reply : " +i);
                    reply = replyService.findByIdWithAccount(reply.getSeq());
                    if(reply.getAuthor().getSeq() != 30L){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    List<Reply> list = replyService.findByPostSeq(postSeq);
                    if(list.size() < 0 || list == null || list.isEmpty()){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    Reply updated = replyService.updateContent(seq,"updatedContent");
                    if(!updated.getContent().equals("updatedContent")){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.error("====================================================test2 error 발생");
                    log.error(throwables.getMessage());
                    System.exit(0);
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            log.info("Thread 3 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.findByIdWithAccount(seq);
                    if(reply == null || reply.getAuthor() == null){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(replyService.findAll() == null){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    reply = replyService.save(30L,postSeq,"reply : " +i);
                    reply = replyService.findByIdWithAccount(reply.getSeq());
                    if(reply.getAuthor().getSeq() != 30L){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    List<Reply> list = replyService.findByPostSeq(postSeq);
                    if(list.size() < 0 || list == null || list.isEmpty()){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    Reply updated = replyService.updateContent(seq,"updatedContent");
                    if(!updated.getContent().equals("updatedContent")){
                        throw new ThreadException("Thread Exception : reply " + reply);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                    log.error("====================================================test3 error 발생");
                    log.error(throwables.getMessage());
                    System.exit(0);
                    break;
                }
            }
        }).start();
    }
}
