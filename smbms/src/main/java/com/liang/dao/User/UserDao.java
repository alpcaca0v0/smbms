package com.liang.dao.User;

import com.liang.pojo.Role;
import com.liang.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到要登录的用户
    public User getLoginUser(Connection conn, String useCode);

    //修改当前用户密码
    public int updatePwd(Connection conn, int id, String passWord) throws SQLException;

    //根据用户名或角色查询用户总数
    public int getUserCount(Connection conn,String userName, int userRole) throws SQLException;

    //通过条件查询userlist
    public List<User> getUserList(Connection conn, String userName,
                                  int userRole, int currentPageNo, int pageSize)throws Exception;

}
