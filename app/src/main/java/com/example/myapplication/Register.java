package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.ImplicitDirectBootViolation;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText rFullname, rEmail, rPassword, rPhone;
    Button rbtnRegister;
    TextView rLoginButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rFullname = findViewById(R.id.etxtRegName);
        rEmail = findViewById(R.id.etxtRegEmail);
        rPassword = findViewById(R.id.etxtRegPass);
        rPhone = findViewById(R.id.etxtRegPhone);
        rbtnRegister = findViewById(R.id.btnReg);
        rLoginButton = findViewById(R.id.textView4);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

        rbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = rEmail.getText().toString().trim();
                final String password = rPassword.getText().toString().trim();
                final String fullName = rFullname.getText().toString();
                final String phone = rPhone.getText().toString();

                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Password is required.");
                    return;
                }

                if(password.length() < 6 ) {

                    rPassword.setError("Password must be 6 characters long.");
                    return;

                }


                //register user in firebase
                fAuth.createUserWithEmailAndPassword(email,password) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();

                            //collect data
                            userID= fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("FullName", fullName);
                            user.put("phone", phone);
                            user.put("email", email);
                            user.put("password",password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile id created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Some Error Occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        rLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}