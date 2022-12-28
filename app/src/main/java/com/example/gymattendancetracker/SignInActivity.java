package com.example.gymattendancetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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


public class SignInActivity extends AppCompatActivity {
    EditText PasswordSI , EmailSI ;
    TextView ToSU;
    Button SignIn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        PasswordSI = findViewById(R.id.PasswordSI);
        EmailSI = findViewById(R.id.EmailSI);
        SignIn = findViewById(R.id.SignIn);
        ToSU = findViewById(R.id.ToSU);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!= null){
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for getting text from editTexts
                String txt_email = EmailSI.getText().toString();
                String txt_pass = PasswordSI.getText().toString();
                //method created for signIn Activity
                try {
                    SignInUser(txt_email,txt_pass);
                }
                catch (IllegalArgumentException e){
                    Toast.makeText(SignInActivity.this , "Enter values!" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        ToSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });


    }

    private void SignInUser(String txt_email, String txt_pass) {
        auth.signInWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "User logged in successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(SignInActivity.this, "Incorrect credentials" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}