package com.xiao.servlet;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.xiao.pojo.Role;
import com.xiao.pojo.User;
import com.xiao.service.role.RoleServiceImpl;
import com.xiao.service.user.UserServiceImpl;
import com.xiao.util.Constants;
import com.xiao.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现Servlet复用
 *      实现复用，需要提取出来方法
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");//接收前端传来的method（pwdmodify.jsp）

        /**
         * 这块有点隐患！！！！
         */
        //先验证旧密码
        if (method.equals("pwdmodify") && method != null){
            this.pwdModify(req,resp);
        }
        //后更新密码
        if (method.equals("savepwd") && method != null){
            this.updatePwd(req,resp);
        }

        if (method.equals("query") && method != null){
            this.query(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //重点、难点

    /**
     * 用户显示的Servlet
     * @param req
     * @param resp
     *
     * 1 获取用户前端你的数据（查询）
     * 2 判断请求是否需要执行，看参数的值判断
     * 3 为了实现分页，需要计算出当前页面和总页面，页面大小……
     * 4 用户列表展示
     * 5 返回前端
     */
    public void query(HttpServletRequest req,HttpServletResponse resp){
        //查询用户列表
        //从前端 获取 数据----->userlist.jsp
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;

        //第一次走这个请求,一定是第一页,页面大小是固定的
        int pageSize = 5;//可以把这个写在配置文件里,方便后期修改
        int currentPageNo = 1;

        if (queryUserName == null){
            queryUserName = "";
        }
        if (temp!=null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp); //给查询赋值！ 0，1，2，3
        }
        if (pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户的总数 (分页: 上一页, 下一页的情况)
        int totalCount = userService.getUserCount(queryUserName,queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = ((int)(totalCount/pageSize))+1; // 总共有几页

        //控制首页和尾页
        //如果页面要小于1了,就显示第一页的东西
        if (currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){ //当前页面大于了最后一页
            currentPageNo = totalPageCount;
        }

        // 获取用户列表展示
        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
        req.setAttribute("userList",userList);
        // 获取角色列表展示
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改密码
     * @param req
     * @param resp
     * @return
     * @throws IOException
     */
    public boolean updatePwd(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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

                /*
                    修改成功了，咱们就是说，赶紧让他去登录页面，别老隔这呆着
                 */
//                resp.sendRedirect("login.jsp");
//                req.getRequestDispatcher("login.jsp").forward(req, resp);//500 错误 无法转发

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
            /*
                我的本意呢是想要在修改完密码，直接跳转到登录页，但是会被拦截到error.jsp页面。这个问题往后留留，我先继续完善其他功能把。今天耽误的时间太多了！！！6.2
             */
//            resp.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 验证旧密码，session中有用户的密码
     * @param req
     * @param resp
     */
    public void pwdModify(HttpServletRequest req,HttpServletResponse resp){

        //从Session里面拿ID
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        //session中有用户的密码，从里面取旧密码，与前端传递过来的参数做对比
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map: 结果集（使用map封装数据，一切的东西都可以使用map去保存）
        Map<String,String> resultMap = new HashMap<String,String>();

        if (o==null){//Session失效了，Session过期了
            resultMap.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){//输入的密码为空
            resultMap.put("result","error");
        }else {
            String userPassword = ((User)o).getUserPassword();//Session中用户的密码
            if (oldpassword.equals(userPassword)){//旧密码输入正确
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }

        //因为前端使用json，需要将map转换为json格式，让前端接收
        try {
            //设置返回类型
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray 阿里巴巴的JSON工具类，转换格式
            /*
            resultMap = ["result","sessionerror","result","error"]
            Json格式 = {key:value}
             */
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            //关闭流操作
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
