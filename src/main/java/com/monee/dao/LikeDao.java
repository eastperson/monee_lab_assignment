package com.monee.dao;

import com.monee.model.Reply;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.monee.utils.DateTimeUtils.dateTimeOf;

public class LikeDao {

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


    public boolean add(Long accountSeq, Long postSeq) {

        String query = "INSERT INTO likes (account_seq,post_seq) VALUES(?,?);";

            try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query);)
            {
                this.conn = connection;
                this.pstmt = preparedStatement;
                System.out.println("find by seq conn : " + this.conn);

                pstmt.setLong(1,accountSeq);
                pstmt.setLong(2,postSeq);
                System.out.println("pass");

                int rs = pstmt.executeUpdate();

                return rs == 1;

            } catch (Exception e) {

                e.printStackTrace();
                return false;
            }
    }

    public boolean remove(Long accountSeq, Long postSeq) {

        String query = "DELETE FROM likes WHERE account_seq = (?) AND post_seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;
            System.out.println("find by seq conn : " + this.conn);

            pstmt.setLong(1,accountSeq);
            pstmt.setLong(2,postSeq);
            System.out.println("pass");

            int rs = pstmt.executeUpdate();

            return rs == 1;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean isLikedPost(Long accountSeq, Long postSeq) {

        String query = "SELECT * FROM likes WHERE account_seq = (?) AND post_seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;
            System.out.println("find by seq conn : " + this.conn);

            pstmt.setLong(1,accountSeq);
            pstmt.setLong(2,postSeq);
            System.out.println("pass");

            ResultSet rs = pstmt.executeQuery();

            if(!rs.next()){
                return false;
            }

            return rs.getString("account_seq") != null && rs.getString("post_seq") != null;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}
