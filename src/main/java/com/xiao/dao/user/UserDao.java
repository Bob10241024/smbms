package com.xiao.dao.user;

import com.xiao.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode) throws SQLException;

    //修改当前用户密码
    public int updatePwd(Connection connection,int id,String password) throws SQLException;

    //根据 用户名或者角色 查询用户总数
    //select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id;
    public int getUserCount(Connection connection,String username,int userRole) throws SQLException;

    //获取用户列表  通过条件查询-userList
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;
}
