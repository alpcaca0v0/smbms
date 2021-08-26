package com.liang.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.liang.pojo.Role;
import com.liang.pojo.User;
import com.liang.service.role.RoleServiceImpl;
import com.liang.service.user.UserService;
import com.liang.service.user.UserServiceImpl;
import com.liang.util.Constants;
import com.liang.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd") && method != null){
            this.updatePwd(req,resp);
        }else if(method.equals("pwdmodify") && method !=null){
            this.pwdModify(req,resp);
        }else if(method.equals("query") && method != null){
            this.query(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从sessino拿用户id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newPassword = req.getParameter("newpassword");

        boolean flag = false;

        if(o != null && newPassword != null){

            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) o).getId(),newPassword);
            if(flag){
                req.setAttribute("message","密码修改成功,重新登录");
                //移除当前session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","密码修改失败");
            }
        }else{
            req.setAttribute("message","密码修改有问题");
        }
        resp.sendRedirect(req.getContextPath()+"/jsp/pwdmodify.jsp");
        //req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        //这个会报错500:提交响应后无法转发,但上面的方法会立刻跳转
    }

    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        HashMap<String, String> resultMap = new HashMap<>();
        if(o == null){ //session失效
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result","error");
        }else{
            String userPassword = ((User) o).getUserPassword();
            if(userPassword.equals(oldpassword)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void query(HttpServletRequest req, HttpServletResponse resp){
        //从前端获取数据
        String queryname = req.getParameter("queryname");
        String userRole = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //第一次走这个请求,页面大小固定,且在第一页
        int currentPageNo = 1;
        int pageSize = 5;
        if(queryname == null){
            queryname = "";
        }
        if(userRole != null && !userRole.equals("")){
            queryUserRole = Integer.parseInt(userRole); //给查询赋值,0123
        }
        if(pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        int totalCount = userService.getUserCount(queryname, queryUserRole);
        List<User> userList = null;
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){ //第一页无法往前
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){ //最后一页无法向后
            currentPageNo = totalPageCount;
        }

        //获取用户列表
        userList = userService.getUserList(queryname,queryUserRole,currentPageNo,pageSize);
        req.setAttribute("userList",userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryname);
        req.setAttribute("queryUserRole",queryUserRole);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
