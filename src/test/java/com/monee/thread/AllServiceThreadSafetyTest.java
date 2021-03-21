package com.monee.thread;

import com.monee.dto.AccountDto;
import com.monee.errors.ThreadException;
import com.monee.model.Account;
import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import com.monee.service.AccountService;
import com.monee.service.PostService;
import com.monee.service.ReplyService;
import com.monee.thread.account.AccountThreadSafetyTest;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AllServiceThreadSafetyTest {

    private static Logger log = LoggerFactory.getLogger(AllServiceThreadSafetyTest.class);

    public static void main(String[] args) throws SQLException {
        ObjectPool pool = ObjectPool.getInstance();
        AccountService accountService = pool.getAccountService();

        PostService postService = pool.getPostService();
        Long seq = postService.findAll().get(0).getSeq();

        System.out.println("Test start!!");
        log.info("exists " + postService.findById(seq));

        System.out.println("Test start!!");
        log.info("exists " + accountService.findByEmail("kjuioqqq@naver.com"));

        ReplyService replyService = pool.getReplyService();
        Long replySeq = replyService.findAll().get(0).getSeq();
        Long postSeq = postService.findAll().get(0).getSeq();


        new Thread(() -> {
            log.info("Thread 1 start");
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto1 = new AccountDto();
                    dto1.setNickname("nickname : ");
                    dto1.setEmail(UUID.randomUUID().toString().substring(0,10).substring(0,10));
                    dto1.setPassword("123123123");
                    dto1.setEmail(dto1.getEmail() + "Thread 1 - test " + i);
                    dto1.setNickname(dto1.getNickname() + "Thread 1 - test " + i);
                    Account account = accountService.signup(dto1);
                    if(!account.getEmail().equals(dto1.getEmail())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    account = accountService.updatePassword(account.getSeq(),"123123");
                    if(account == null || !BCrypt.checkpw("123123",account.getPassword())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!accountService.login(account.getEmail(),"123123")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    account = accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(account == null || !account.getNickname().equals("updatedNickname")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
                        throw new ThreadException("Thread Exception : account " + account);
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
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto1 = new AccountDto();
                    dto1.setNickname("nickname : ");
                    dto1.setEmail(UUID.randomUUID().toString().substring(0,10).substring(0,10));
                    dto1.setPassword("123123123");
                    dto1.setEmail(dto1.getEmail() + "Thread 1 - test " + i);
                    dto1.setNickname(dto1.getNickname() + "Thread 1 - test " + i);
                    Account account = accountService.signup(dto1);
                    if(!account.getEmail().equals(dto1.getEmail())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    account = accountService.updatePassword(account.getSeq(),"123123");
                    if(account == null || !BCrypt.checkpw("123123",account.getPassword())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!accountService.login(account.getEmail(),"123123")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    account = accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(account == null || !account.getNickname().equals("updatedNickname")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
                        throw new ThreadException("Thread Exception : account " + account);
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
            for (int i = 0; i< 1000; i++) {
                try {
                    AccountDto dto1 = new AccountDto();
                    dto1.setNickname("nickname : ");
                    dto1.setEmail(UUID.randomUUID().toString().substring(0,10).substring(0,10));
                    dto1.setPassword("123123123");
                    dto1.setEmail(dto1.getEmail() + "Thread 1 - test " + i);
                    dto1.setNickname(dto1.getNickname() + "Thread 1 - test " + i);
                    Account account = accountService.signup(dto1);
                    if(!account.getEmail().equals(dto1.getEmail())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    account = accountService.findByEmail("kjuioqqq@naver.com").orElseThrow(NullPointerException::new);
                    Thread.sleep(300);
                    account = accountService.updatePassword(account.getSeq(),"123123");
                    if(account == null || !BCrypt.checkpw("123123",account.getPassword())){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!accountService.login(account.getEmail(),"123123")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    account = accountService.updateNickname(account.getSeq(),"updatedNickname");
                    if(account == null || !account.getNickname().equals("updatedNickname")){
                        throw new ThreadException("Thread Exception : account " + account);
                    }
                    Thread.sleep((int)Math.random()*1000);
                    if(!account.getEmail().equals("kjuioqqq@naver.com")){
                        throw new ThreadException("Thread Exception : account " + account);
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

        new Thread(() -> {
            log.info("Thread 1 start");
            int cnt = 0;
            for (int i = 0; i< 1000; i++) {
                try {
                    Reply reply = replyService.findByIdWithAccount(replySeq);
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
                    Reply updated = replyService.updateContent(replySeq,"updatedContent");
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
                    Reply reply = replyService.findByIdWithAccount(replySeq);
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
                    Reply updated = replyService.updateContent(replySeq,"updatedContent");
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
                    Reply reply = replyService.findByIdWithAccount(replySeq);
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
                    Reply updated = replyService.updateContent(replySeq,"updatedContent");
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
