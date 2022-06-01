package com.xiao.servlet;

import com.xiao.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销功能
 *
 *  思路：移除session，返回登录页面
 *  错误：登录过滤器一直没有生效
 *  错误代码：req.getSession().removeAttribute("Constants.USER_SESSION");
 *  原因：Constants.USER_SESSION是常量！不应该加 引号“”
 */
public class LoginoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //移除用户的Constants.USER_SESSION
        req.getSession().removeAttribute(Constants.USER_SESSION);
        //返回登录界面
        resp.sendRedirect(req.getContextPath()+"/login.jsp");

        System.out.println("LoginoutServlet已经走过！！！！！！！！！");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
