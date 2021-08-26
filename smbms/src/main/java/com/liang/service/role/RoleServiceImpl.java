package com.liang.service.role;

import com.liang.dao.BaseDao;
import com.liang.dao.Role.RoleDao;
import com.liang.dao.Role.RoleDaoImpl;
import com.liang.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    //引入dao
    private RoleDao roleDao;
    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList(){
        Connection conn = null;
        List<Role> roleList = new ArrayList();

        try {
            conn = BaseDao.openConnection();
            roleList= roleDao.getRoleList(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResources(conn,null,null);
        }
        return roleList;
    }
}
