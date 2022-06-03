package com.xiao.service.user;

import com.xiao.dao.BaseDao;
import com.xiao.dao.user.UserDao;
import com.xiao.dao.user.UserDaoImpl;
import com.xiao.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    /**
     * 查询记录数
     * @param username
     * @param userRole
     * @return
     */
    @Override
    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection,username,userRole);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        return count;
    }

    /**
     * 根据条件查询用户
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;

        System.out.println("queryUserName------>"+queryUserName);
        System.out.println("queryUserRole------>"+queryUserRole);
        System.out.println("currentPageNo------>"+currentPageNo);
        System.out.println("pageSize------>"+pageSize);
        try{
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection,queryUserName,queryUserRole,currentPageNo,pageSize);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userList;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login("admin", "123");
        System.out.println(user.getUserName());
    }
    @Test
    public void test2(){
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount("null", 2);
        System.out.println(userCount);
    }
    @Test
    public void test3(){
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = userService.getUserList("李明", 2, 1, 1);
        System.out.println(userList);
    }

}
