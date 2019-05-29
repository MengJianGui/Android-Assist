package com.example.administrator.sunxiaohong2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectMysql {
    private static Connection conn;
    private static final String URL = "jdbc:mysql://10.20.4.164/mk1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mk143741";

    public static Connection getConn(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (ClassNotFoundException e){
            System.out.println("加载驱动程序出错");
        }catch(SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConn(){
        if(conn != null){
            try{
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
