package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText lEmail, lPass;
    Button loginbtn;
    TextView lCreate;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        lEmail = findViewById(R.id.txtRegEmail);
        lPass = findViewById(R.id.txtRegPass);
        loginbtn = findViewById(R.id.btnLogin);
        lCreate = findViewById(R.id.txtCreate);

        fAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = lEmail.getText().toString().trim();
                String password = lPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    lEmail.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    lPass.setError("Password is required.");
                    return;
                }

                if(password.length() < 6 ) {

                    lEmail.setError("Password must be 6 characters long.");
                    return;

                }


                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));


                        }

                        else{

                            Toast.makeText(Login.this, "Some Error Occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        lCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }
}