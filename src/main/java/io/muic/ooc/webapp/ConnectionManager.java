package io.muic.ooc.webapp;

import org.apache.commons.lang.StringUtils;

import java.sql.*;

public class ConnectionManager {

    enum TestTableColumns {
        username, password;
    }

//    private final String jdbcDriverStr;
//    private final String jdbcURL;

    private static Connection connection;
    private static Statement statement;
    public static ResultSet resultSet;
    private static String databaseURL = "jdbc:mysql://localhost:3306/LoginDB";
    private static String databaseUsername = "root";
    private static String databasePassword = "MSMDatabase";
    public ConnectionManager() {

    }
    public static Connection connectToDB(){
        try {
            return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        }
        catch (SQLException err){
            System.out.println(err);
        }
        return null;
    }

    public static  boolean checkLogin(String username, String password) throws Exception {
        boolean not_pass = false;
        Password hashSalt = new Password();
        try {
            //Class.forName(jdbcDriverStr);
            connection = connectToDB();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM LoginDB.user");
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

    public void DeleteRow(String name) {
        try {
//            Class.forName(jdbcDriverStr);
            connection = connectToDB();
            PreparedStatement st = connection.prepareStatement("DELETE FROM LoginDB.user WHERE username = ?");
            st.setString(1,name);
            st.executeUpdate();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void UpdateRow( String id,String username, String fname, String lname) {
        try {
//            Class.forName(jdbcDriverStr);
            connection = connectToDB();
            String query = "UPDATE LoginDB.user SET username = ?, firstname = ?, lastname = ? WHERE username = ?";
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

    public void AddRow(String username, String password, String fname, String lname) {
        try {
            // create a mysql database connection
//            Class.forName(jdbcDriverStr);
            Password hashSalt = new Password();
            connection = connectToDB();
            // note that i'm leaving "date_created" out of this insert statement
            String query = "INSERT INTO LoginDB.user (username, password, firstname, lastname, status) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,hashSalt.hashPassword(password));
            preparedStatement.setString(3,fname);
            preparedStatement.setString(4,lname);
            preparedStatement.setString(5,"logout");
            preparedStatement.executeUpdate();
            connection.close();

        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }

    private static void close() {
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