package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.ConnectionManager;
import io.muic.ooc.webapp.Password;
import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserServlet extends HttpServlet implements Routable {
    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/adduser";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/adduser.jsp");
            rd.include(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fname = request.getParameter("firstname");
            String lname = request.getParameter("lastname");



            ConnectionManager cm = new ConnectionManager();
            if(cm.addRow(username, password, fname, lname)){
                String hashedPassword = new ConnectionManager().selectUserRowByUsername(username).getPassword();
                securityService.addUserCredentials(username , hashedPassword);
                response.sendRedirect("/");
            }
            else{
                String error = "There is something wrong try changing username.";
                request.setAttribute("error", error);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/adduser.jsp");
                rd.include(request, response);
            }


        }
        else{
            response.sendRedirect("/login");
        }
    }
}
