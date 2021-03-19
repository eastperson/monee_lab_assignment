package com.monee.thread.post;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import com.monee.thread.account.service.CreateAccountThreadSafetyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class PostServiceThreadSafetyTest {
    private static Logger log = LoggerFactory.getLogger(PostServiceThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        PostService postService = pool.getPostService();

        System.out.println("Test start!!");
        log.info("exists " + postService.findById(30L));

        new Thread(() -> {
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Post post = postService.findByIdWithAccount(30L);
                    if(post == null || post.getAuthor() == null){
                        throw new NullPointerException();
                    }
                    Thread.sleep(300);
                    if(postService.findAll() == null){
                        throw new NullPointerException();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    post = postService.save("title","content",30L);
                    post = postService.findByIdWithAll(post.getSeq());
                    if(post.getAuthor().getSeq() != 30L || post.getReplyList() == null || post.getReplyList().isEmpty() || post.getReplyList().size() <1){
                        throw new NullPointerException();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.addLike(1L,30L) || !postService.cancleLike(1L,30L)){
                        throw new Exception();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.isLikedPost(1L,1L)){
                        throw new Exception();
                    }
                    Post updated = postService.updateContent(1L,"123123");
                    if(!updated.getContent().equals("123123")){
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
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Post post = postService.findByIdWithAccount(30L);
                    if(post == null || post.getAuthor() == null){
                        throw new NullPointerException();
                    }
                    Thread.sleep(300);
                    if(postService.findAll() == null){
                        throw new NullPointerException();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    post = postService.save("title","content",30L);
                    post = postService.findByIdWithAll(post.getSeq());
                    if(post.getAuthor().getSeq() != 30L || post.getReplyList() == null || post.getReplyList().isEmpty() || post.getReplyList().size() <1){
                        throw new NullPointerException();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.addLike(2L,30L) || !postService.cancleLike(2L,30L)){
                        throw new Exception();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.isLikedPost(1L,1L)){
                        throw new Exception();
                    }
                    Post updated = postService.updateContent(1L,"123123");
                    if(!updated.getContent().equals("123123")){
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
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Post post = postService.findByIdWithAccount(30L);
                    if(post == null || post.getAuthor() == null){
                        throw new NullPointerException();
                    }
                    Thread.sleep(300);
                    if(postService.findAll() == null){
                        throw new NullPointerException();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    post = postService.save("title","content",30L);
                    post = postService.findByIdWithAll(post.getSeq());
                    if(post.getAuthor().getSeq() != 30L || post.getReplyList() == null || post.getReplyList().isEmpty() || post.getReplyList().size() <1){
                        throw new NullPointerException();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.addLike(3L,30L) || !postService.cancleLike(3L,30L)){
                        throw new Exception();
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.isLikedPost(1L,1L)){
                        throw new Exception();
                    }
                    Post updated = postService.updateContent(1L,"123123");
                    if(!updated.getContent().equals("123123")){
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
    }
}
