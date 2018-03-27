/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.ConnectionManager;
import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.UserModel;
import io.muic.ooc.webapp.service.SecurityService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gigadot
 */
public class HomeServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/index.jsp";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(authorized) {
            if(request.getParameter("addUserButton")!=null) {
                response.sendRedirect("/adduser");
            }
            else if(request.getParameter("logout")!=null){
                String username = (String) request.getSession().getAttribute("username");
                try {
                    new ConnectionManager().updateColumn(username,"status","logout");
                    response.sendRedirect("/login");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(request.getParameter("editProfile")!=null){
                response.sendRedirect("/editprofile");
            }
            else {
                String userToRemove = request.getParameterNames().nextElement();
                if(!userToRemove.equals(request.getSession().getAttribute("username").toString())) {
                    securityService.removeUserCredentials(userToRemove);
                    new ConnectionManager().deleteRow(userToRemove);
                }

                response.sendRedirect("/");

            }
        }
        else {
            response.sendRedirect("/login");
        }
    }
    public Map<String, String> getUsernameStatus(Set<UserModel> setUsers){
        Map<String, String> ret = new HashMap<>();
        for (UserModel u : setUsers){
            ret.put(u.getUsername(), u.getStatus());
        }
        return ret;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            // do MVC in here
            String username = (String) request.getSession().getAttribute("username");
            Map<String, String> usernameStatusMap = getUsernameStatus(new ConnectionManager().selectUser());
            request.setAttribute("username", username);
            request.setAttribute("usernameStatusMap", usernameStatusMap);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/home.jsp");
            rd.include(request, response);
        } else {
            response.sendRedirect("/login");
        }
    }
}
