package com.monee.dao;

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

public class AccountDao{

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

    public AccountDao(){
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

    public Optional<Account> findById(Long seq){

        String query = "SELECT * FROM ACCOUNTS WHERE seq =?";

        Account account = null;

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;
            System.out.println("find by seq conn : " + this.conn);
            System.out.println("seq : " + seq);

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

        }
    }

    public List<Account> findAll() {

        String query = "SELECT * FROM ACCOUNTS";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            this.conn = connection;
            this.pstmt = preparedStatement;

            System.out.println("find all conn : " + this.conn);

            System.out.println("pass");

            rs = pstmt.executeQuery();

            List<Account> list = new ArrayList<>();

            while (rs.next()){
                Account account = new Account(rs.getString(2),rs.getString(3),rs.getString(4));
                account.setSeq(rs.getLong(1));
                account.setCreateAt(dateTimeOf(rs.getTimestamp(5)));
                list.add(account);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;

        }
    }

    public List<Account> findAll(Pageable pageable) {
        return null;
    }

    public Optional<Account> save(Account account) throws SQLException {

        String query = "INSERT INTO accounts (email,nickname,password) VALUES(?,?,?);";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
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

        }
    }

    public int updateNickname(Long seq, String nickname) throws SQLException {

        String query = "UPDATE accounts SET nickname = (?) WHERE seq = (?)";

        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            PreparedStatement preparedStatement= connection.prepareStatement(query);)
        {
            System.out.println("save conn : " + this.conn);

            this.pstmt = preparedStatement;
            pstmt.setString(1,nickname);
            pstmt.setLong(2,seq);

            System.out.println("pass");

            int result = pstmt.executeUpdate();

            return result;

        } catch (Exception e) {

            e.printStackTrace();

            return -1;

        }
    }
}
