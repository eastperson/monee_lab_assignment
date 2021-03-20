package com.monee.dao;

import com.monee.model.Post;
import com.monee.model.Reply;
import com.monee.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.monee.utils.DateTimeUtils.dateTimeOf;
import static com.monee.utils.DateTimeUtils.timestampOf;
import static org.junit.jupiter.api.Assertions.fail;

public class ReplyDao {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String DB_URL = "jdbc:mysql://localhost:3307/monee?autoReconnect=true";
    String DB_USER = "monee";
    String DB_PASSWORD = "1234";

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs;

    private static Logger log = LoggerFactory.getLogger(ReplyDao.class);
    
    private final AccountDao accountDao;

    private final PostDao postDao;

    public ReplyDao(AccountDao accountDao, PostDao postDao){
        this.postDao = postDao;
        this.accountDao = accountDao;
        try(Connection conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD)){
            this.conn = conn;
            log.info("connection 생성 : "+conn);

        }catch(Exception e) {
            fail(e.getMessage());

        }
    }

    public Optional<Reply> findById(Long seq) throws SQLException {

        String query = "SELECT * FROM replys WHERE seq =?";

        Reply reply = null;

        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                this.conn = connection;
                this.pstmt = preparedStatement;
                log.info("find by seq conn : " + this.conn);

                pstmt.setLong(1, seq);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    reply = new Reply(rs.getString("content"));
                    reply.setSeq(rs.getLong("seq"));
                    reply.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                    reply.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                }

                return Optional.of(reply);

            } catch (Exception e) {

                e.printStackTrace();
                return Optional.empty();

            }
        }
    }

    public Optional<Reply> findByIdWithAccount(Long seq) throws SQLException {

        String query = "SELECT * FROM replys WHERE seq = (?)";

        Reply reply = null;


        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                this.conn = connection;
                this.pstmt = preparedStatement;
                log.info("find by seq with all conn : " + this.conn);

                pstmt.setLong(1, seq);

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    reply = new Reply(rs.getString("content"));
                    reply.setSeq(rs.getLong("seq"));
                    reply.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                    reply.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                    reply.setAuthor(accountDao.findById(rs.getLong("author_seq")).get());
                }

                return Optional.of(reply);

            } catch (Exception e) {

                e.printStackTrace();
                return Optional.empty();

            }
        }
    }

    public List<Reply> findByPostId(Long postSeq) throws SQLException {

        String query = "SELECT * FROM replys WHERE post_seq =?";

        Reply reply = null;


        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                this.conn = connection;
                this.pstmt = preparedStatement;
                log.info("find by post id conn : " + this.conn);

                pstmt.setLong(1, postSeq);

                rs = pstmt.executeQuery();

                List<Reply> list = new ArrayList<>();

                while (rs.next()) {
                    reply = new Reply(rs.getString("content"));
                    reply.setSeq(rs.getLong("seq"));
                    reply.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                    reply.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                    reply.setAuthor(accountDao.findById(rs.getLong("author_seq")).get());
                    list.add(reply);
                }

                return list;

            } catch (Exception e) {

                e.printStackTrace();
                return Collections.EMPTY_LIST;

            }
        }
    }

    public List<Reply> findAll() throws SQLException {

        String query = "SELECT * FROM replys";

        Reply reply = null;

        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                this.conn = connection;
                this.pstmt = preparedStatement;

                log.info("find all conn : " + this.conn);

                rs = pstmt.executeQuery();

                List<Reply> list = new ArrayList<>();

                while (rs.next()) {
                    reply = new Reply(rs.getString("content"));
                    reply.setSeq(rs.getLong("seq"));
                    reply.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                    reply.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                    reply.setAuthor(accountDao.findById(rs.getLong("author_seq")).get());
                    list.add(reply);
                }
                return list;

            } catch (Exception e) {
                e.printStackTrace();
                return Collections.EMPTY_LIST;

            }
        }
    }

    public Optional<Reply> save(Long accountSeq,Long postSeq, Reply reply) throws SQLException {

        String query = "INSERT INTO replys (content,post_seq,author_seq) VALUES(?,?,?);";

        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
                log.info("save conn : " + this.conn);

                this.pstmt = preparedStatement;
                pstmt.setString(1, reply.getContent());
                pstmt.setLong(2, postSeq);
                pstmt.setLong(3, accountSeq);
                log.info("meta data : " + pstmt.getParameterMetaData());

                int result = pstmt.executeUpdate();
                log.info("저장한 개수 " + result);

                rs = pstmt.getGeneratedKeys();

                Optional<Reply> newReply = null;

                if (rs.next()) {
                    Long key = rs.getLong(1);
                    log.info("key : "+key);
                    newReply = findById(key);
                    updatePostWithReply(postSeq, key);
                    postDao.addRevwCnt(postSeq);
                }

                return newReply;

            } catch (Exception e) {

                e.printStackTrace();
                return Optional.empty();

            }

        }
    }

    public void updatePostWithReply(Long postSeq, Long replySeq){
        String query = "INSERT INTO posts_replys (post_seq,reply_seq) VALUES(?,?);";


        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                log.info("update post with reply : " + this.conn);

                this.pstmt = preparedStatement;
                pstmt.setLong(1, postSeq);
                pstmt.setLong(2, replySeq);

                int result = pstmt.executeUpdate();


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }

    public int updateContent(Long seq, String content) throws SQLException{

        String query = "UPDATE replys SET content = (?), update_at = (?) WHERE seq = (?)";

        synchronized (ReplyDao.class) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                log.info("update content conn : " + this.conn);

                this.pstmt = preparedStatement;
                pstmt.setString(1, content);
                pstmt.setTimestamp(2, timestampOf(LocalDateTime.now()));
                pstmt.setLong(3, seq);

                int result = pstmt.executeUpdate();

                return result;

            } catch (Exception e) {

                e.printStackTrace();

                return -1;

            }
        }
    }

    public int delete(Long seq) throws SQLException{

        String query = "DELETE FROM replys WHERE seq = ?";

        synchronized (ReplyDao.class){

        if(deleteRelation(seq) > 0) {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
                log.info("reply delete conn : " + this.conn);

                this.pstmt = preparedStatement;
                pstmt.setLong(1, seq);

                int result = pstmt.executeUpdate();

                return result;

            } catch (Exception e) {

                e.printStackTrace();

                return -1;

            }
        }
        }

        return -1;

    }

    public int deleteRelation(Long replySeq) throws SQLException{

        String query = "DELETE FROM posts_replys WHERE reply_seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query);)
        {
            log.info("delete relation conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setLong(1,replySeq);

            int result = pstmt.executeUpdate();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }
    }
}
