package com.gcos.student_plus;

import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by Mazokie on 7/10/2558.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){

        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);

    }

    public void storeUserData(User user){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name",user.name);
        spEditor.putString("email",user.email);
        spEditor.putString("username",user.username);
        spEditor.putString("password",user.password);
        spEditor.commit();

    }

    public String getUserName() {

        String userName = userLocalDatabase.getString("name", "name");
        return userName;
    }

    public User getLoggedInUser(){

        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email","");
        String username = userLocalDatabase.getString("username","");
        String password = userLocalDatabase.getString("password","");

        User storedUser = new User(name,email,username,password);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn",loggedIn);
        spEditor.commit();

    }

    public boolean getUserLoggedIn(){

        if(userLocalDatabase.getBoolean("LoggedIn", false) == true){
            return true;
        }

        else {
            return false;
        }

    }

    public void clearUserData(){

        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();

    }

}
