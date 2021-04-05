/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Marlene
 */
public class User {
    private static String userId;
    private static String userName;
    private static String password;

    
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }
    
}

