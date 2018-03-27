package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.ConnectionManager;
import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.UserModel;
import io.muic.ooc.webapp.service.SecurityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class EditProfileServlet extends HttpServlet implements Routable {
    private SecurityService securityService;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if(authorized){
            boolean updated = false;
            if(request.getParameter("xusername")!=null){
                updated = updateProfileCriteria(request,response, "newusername", "username");
            }
            else if(request.getParameter("xfirstname")!=null){
                updated = updateProfileCriteria(request,response, "newfn", "firstname");
            }
            else if(request.getParameter("xlastname")!=null){
                updated = updateProfileCriteria(request,response, "newln", "lastname");
            }
            else if(request.getParameter("home")!=null){
                response.sendRedirect("/");
            }
            if(updated){
                String error = "selected field is edited.";
                request.setAttribute("error", error);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/editprofile.jsp");
                rd.include(request, response);
            }
            else{
                String error = "editing field is required.";
                request.setAttribute("error", error);
                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/editprofile.jsp");
                rd.include(request, response);
            }
        }
        else{
            response.sendRedirect("/login");
        }
    }
    boolean updateProfileCriteria(HttpServletRequest request, HttpServletResponse response, String textfieldName, String attrToUpdate) throws ServletException, IOException{
        String username = (String) request.getSession().getAttribute("username");
        String updateValue = request.getParameter(textfieldName);
        if(updateValue.length()>0){
            try {
                new ConnectionManager().updateColumn(username,attrToUpdate,updateValue);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String username = (String) request.getSession().getAttribute("username");
            UserModel user = new ConnectionManager().selectUserRowByUsername(username);
            request.setAttribute("fname", user.getFirstname());
            request.setAttribute("lname", user.getLastname());
            request.setAttribute("username", username);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/editprofile.jsp");
            rd.include(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    public String getMapping() {
        return "/editprofile";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
