package com.liang.service.role;

import com.liang.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    //获取角色列表
    public List<Role> getRoleList();
}
