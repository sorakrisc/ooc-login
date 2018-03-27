package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.ConnectionManager;
import io.muic.ooc.webapp.service.SecurityService;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import io.muic.ooc.webapp.Routable;

public class LoginServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doget login");
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
        rd.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // do login post logic
        // extract username and password from request
        String username = request.getParameter("username");
        String password = request.getParameter("password");
            if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {
                try {
                    if (securityService.authenticate(username, password, request)) {
                        try {
                            new ConnectionManager().updateColumn(username, "status","login");
                            response.sendRedirect("/");
                        }catch (SQLException err) {
                            System.out.println(err);
                            String error = "Login request did not go through try again later.";
                            request.setAttribute("error", error);
                            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
                            rd.include(request, response);
                        }

                    } else {
                        String error = "Wrong username or password.";
                        request.setAttribute("error", error);
                        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
                        rd.include(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String error = "Username or password is missing.";
                request.setAttribute("error", error);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
                rd.include(request, response);
            }

        // check username and password against database
        // if valid then set username attribute to session via securityService
        // else put error message to render error on the login form

    }

    @Override
    public String getMapping() {
        return "/login";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
