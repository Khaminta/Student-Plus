package com.gcos.student_plus;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button bt_Login;
    EditText et_Username,et_Password;
    TextView tv_Register;
    Toolbar tb_Toolbar;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_Username = (EditText)findViewById(R.id.et_Usename);
        et_Password = (EditText)findViewById(R.id.et_Password);
        bt_Login = (Button)findViewById(R.id.bt_Login);
        tv_Register = (TextView)findViewById(R.id.tv_Register);

        bt_Login.setOnClickListener(this);
        tv_Register.setOnClickListener(this);

        tb_Toolbar = (Toolbar)findViewById(R.id.login_toolbar);
        tb_Toolbar.setTitle("Sign In");

        userLocalStore = new UserLocalStore(this);
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser){

        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));

    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {

                    showErrorMessage();

                } else {

                    logUserIn(returnedUser);

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_Login:

                String usermane = et_Username.getText().toString();
                String password = et_Password.getText().toString();

                User user = new User(usermane, password);

                authenticate(user);

                userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);

                break;

            case R.id.tv_Register:

                startActivity(new Intent(this, Register.class));

                break;
        }
    }
}
