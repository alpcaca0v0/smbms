package com.liang.dao.User;

import com.liang.dao.BaseDao;
import com.liang.pojo.Role;
import com.liang.pojo.User;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public User getLoginUser(Connection conn, String userCode) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;

        if(conn!=null){
            String sql = "select * from smbms_user where userCode=?;";
            Object[] params = {userCode};

            try {
                rs = BaseDao.execute(conn,pstm,rs,sql,params);
                if(rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }
                BaseDao.closeResources(null,pstm,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    //修改当前用户密码
    @Override
    public int updatePwd(Connection conn, int id, String passWord) throws SQLException {
        PreparedStatement pstm = null;
        int execute = 0;
        if(conn != null){
            String sql ="update smbms_user set userPassword=? where id=?;";
            Object params[] = {passWord, id};

            execute = BaseDao.execute(conn, pstm, sql, params);
            BaseDao.closeResources(null,pstm,null);
        }

        return execute;
    }

    @Override
    public int getUserCount(Connection conn, String userName, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if(conn != null){
            StringBuffer sql = new StringBuffer(); //为了下面能够判断条件之后拼接而采用的类型
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole=r.id");
            ArrayList<Object> list = new ArrayList<>();//存放参数

            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }

            if(userRole > 0){
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }

            Object[] params = list.toArray();
            System.out.println(sql.toString()); //最后完整的sql语句

            rs = BaseDao.execute(conn,pstm,rs,sql.toString(),params);

            if(rs.next()){
                count = rs.getInt("count"); //从结果中获最终数量
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection conn, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        if(conn != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if( !StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+ userName + "%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            //数据库中分页使用 limit startIndex, pageSize; 总数
            //0,5  1  0   01234
            //5,5  2  5   56789
            //10,5 3  10  10~
            sql.append(" order by creationDate DESC limit ?,?");
            //当前页在第几个数据
            currentPageNo = (currentPageNo -1 )* pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println(sql.toString()); //完整的sql语句
            rs = BaseDao.execute(conn,pstm,rs,sql.toString(),params);
            while(rs.next()){
                User resultUser = new User();
                resultUser.setId(rs.getInt("id"));
                resultUser.setUserCode(rs.getString("userCode"));
                resultUser.setUserName(rs.getString("userName"));
                resultUser.setGender(rs.getInt("gender"));
                resultUser.setBirthday(rs.getDate("birthday"));
                resultUser.setPhone(rs.getString("phone"));
                resultUser.setUserRole(rs.getInt("userRole"));
                resultUser.setUserRoleName(rs.getString("userRoleName"));
                userList.add(resultUser);
            }
            BaseDao.closeResources(null,pstm,rs);
        }
        return userList;
    }
}
