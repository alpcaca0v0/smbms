package com.liang.service.user;

import com.liang.pojo.Role;
import com.liang.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //用户登录
    public User Login(String userCode, String password);

    //根据id修改当前用户密码
    public boolean updatePwd(int id, String passWord);

    //查询记录数
    public int getUserCount(String userName, int userRole);

    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryeUserRole, int currentPageNo, int pageSize);
}
