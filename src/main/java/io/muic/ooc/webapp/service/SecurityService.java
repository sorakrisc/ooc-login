/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import io.muic.ooc.webapp.ConnectionManager;
import io.muic.ooc.webapp.UserModel;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gigadot
 */
public class SecurityService {
    
    private Map<String, String> userCredentials = extractUserCredentialsFromDB();
    
    public boolean isAuthorized(HttpServletRequest request) {
        String username = (String) request.getSession()
                .getAttribute("username");
        // do checking
       return (username != null && userCredentials.containsKey(username));
    }
    public Map<String, String> extractUserCredentialsFromDB(){
        Map<String, String> map = new HashMap<>();

        Set<UserModel> userSet =new ConnectionManager().selectUser();
        for(UserModel u: userSet){
            map.put(u.getUsername(), u.getPassword());
        }
        return map;

    }
    public boolean authenticate(String username, String password, HttpServletRequest request) throws Exception{
        String passwordInLocal = userCredentials.get(username);
        boolean isMatched = new ConnectionManager().checkLogin(username, password);
//        boolean isMatched = StringUtils.equals(password, passwordInDB);
        if (isMatched) {
            request.getSession().setAttribute("username", username);
            return true;
        } else {
            return false;
        }
    }
    
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public void addUserCredentials(String username, String password){
        if(!getUserCredentialsUsername().contains(username)) {
            this.userCredentials.put(username, password);
        }
    }
    public Set<String> getUserCredentialsUsername(){
        return userCredentials.keySet();
    }

    public void removeUserCredentials(String username){
        this.userCredentials.remove(username);
    }
}
