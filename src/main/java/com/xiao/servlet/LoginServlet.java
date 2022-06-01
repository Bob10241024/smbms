package com.xiao.servlet;

import com.xiao.pojo.User;
import com.xiao.service.UserService;
import com.xiao.service.UserServiceImpl;
import com.xiao.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 登录
public class LoginServlet extends HttpServlet {
    //Servlet: 控制层， 调用业务层代码

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("LoginServlet--start....");

        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //和数据库中的账号跟密码进行对比
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);//这里已经把用户查出来了

        if (user != null){
            //将用户的信息放入到session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //将页面跳转到主页
            resp.sendRedirect("jsp/frame.jsp");//这块的地址在最前面不可以加 “/”
        }else{//查无此人
            //转发回登录界面，并且提示他输出错误
            req.setAttribute("error","用户名或者密码输入错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
