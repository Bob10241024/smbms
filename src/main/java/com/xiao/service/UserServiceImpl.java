package com.xiao.service;

import com.xiao.dao.BaseDao;
import com.xiao.dao.user.UserDao;
import com.xiao.dao.user.UserDaoImpl;
import com.xiao.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{

    //业务层都会调用DAO层，所以我们要引入Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;

        connection = BaseDao.getConnection();
        try {
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    /**
     * 修改用户ID修改密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public boolean updatePwd(int id, String password) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection,id,password)>0){//操作成功
                flag = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        //否则返回 false
        return flag;//之前 写的是 false
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login("admin", "123");
        System.out.println(user.getUserName());
    }

}
