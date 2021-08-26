package com.liang.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {
    private static String driver;
    private static String username;
    private static String password;
    private static String url;

    static{
        Properties properties = new Properties();
        //通过类加载器读取对应资源
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        url = properties.getProperty("url");
    }
    //获取数据库连接
    public static Connection openConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //编写查询方法
    public static ResultSet execute(Connection conn,
                                    PreparedStatement preparedStatement,
                                    ResultSet resultSet,
                                    String sql,
                                    Object[] params
                                    ) throws SQLException {
        preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i< params.length;i++) {
            //给前面的查询语句(?,?,?,?....)赋值
            preparedStatement.setObject(i+1,params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //编写增删改
    public static int execute(Connection conn,
                                    PreparedStatement preparedStatement,
                                    String sql,
                                    Object[] params
    ) throws SQLException {
        preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i< params.length;i++) {
            //给前面的语句(?,?,?,?....)赋值
            preparedStatement.setObject(i+1,params[i]);
        }
        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    public static boolean closeResources(Connection conn,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag = true;
        if(conn != null){
            try {
                conn.close();
                //gc回收
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if(preparedStatement != null){
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if(resultSet != null){
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        return flag;
    }

}

