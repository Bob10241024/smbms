package com.xiao.service.user;

import com.xiao.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode, String password);

    //修改用户ID修改密码
    public boolean updatePwd(int id, String password);//这里用boolean是因为，在业务里面，功能要么成功要么失败。

    //查询记录数
    public int getUserCount(String username,int userRole);

    //根据条件查询用户
    public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo,int pageSize);
}
