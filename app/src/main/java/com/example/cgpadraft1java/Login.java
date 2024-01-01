package com.example.cgpadraft1java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText etEmail, etPassword;
    String tEmail, tpassword;
    boolean isVaild;
    tmnDB tdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tEmail = etEmail.getText().toString();
                tpassword = etPassword.getText().toString();
                tdb = new tmnDB(Login.this);
                isVaild = tdb.checkUser(tEmail,tpassword);
                if (isVaild){
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                    Intent go = new Intent(Login.this, MainActivity.class);
                    startActivity(go);
                }
                else {
                    Toast.makeText(Login.this, "User Doesnt Exist. Check details or Register to Login", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, registeration.class);
                startActivity(intent);
            }
        });


    }
}