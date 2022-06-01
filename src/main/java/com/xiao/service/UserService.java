package com.xiao.service;

import com.xiao.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserService {
    //用户登录
    public User login(String userCode, String password);

    //修改用户ID修改密码
    public boolean updatePwd(int id, String password);//这里用boolean是因为，在业务里面，功能要么成功要么失败。
}
