package com.xiao.dao.user;

import com.mysql.cj.util.StringUtils;
import com.xiao.dao.BaseDao;
import com.xiao.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;

        //判断数据库是否连接了
        if (connection != null){
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};

            rs = BaseDao.execute(connection,pstm,rs,sql,params);//得到一个结果集

            if (rs.next()){
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
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            //关闭资源
            BaseDao.closeResource(null,pstm,rs);

        }

        return user;

    }

    /**
     * 修改密码
     * @param connection
     * @param id
     * @param password
     * @return
     * @throws SQLException
     */
    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {

        PreparedStatement pstm = null;
        int execute = 0;//原本直接在if语句里面int，但因为要返回其值，所以我们提升了他的作用域
        if (connection!=null){
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object params[] = {password,id};
            execute = BaseDao.execute(connection,pstm,sql,params);
            BaseDao.closeResource(null,pstm,null);
        }
        return execute;
    }

    /**
     * 根据 用户名或者角色 查询用户总数【最难理解的SQL】
     * @param connection
     * @param username
     * @param userRole
     * @return
     * @throws SQLException
     */
    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;

        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();//存放我们的参数

            if (!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");//index:0
            }
            if (userRole>0){
                sql.append(" and u.userName like ?");
                list.add(userRole);//index:1
            }

            //怎么将list转换成数组
            Object[] params = list.toArray();

            System.out.printf("UserDaoImpl->getUserCount:"+sql.toString());//输出最后完整的SQL语句

            rs = BaseDao.execute(connection, pstm,rs,sql.toString(), params);

            if (rs.next()){
                count = rs.getInt("count");//从结果集中获取最终的数量
            }
            BaseDao.closeResource(null,pstm,rs);
        }

        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        if (connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if (userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            /**
             * 在数据库中  分页使用  limit startIndex，pageSize; 总数
             * 当前页   (当前页-1)*页面大小
             * 0,5      1   0   01234
             * 5,5      2   5   56789
             * 10,5     3   10
             */

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql------>"+sql.toString());
            rs = BaseDao.execute(connection,pstm,rs,sql.toString(),params);
            while (rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return userList;
    }
}
