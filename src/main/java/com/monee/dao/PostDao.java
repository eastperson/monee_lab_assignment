package com.monee.dao;

import com.google.protobuf.Empty;
import com.monee.model.Account;
import com.monee.model.Post;

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
import java.util.stream.Collectors;

import static com.monee.utils.DateTimeUtils.dateTimeOf;
import static com.monee.utils.DateTimeUtils.timestampOf;
import static org.junit.jupiter.api.Assertions.fail;

public class PostDao {

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

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setReplyDao(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    private AccountDao accountDao;
    private ReplyDao replyDao;

    public PostDao(){
        try(Connection conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD)){
            this.conn = conn;
            System.out.println("connection 생성 : "+conn);
        }catch(Exception e) {
            fail(e.getMessage());
        }
    }

    public PostDao(AccountDao accountDao, ReplyDao replyDao){
        try(Connection conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD)){
            this.conn = conn;
            System.out.println("connection 생성 : "+conn);
        }catch(Exception e) {
            fail(e.getMessage());
        }
        this.accountDao = accountDao;
        this.replyDao = replyDao;
    }

    public Optional<Post> findById(Long seq) throws SQLException {

        String query = "SELECT * FROM posts WHERE seq =?";

        Post post = null;

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;
            System.out.println("find by seq conn : " + this.conn);

            pstmt.setLong(1,seq);
            System.out.println("pass");

            rs = pstmt.executeQuery();

            while (rs.next()){
                post = new Post(rs.getString("title"),rs.getString("content"));
                post.setRevwCnt(rs.getLong("revw_cnt"));
                post.setSeq(rs.getLong("seq"));
                post.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                post.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
            }

            return Optional.of(post);

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }
    }

    public Optional<Post> findByIdWithAccount(Long seq) throws SQLException {

        String query = "SELECT * FROM posts WHERE seq =?";

        Post post = null;

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;
            System.out.println("find by seq conn : " + this.conn);

            pstmt.setLong(1,seq);
            System.out.println("pass");

            rs = pstmt.executeQuery();

            while (rs.next()){
                post = new Post(rs.getString("title"),rs.getString("content"));
                post.setSeq(rs.getLong("seq"));
                post.setRevwCnt(rs.getLong("revw_cnt"));
                post.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                post.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                post.setAuthor(accountDao.findById(rs.getLong("author_seq")).get());
            }

            return Optional.of(post);

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }
    }

    public Optional<Post> findByIdWithReolyList(Long seq) throws SQLException {

        String query = "SELECT * FROM posts WHERE seq =?";

        Post post = null;

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;
            System.out.println("find by seq conn : " + this.conn);

            pstmt.setLong(1,seq);
            System.out.println("pass");

            rs = pstmt.executeQuery();

            while (rs.next()){
                post = new Post(rs.getString("title"),rs.getString("content"));
                post.setSeq(rs.getLong("seq"));
                post.setRevwCnt(rs.getLong("revw_cnt"));
                post.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                post.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                post.setReplyList(replyDao.findByPostId(post.getSeq()));
            }

            return Optional.of(post);

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }
    }

    public Optional<Post> findByIdWithReplys(Long seq) throws SQLException {

        return Optional.empty();
    }

    public List<Post> findAll() throws SQLException {

        String query = "SELECT * FROM posts";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;

            System.out.println("find all conn : " + this.conn);

            System.out.println("pass");

            rs = pstmt.executeQuery();

            List<Post> list = new ArrayList<>();

            while (rs.next()){
                Post post = new Post(rs.getString("title"),rs.getString("content"));
                post.setRevwCnt(rs.getLong("revw_cnt"));
                post.setSeq(rs.getLong("seq"));
                post.setCreateAt(dateTimeOf(rs.getTimestamp("create_at")));
                post.setUpdateAt(dateTimeOf(rs.getTimestamp("update_at")));
                list.add(post);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;

        }
    }

    public Optional<Post> save(Long accountSeq,Post post) throws SQLException {

        String query = "INSERT INTO posts (title,content,author_seq) VALUES(?,?,?);";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setString(1,post.getTitle());
            pstmt.setString(2,post.getContent());
            pstmt.setLong(3,accountSeq);
            System.out.println("pass");
            System.out.println("meta data : "+pstmt.getParameterMetaData());

            int result = pstmt.executeUpdate();
            System.out.println("저장한 개수 " + result);

            rs = pstmt.getGeneratedKeys();

            Optional<Post> newPost = null;

            if(rs.next()){
                Long key = rs.getLong(1);
                System.out.println(key);
                newPost = findById(key);
            }

            return newPost;

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }
    }

    public int updateTitle(Long seq, String title) throws SQLException {

        String query = "UPDATE posts SET title = (?), update_at = (?) WHERE seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setString(1,title);
            pstmt.setTimestamp(2, timestampOf(LocalDateTime.now()));
            pstmt.setLong(3,seq);

            System.out.println("pass");

            int result = pstmt.executeUpdate();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }
    }

    public int updateContent(Long seq, String content) throws SQLException{

        String query = "UPDATE posts SET content = (?), update_at = (?) WHERE seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setString(1,content);
            pstmt.setTimestamp(2, timestampOf(LocalDateTime.now()));
            pstmt.setLong(3,seq);

            System.out.println("pass");

            int result = pstmt.executeUpdate();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }
    }

    public int delete(Long seq) throws SQLException{

        String query = "DELETE FROM posts WHERE seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setLong(1,seq);

            System.out.println("pass");

            int result = pstmt.executeUpdate();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }
    }

    public int addRevwCnt(Long seq) throws SQLException{

        String query = "UPDATE posts SET revw_cnt = revw_cnt + 1 WHERE seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setLong(1,seq);

            System.out.println("pass");

            int result = pstmt.executeUpdate();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }
    }

}
