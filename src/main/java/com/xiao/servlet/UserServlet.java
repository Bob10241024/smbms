package com.xiao.servlet;

import com.mysql.cj.util.StringUtils;
import com.xiao.pojo.User;
import com.xiao.service.UserServiceImpl;
import com.xiao.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现Servlet复用
 *      实现复用，需要提取出来方法
 */
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("UserServlet: doGet");
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null){
            this.updatePwd(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //形成方法
    public boolean updatePwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("UserServlet: updatePwd");
        //从Session里面拿用户ID
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");//获取新密码（前端传过来的）

        boolean flag = false;

        if (o != null && !StringUtils.isNullOrEmpty(newpassword)){
            UserServiceImpl userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(),newpassword);

            System.out.println("用户ID："+((User)o).getId());

            if (flag){//flag为true时
                req.setAttribute("message","修改密码成功，请退出，使用新密码登录");
                System.out.println("修改密码成功");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","修改密码失败");
                System.out.println("修改密码失败");
            }
        }else{
            req.setAttribute("message","新密码有问题");
            System.out.println("新密码有问题");
        }

        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
