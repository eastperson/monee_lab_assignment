package com.monee.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    public static void main(String... args) throws Exception {

        // JDBC 드라이버 Load
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Connection 객체 생성
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/monee","monee","1234");

        // PreparedStatement 객체 생성
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String query = "select now();";

        String current = "";

        try {
            pstmt = conn.prepareStatement(query);

            rs = pstmt.executeQuery(query);

            while (rs.next()){
                current = rs.getString(1);
            }

            System.out.println("current time : "+current);

        } catch (Exception e) {

            e.printStackTrace();

        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {

            }
        }
    }
}