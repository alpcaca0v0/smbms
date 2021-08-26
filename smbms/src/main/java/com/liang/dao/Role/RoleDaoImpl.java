package com.liang.dao.Role;

import com.liang.dao.BaseDao;
import com.liang.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    @Override
    public List<Role> getRoleList(Connection conn) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Object[] params = {};
        List<Role> userRoleList = new ArrayList();
        if(conn != null){
            String sql = "select * from smbms_role;";
            rs =  BaseDao.execute(conn,pstm,rs,sql,params);
            while(rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                userRoleList.add(role);
            }
        }
        BaseDao.closeResources(null,pstm,rs);
        return userRoleList;
    }
}
