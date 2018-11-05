package com.example.a97586.mysqltest.Utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBUtils {
    private static final String TAG ="DBUtils";
    private static Connection getConnection(String dbname){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String ip ="127.0.0.1";
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql_first_test",
                    "root","mysql123456");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static HashMap<String,String> getUserInfoByName(String name){
        HashMap<String,String> map =new HashMap<>();
        Connection conn =getConnection("mysql_first_test");
        try {
            Statement st = conn.createStatement();
            String sql = "select * from user where name = '" + name + "'";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                res.next();
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnName(i);
                    map.put(field, res.getString(field));
                }
                conn.close();
                st.close();
                res.close();
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
    }
}
