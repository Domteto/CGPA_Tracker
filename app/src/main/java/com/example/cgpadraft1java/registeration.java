package com.example.cgpadraft1java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registeration extends AppCompatActivity {
    Button btnbtLogin, btnRegister;
    EditText etName, etEmail, etPassword;
    String tName, tEmail, tpassword;
    Users newUser;
    tmnDB tdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnbtLogin = findViewById(R.id.btnbtLogin);



        // Register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tName = etName.getText().toString().trim();
                tEmail = etEmail.getText().toString().trim();
                tpassword = etPassword.getText().toString().trim();

                if (tName.isEmpty() || tEmail.isEmpty() || tpassword.isEmpty()){
                    Toast.makeText(registeration.this, "Please input all the required details above", Toast.LENGTH_LONG).show();
                }
                else{
                    newUser = new Users(tName,tEmail,tpassword);
                    tdb = new tmnDB(registeration.this);
                    tdb.addUser(newUser);
                    Toast.makeText(registeration.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    Intent forward = new Intent(registeration.this, Login.class);
                    startActivity(forward);
                }
            }
        });

        //Back to Login Button
        btnbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forward = new Intent(registeration.this, Login.class);
                startActivity(forward);
            }
        });





    }
}