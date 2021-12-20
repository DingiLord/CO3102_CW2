package com.example.co3102_cw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText emailLogin, passwordLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = findViewById(R.id.EmailLogin);
        passwordLogin = findViewById(R.id.PasswordLogin);
        mAuth = FirebaseAuth.getInstance();

        // Register Button
        Button registerButton = (Button) findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Wowie", Toast.LENGTH_SHORT).show();
                // Possibly pass over the entered email to the register field of email for Quality Of Life
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);

            }
        });
        // Login Button
        Button loginButton = (Button) findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailLogin.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordLogin.setError("Password is Required");
                    return;
                }
                if(password.length() < 8){
                    passwordLogin.setError("Password has to be atleast 8 characters");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Succesfully Loged In", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to Log In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




    }


}