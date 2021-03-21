package com.monee.thread.post;

import com.monee.errors.ThreadException;
import com.monee.model.Post;
import com.monee.pool.ObjectPool;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class PostServiceThreadSafetyTest {
    private static Logger log = LoggerFactory.getLogger(PostServiceThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {

        ObjectPool pool = ObjectPool.getInstance();
        PostService postService = pool.getPostService();
        Long seq = postService.findAll().get(0).getSeq();

        System.out.println("Test start!!");
        log.info("exists " + postService.findById(seq));

        new Thread(() -> {
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    Post post = postService.findByIdWithAccount(seq);
                    if(post == null || post.getAuthor() == null){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep(300);
                    if(postService.findAll() == null){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    post = postService.save("title","content",30L);
                    if(post == null || !post.getTitle().equals("title") || !post.getContent().equals("content")){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    post = postService.findByIdWithAll(post.getSeq());
                    ReplyService replyService = ObjectPool.getInstance().getReplyService();;
                    replyService.save(post.getAuthor().getSeq(),post.getSeq(),"댓글 내용");
                    post = postService.findByIdWithAll(post.getSeq());
                    if(post.getAuthor().getSeq() != 30L || post.getReplyList() == null || post.getReplyList().isEmpty() || post.getReplyList().size() <1){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.addLike(1L,seq) || !postService.isLikedPost(1L,seq) || !postService.cancleLike(1L,seq)){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    Post updated = postService.updateContent(seq,"123123");
                    if(!updated.getContent().equals("123123")){
                        throw new ThreadException("Thread Exception : post " + post);
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
                    Post post = postService.findByIdWithAccount(seq);
                    if(post == null || post.getAuthor() == null){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep(300);
                    if(postService.findAll() == null){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    post = postService.save("title","content",30L);
                    if(post == null || !post.getTitle().equals("title") || !post.getContent().equals("content")){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    post = postService.findByIdWithAll(post.getSeq());
                    ReplyService replyService = ObjectPool.getInstance().getReplyService();;
                    replyService.save(post.getAuthor().getSeq(),post.getSeq(),"댓글 내용");
                    post = postService.findByIdWithAll(post.getSeq());
                    if(post.getAuthor().getSeq() != 30L || post.getReplyList() == null || post.getReplyList().isEmpty() || post.getReplyList().size() <1){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.addLike(1L,seq) || !postService.isLikedPost(1L,seq) || !postService.cancleLike(1L,seq)){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    Post updated = postService.updateContent(seq,"123123");
                    if(!updated.getContent().equals("123123")){
                        throw new ThreadException("Thread Exception : post " + post);
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
                    Post post = postService.findByIdWithAccount(seq);
                    if(post == null || post.getAuthor() == null){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep(300);
                    if(postService.findAll() == null){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    post = postService.save("title","content",30L);
                    if(post == null || !post.getTitle().equals("title") || !post.getContent().equals("content")){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    post = postService.findByIdWithAll(post.getSeq());
                    ReplyService replyService = ObjectPool.getInstance().getReplyService();;
                    replyService.save(post.getAuthor().getSeq(),post.getSeq(),"댓글 내용");
                    post = postService.findByIdWithAll(post.getSeq());
                    if(post.getAuthor().getSeq() != 30L || post.getReplyList() == null || post.getReplyList().isEmpty() || post.getReplyList().size() <1){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!postService.addLike(1L,seq) || !postService.isLikedPost(1L,seq) || !postService.cancleLike(1L,seq)){
                        throw new ThreadException("Thread Exception : post " + post);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    Post updated = postService.updateContent(seq,"123123");
                    if(!updated.getContent().equals("123123")){
                        throw new ThreadException("Thread Exception : post " + post);
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
