package com.monee.repository;

import com.monee.model.Account;

import java.awt.print.Pageable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class JdbcAccountRepository implements AccountRepository{

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String DB_URL = "jdbc:mysql://localhost:3307/monee";
    String DB_USER = "monee";
    String DB_PASSWORD = "1234";

    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public JdbcAccountRepository(){
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

    @Override
    public Optional<Account> findById(Long id) {

        String query = "SELECT * FROM ACCOUNT WHERE id = (?)";

        Account result = null;

        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setLong(1,id);

            rs = pstmt.executeQuery(query);

            while (rs.next()){
                result = new Account(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4));
            }

            return Optional.of(result);

        } catch (Exception e) {

            e.printStackTrace();

        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {

            }
        }

        return null;
    }

    @Override
    public List<Account> findAll() {
        return null;
    }

    @Override
    public List<Account> findAll(Pageable pageable) {
        return null;
    }
}
