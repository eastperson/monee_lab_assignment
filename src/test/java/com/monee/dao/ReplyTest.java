package com.monee.dao;

import com.monee.model.Account;
import com.monee.model.Post;
import com.monee.model.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplyTest {

    private ReplyDao replyDao;

    @DisplayName("시퀀스 찾기 테스트")
    @Test
    void findById() throws SQLException {

        replyDao = new ReplyDao();

        Optional<Reply> result = replyDao.findById(1L);

        System.out.println(result);

        if(result.isPresent()){
            Reply reply = result.get();
            System.out.println(reply);
            assertNotNull(reply);
            assertTrue(reply.getSeq().equals(1L));
            assertNull(reply.getAuthor());
        }

    }

    @DisplayName("댓글 + 계정 찾기 테스트")
    @Test
    void findByIdWithAccount() throws SQLException {

        replyDao = new ReplyDao();

        Optional<Reply> result = replyDao.findByIdWithAccount(1L);

        System.out.println(result);

        if(result.isPresent()){
            Reply reply = result.get();
            System.out.println(reply);
            assertNotNull(reply);
            assertTrue(reply.getSeq().equals(1L));
            assertNotNull(reply.getAuthor());
        }

    }

    @DisplayName("댓글 전체 찾기 테스트")
    @Test
    void findAll() throws SQLException {

        replyDao = new ReplyDao();

        List<Reply> result = replyDao.findAll();

        System.out.println(result);

        assertNotNull(result);

    }

    @DisplayName("댓글 포스트 아이디 테스트")
    @Test
    void findByPostId() throws SQLException {

        replyDao = new ReplyDao();

        List<Reply> result = replyDao.findByPostId(33L);

        System.out.println(result);

        assertNotNull(result);

    }

    @DisplayName("댓글 작성 테스트")
    @Test
    void save() throws SQLException {

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.findAll().get(0);
        assertNotNull(account);
        ReplyDao replyDao = new ReplyDao();
        PostDao postDao = new PostDao(accountDao,replyDao);
        Post post = new Post("타이틀","콘텐츠");
        Optional<Post> result = postDao.save(account.getSeq(),post);
        replyDao.setPostDao(postDao);
        replyDao.setAccountDao(accountDao);

        if(result.isPresent()){
            Post newPost = result.get();
            System.out.println(newPost);
            assertTrue(post.getTitle().equals(newPost.getTitle()));
            assertTrue(post.getContent().equals(newPost.getContent()));
            assertTrue(newPost.getRevwCnt().equals(0L));
            assertNull(newPost.getAuthor());
            System.out.println("new post : "+newPost);
            Account author = postDao.findByIdWithAccount(newPost.getSeq()).get().getAuthor();
            assertNotNull(author);
            assertTrue(author.getSeq().equals(account.getSeq()));
            System.out.println("author : "+author);

            Reply reply = new Reply("댓글 내용");
            Optional<Reply> resultReply = replyDao.save(account.getSeq(),newPost.getSeq(),reply);
            Reply newReply = resultReply.get();
            System.out.println("new reply : "+newReply);
            assertTrue(newReply.getContent().equals("댓글 내용"));


            Account replyWriter = replyDao.findByIdWithAccount(newReply.getSeq()).get().getAuthor();
            System.out.println("reply writer : "+replyWriter);
            assertNotNull(replyWriter);
            assertTrue(replyWriter.getSeq().equals(account.getSeq()));

            assertTrue(postDao.findById(newPost.getSeq()).get().getRevwCnt() == 1);
        }
    }

    @DisplayName("댓글 내용 수정 테스트")
    @Test
    void updateContent() throws SQLException {
        replyDao = new ReplyDao();
        Reply reply = replyDao.findAll().get(1);
        System.out.println("reply : "+reply);
        assertNotNull(reply);
        int result = replyDao.updateContent(reply.getSeq(), "변경");
        assertTrue(result > 0);

        Optional<Reply> rs = replyDao.findById(reply.getSeq());
        if(rs.isPresent()){
            Reply update = rs.get();
            System.out.println("update : "+update);
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

        replyDao = new ReplyDao();
        Reply reply = new Reply("댓글 내용");
        Optional<Reply> rs = replyDao.save(account.getSeq(),newPost.getSeq(),reply);
        if(rs.isPresent()){
            Reply newReply = replyDao.findById(rs.get().getSeq()).get();
            assertNotNull(newReply);
            System.out.println(newReply);
            int rss = replyDao.delete(newReply.getSeq());
            assertTrue(rss > 0);
            assertFalse(replyDao.findById(newReply.getSeq()).isPresent());
        }
    }
}
