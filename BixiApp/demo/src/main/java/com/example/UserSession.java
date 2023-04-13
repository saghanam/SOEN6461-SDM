package com.example;

import java.util.*;

public final class UserSession {

    private static UserSession instance;

    private String userName;
    private String customerId;


    private UserSession(String userName,String customerId) {
        this.userName = userName;
        this.customerId = customerId;
    }

    public static UserSession getInstance(String userName,String customerId) {
        if(instance == null) {
            instance = new UserSession(userName,customerId);
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public String getCustomerId(){
        return customerId;
    }

    public void cleanUserSession() {
        instance = null;
        System.out.println("User Session is cleared");
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                "customerId='" + customerId + '\'' +
                '}';
    }
}
