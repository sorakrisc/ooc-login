package io.muic.ooc.webapp;

import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionManager {

    enum TestTableColumns {
        username, password;
    }

//    private final String jdbcDriverStr;
//    private final String jdbcURL;

    private Connection connection;
    private Statement statement;
    public ResultSet resultSet;
    private String databaseURL = "jdbc:mysql://localhost:3306/LoginCloudDB";
    private String databaseUsername = "csorakris";
    private String databasePassword = "Login123";
    public ConnectionManager() {
        connection = connectToDB();
    }
    public Connection connectToDB(){
        try {
            return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        }
        catch (SQLException err){
            System.out.println(err);
        }
        return null;
    }

    public boolean checkLogin(String username, String password) throws Exception {
        boolean not_pass = false;
        Password hashSalt = new Password();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM LoginCloudDB.user");
            while(resultSet.next()){
                if(StringUtils.equals(resultSet.getString("username"),username) &&
                        hashSalt.checkPassword(password,resultSet.getString("password"))){
                    not_pass = true;
                }
            }
        } finally {
            close();
            return not_pass;
        }
    }

    public void deleteRow(String name) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM LoginCloudDB.user WHERE username = ?");
            st.setString(1,name);
            st.executeUpdate();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void updateColumn(String username, String columnToUpdate, String valueToUpdate ) throws SQLException{
        String query = "UPDATE LoginCloudDB.user SET "+columnToUpdate+" = ? WHERE username = ?";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, valueToUpdate);
        preparedStmt.setString(2, username);
        preparedStmt.executeUpdate();
        connection.close();
    }
    public void updateRow( String id,String username, String fname, String lname) {
        try {
            String query = "UPDATE LoginCloudDB.user SET username = ?, firstname = ?, lastname = ? WHERE username = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, fname);
            preparedStmt.setString(3, lname);
            preparedStmt.setString(4, id);

            // execute the java preparedstatement
            preparedStmt.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserModel selectUserRowByUsername(String username){
        String query = "SELECT * FROM LoginCloudDB.user WHERE username = '"+username+"'";
        try {
            // create the java statement
            Statement st = connection.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt("userid");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String status  = rs.getString("status");
                String password = rs.getString("password");
                UserModel user = new UserModel();
                user.setFirstname(firstName);
                user.setLastname(lastName);
                user.setStatus(status);
                user.setPassword(password);
                user.setUsername(username);
                user.setUserid(id);
                return user;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Set<UserModel> selectUser(){
        Set<UserModel> ret = new HashSet<>();
        String query = "SELECT * FROM LoginCloudDB.user";
        try {
            // create the java statement
            Statement st = connection.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt("userid");
                String username = rs.getString("username");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String status  = rs.getString("status");
                String password = rs.getString("password");
                UserModel user = new UserModel();
                user.setFirstname(firstName);
                user.setLastname(lastName);
                user.setStatus(status);
                user.setPassword(password);
                user.setUsername(username);
                user.setUserid(id);
                ret.add(user);
            }
            return ret;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean addRow(String username, String password, String fname, String lname) {
        try {
            // create a mysql database connection
//            Class.forName(jdbcDriverStr);
            Password hashSalt = new Password();
            // note that i'm leaving "date_created" out of this insert statement
            String query = "INSERT INTO LoginCloudDB.user (username, password, firstname, lastname, status) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,hashSalt.hashPassword(password));
            preparedStatement.setString(3,fname);
            preparedStatement.setString(4,lname);
            preparedStatement.setString(5,"logout");
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            return false;
        }

    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
        }
    }
}