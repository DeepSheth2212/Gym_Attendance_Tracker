package com.example.gymattendancetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymattendancetracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    TextView ToSI;
    EditText NameSU , EmailSU , PasswordSU;
    Button Register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        PasswordSU = findViewById(R.id.PasswordSU);
        NameSU = findViewById(R.id.NameSU);
        EmailSU = findViewById(R.id.EmailSU);
        Register = findViewById(R.id.Register);
        ToSI = findViewById(R.id.ToSI);
        auth = FirebaseAuth.getInstance();


        //when clicking the register button
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for getting the string from email and password
                String txt_email = EmailSU.getText().toString();
                String txt_pass = PasswordSU.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(SignUpActivity.this, "Enter Email and Password!", Toast.LENGTH_SHORT).show();
                }else if(txt_pass.length()<6){
                    Toast.makeText(SignUpActivity.this, "Enter password greater than 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(txt_email,txt_pass);
                }
            }
        });

        ToSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this , SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(String Email , String Pass) {
        auth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this , SignInActivity.class));
                }
                else{
                    Toast.makeText(SignUpActivity.this, "User Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}