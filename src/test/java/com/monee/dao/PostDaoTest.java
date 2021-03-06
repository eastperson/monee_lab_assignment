package com.monee.dao;

import com.monee.model.Account;
import com.monee.model.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostDaoTest {

    private PostDao postDao;

    private static Logger log = LoggerFactory.getLogger(PostDaoTest.class);

    @DisplayName("시퀀스 찾기 테스트")
    @Test
    void findById() throws SQLException {

        postDao = new PostDao();

        Optional<Post> result = postDao.findById(1L);

        log.info(result.toString());

        if(result.isPresent()){
            Post post = result.get();
            log.info(post.toString());
            assertNotNull(post);
            assertTrue(post.getSeq().equals(1L));
            assertNull(post.getAuthor());
        }

    }

    @DisplayName("게시글 + 계정 찾기 테스트")
    @Test
    void findByIdWithAccount() throws SQLException {

        postDao = new PostDao();

        Optional<Post> result = postDao.findByIdWithAccount(1L);

        log.info(result.toString());

        if(result.isPresent()){
            Post post = result.get();
            log.info(post.toString());
            assertNotNull(post);
            assertTrue(post.getSeq().equals(1L));
            assertNotNull(post.getAuthor());
        }

    }

    @DisplayName("게시글 전체 찾기 테스트")
    @Test
    void findAll() throws SQLException {

        postDao = new PostDao();

        List<Post> result = postDao.findAll();

        log.info(result.toString());

        assertNotNull(result);

    }

    @DisplayName("게시글 만들기 테스트")
    @Test
    void save() throws SQLException {

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.findAll().get(0);
        assertNotNull(account);

        PostDao postDao = new PostDao();
        ReplyDao replyDao = new ReplyDao(accountDao,postDao);
        postDao = new PostDao(accountDao);
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
            log.info("author : "+account);
        }

    }

    @DisplayName("게시글 제목 변경 테스트")
    @Test
    void updateTitle() throws SQLException {
        postDao = new PostDao();
        Post post = postDao.findAll().get(0);
        log.info("post : "+post);
        assertNotNull(post);
        int result = postDao.updateTitle(post.getSeq(), "변경");
        assertTrue(result > 0);

        Optional<Post> rs = postDao.findById(post.getSeq());
        if(rs.isPresent()){
            Post update = rs.get();
            log.info("update : "+update);
            assertNotNull(update);
            assertTrue(update.getTitle().equals("변경"));
            assertTrue(post.getUpdateAt().isBefore(update.getUpdateAt()));
            assertTrue(postDao.updateTitle(post.getSeq(),post.getTitle())>0);
        }
    }

    @DisplayName("게시글 내용 테스트")
    @Test
    void updateContent() throws SQLException {
        postDao = new PostDao();
        Post post = postDao.findAll().get(1);
        log.info("post : "+post);
        assertNotNull(post);
        int result = postDao.updateContent(post.getSeq(), "변경");
        assertTrue(result > 0);

        Optional<Post> rs = postDao.findById(post.getSeq());
        if(rs.isPresent()){
            Post update = rs.get();
            log.info("update : "+update);
            assertNotNull(update);
            assertTrue(update.getContent().equals("변경"));
            assertTrue(post.getUpdateAt().isBefore(update.getUpdateAt()));
            assertTrue(postDao.updateContent(post.getSeq(),post.getContent())>0);
        }
    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    void delete() throws SQLException {

        AccountDao accountDao = new AccountDao();
        Account account = accountDao.findAll().get(0);
        assertNotNull(account);
        postDao = new PostDao();
        Post post = new Post("타이틀","콘텐츠");
        Optional<Post> result = postDao.save(account.getSeq(),post);
        if(result.isPresent()){
            Post newPost = postDao.findById(result.get().getSeq()).get();
            assertNotNull(newPost);
            int rs = postDao.delete(newPost.getSeq());
            assertTrue(rs > 0);
            assertFalse(postDao.findById(newPost.getSeq()).isPresent());
        }
    }
}
