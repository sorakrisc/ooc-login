package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        System.out.println("doget adduser");
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/adduser.jsp");
        rd.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            securityService.addUserCredentials(username , password);
            response.sendRedirect("/");
        }
        else{
            response.sendRedirect("/login");
        }
    }
}
