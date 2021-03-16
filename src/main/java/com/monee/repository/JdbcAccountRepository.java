package com.monee.repository;

import com.google.protobuf.Empty;
import com.monee.model.Account;

import java.awt.print.Pageable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.monee.utils.DateTimeUtils.dateTimeOf;
import static org.junit.jupiter.api.Assertions.fail;

public class JdbcAccountRepository implements AccountRepository{

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
    public Optional<Account> findById(Long seq) throws SQLException {

        this.conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);

        //String query = "select now();";
        String query = "SELECT * FROM ACCOUNTS WHERE seq =?";

        ResultSet result = null;

        Account account = null;

        try {
            System.out.println("find by seq conn : " + this.conn);

            pstmt = this.conn.prepareStatement(query);
            pstmt.setLong(1,seq);
            System.out.println("pass");

            rs = pstmt.executeQuery();

            while (rs.next()){
                account = new Account(rs.getString(2),rs.getString(3),rs.getString(4));
                account.setSeq(rs.getLong(1));
                account.setCreateAt(dateTimeOf(rs.getTimestamp(5)));
            }

            return Optional.of(account);

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {

            }
        }
    }

    @Override
    public List<Account> findAll() throws SQLException {

        this.conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);

        String query = "SELECT * FROM ACCOUNTS";

        ResultSet result = null;

        Account account = null;

        try {
            System.out.println("find all conn : " + this.conn);

            pstmt = this.conn.prepareStatement(query);

            System.out.println("pass");

            rs = pstmt.executeQuery();

            List<Account> list = new ArrayList<>();

            while (rs.next()){
                account = new Account(rs.getString(2),rs.getString(3),rs.getString(4));
                account.setSeq(rs.getLong(1));
                account.setCreateAt(dateTimeOf(rs.getTimestamp(5)));
                list.add(account);
            }

            return list;

        } catch (Exception e) {

            e.printStackTrace();

            return Collections.EMPTY_LIST;

        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {

            }
        }
    }

    @Override
    public List<Account> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Account> save(Account account) throws SQLException {

        this.conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);

        //String query = "select now();";
        String query = "INSERT INTO accounts (email,nickname,password) VALUES(?,?,?);";


        try {
            System.out.println("save conn : " + this.conn);

            pstmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,account.getEmail());
            pstmt.setString(2,account.getNickname());
            pstmt.setString(3,account.getPassword());
            System.out.println("pass");
            System.out.println("meta data : "+pstmt.getParameterMetaData());

            int result = pstmt.executeUpdate();
            System.out.println("저장한 개수 " + result);

            rs = pstmt.getGeneratedKeys();

            Optional<Account> newAccount = null;

            if(rs.next()){
                Long key = rs.getLong(1);
                System.out.println(key);
                newAccount = findById(key);
            }

            return newAccount;

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {

            }
        }
    }
}
