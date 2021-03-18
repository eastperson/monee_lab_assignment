package com.monee.dao;

import com.monee.model.Account;
import com.monee.model.Post;
import com.monee.model.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplyTest {

    private ReplyDao replyDao;
    private static Logger log = LoggerFactory.getLogger(ReplyTest.class);

    @DisplayName("시퀀스 찾기 테스트")
    @Test
    void findById() throws SQLException {

        AccountDao accountDao = new AccountDao();
        PostDao postDao = new PostDao();
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);

        Optional<Reply> result = replyDao.findById(1L);

        log.info(result.toString());

        if(result.isPresent()){
            Reply reply = result.get();
            log.info(reply.toString());
            assertNotNull(reply);
            assertTrue(reply.getSeq().equals(1L));
            assertNull(reply.getAuthor());
        }

    }

    @DisplayName("댓글 + 계정 찾기 테스트")
    @Test
    void findByIdWithAccount() throws SQLException {

        AccountDao accountDao = new AccountDao();
        PostDao postDao = new PostDao();
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);

        Optional<Reply> result = replyDao.findByIdWithAccount(1L);

        log.info(result.toString());

        if(result.isPresent()){
            Reply reply = result.get();
            log.info(reply.toString());
            assertNotNull(reply);
            assertTrue(reply.getSeq().equals(1L));
            assertNotNull(reply.getAuthor());
        }

    }

    @DisplayName("댓글 전체 찾기 테스트")
    @Test
    void findAll() throws SQLException {

        AccountDao accountDao = new AccountDao();
        PostDao postDao = new PostDao();
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);

        List<Reply> result = replyDao.findAll();

        log.info(result.toString());

        assertNotNull(result);

    }

    @DisplayName("댓글 포스트 아이디 테스트")
    @Test
    void findByPostId() throws SQLException {

        AccountDao accountDao = new AccountDao();
        PostDao postDao = new PostDao();
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);

        List<Reply> result = replyDao.findByPostId(33L);

        log.info(result.toString());

        assertNotNull(result);

    }

    @DisplayName("댓글 작성 테스트")
    @Test
    void save() throws SQLException {

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.findAll().get(0);
        assertNotNull(account);

        PostDao postDao = new PostDao(accountDao,replyDao);
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);
        Post post = new Post("타이틀","콘텐츠");
        Optional<Post> result = postDao.save(account.getSeq(),post);


        if(result.isPresent()){
            Post newPost = result.get();
            log.info(newPost.toString());
            assertTrue(post.getTitle().equals(newPost.getTitle()));
            assertTrue(post.getContent().equals(newPost.getContent()));
            assertTrue(newPost.getRevwCnt().equals(0L));
            assertNull(newPost.getAuthor());
            log.info("new post : "+newPost);
            Account author = postDao.findByIdWithAccount(newPost.getSeq()).get().getAuthor();
            assertNotNull(author);
            assertTrue(author.getSeq().equals(account.getSeq()));
            log.info("author : "+author);

            Reply reply = new Reply("댓글 내용");
            Optional<Reply> resultReply = replyDao.save(account.getSeq(),newPost.getSeq(),reply);
            Reply newReply = resultReply.get();
            log.info("new reply : "+newReply);
            assertTrue(newReply.getContent().equals("댓글 내용"));


            Account replyWriter = replyDao.findByIdWithAccount(newReply.getSeq()).get().getAuthor();
            log.info("reply writer : "+replyWriter);
            assertNotNull(replyWriter);
            assertTrue(replyWriter.getSeq().equals(account.getSeq()));

            assertTrue(postDao.findById(newPost.getSeq()).get().getRevwCnt() == 1);
        }
    }

    @DisplayName("댓글 내용 수정 테스트")
    @Test
    void updateContent() throws SQLException {
        AccountDao accountDao = new AccountDao();
        PostDao postDao = new PostDao();
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);
        Reply reply = replyDao.findAll().get(1);
        log.info("reply : "+reply);
        assertNotNull(reply);
        int result = replyDao.updateContent(reply.getSeq(), "변경");
        assertTrue(result > 0);

        Optional<Reply> rs = replyDao.findById(reply.getSeq());
        if(rs.isPresent()){
            Reply update = rs.get();
            log.info("update : "+update);
            assertNotNull(update);
            assertTrue(update.getContent().equals("변경"));
            assertTrue(reply.getUpdateAt().isBefore(update.getUpdateAt()));
            assertTrue(replyDao.updateContent(reply.getSeq(),reply.getContent())>0);
        }
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    void delete() throws SQLException {

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.findAll().get(0);
        assertNotNull(account);
        PostDao postDao = new PostDao();
        Post post = new Post("타이틀","콘텐츠");
        Optional<Post> result = postDao.save(account.getSeq(),post);
        Post newPost = result.get();

        ReplyDao replyDao = new ReplyDao(accountDao,postDao);
        Reply reply = new Reply("댓글 내용");
        Optional<Reply> rs = replyDao.save(account.getSeq(),newPost.getSeq(),reply);
        if(rs.isPresent()){
            Reply newReply = replyDao.findById(rs.get().getSeq()).get();
            assertNotNull(newReply);
            log.info(newReply.toString());
            int rss = replyDao.delete(newReply.getSeq());
            assertTrue(rss > 0);
            assertFalse(replyDao.findById(newReply.getSeq()).isPresent());
        }
    }
}
