package com.gcos.student_plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener{

    Button bt_Register;
    EditText et_Name,et_Email,et_Username,et_Password;
    Toolbar tb_Toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_Name = (EditText)findViewById(R.id.et_Name);
        et_Email = (EditText)findViewById(R.id.et_Email);
        et_Username = (EditText)findViewById(R.id.et_Usename);
        et_Password = (EditText)findViewById(R.id.et_Password);
        bt_Register = (Button)findViewById(R.id.bt_Register);

        tb_Toolbar = (Toolbar)findViewById(R.id.register_toolbar);
        tb_Toolbar.setTitle("Register");

        bt_Register.setOnClickListener(this);
    }

    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_Register:

                String name = et_Name.getText().toString();
                String email = et_Email.getText().toString();
                String username = et_Username.getText().toString();
                String password = et_Password.getText().toString();

                User user = new User(name,email,username,password);

                registerUser(user);

                break;
        }
    }
}
