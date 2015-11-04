package com.gcos.student_plus;

/**
 * Created by Mazokie on 7/10/2558.
 */
public class User {
    String name,email,username,password;

    public User(String name, String email, String username, String password){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.name = "";
    }
}
