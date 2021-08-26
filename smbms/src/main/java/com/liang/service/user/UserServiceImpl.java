package com.liang.service.user;

import com.liang.dao.BaseDao;
import com.liang.dao.User.UserDao;
import com.liang.dao.User.UserDaoImpl;
import com.liang.pojo.User;
import org.junit.Test;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    //业务层都会调用dao层,所以我们要引入dao层
    private UserDao userDao;

    public UserServiceImpl(){
        //new出来UserServiceImpl时,userDao就会被实例化直接使用
        userDao = new UserDaoImpl();
    }

    @Override
    public User Login(String userCode, String password) {
        Connection conn = null;
        User user = null;

        try {
            conn = BaseDao.openConnection();
            //通过业务层调用dao层,调用对应的数据库操作
            user = userDao.getLoginUser(conn,userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(conn,null,null);
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String passWord) {
        Connection conn = null;
        boolean flag = false;
        try {
            conn= BaseDao.openConnection();
            if(userDao.updatePwd(conn,id,passWord)>0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(conn,null,null);
        }
        return flag;
    }

    //调dao层
    @Override
    public int getUserCount(String userName, int userRole) {
        Connection conn = null;
        int count = 0;
        try {
            conn = BaseDao.openConnection();
            count = userDao.getUserCount(conn,userName,userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(conn,null,null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryeUserRole, int currentPageNo, int pageSize) {
        Connection conn = null;
        List<User> userList = new ArrayList();

        try {
            conn = BaseDao.openConnection();
            userList = userDao.getUserList(conn, queryUserName, queryeUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(conn,null,null);
        }
        return userList;
    }
}
