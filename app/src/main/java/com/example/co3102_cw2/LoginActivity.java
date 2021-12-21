package com.example.co3102_cw2;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


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

                //TODO: Check if User Exists

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

                            loginBasedOnAccess(task.getResult().getUser());
                            Toast.makeText(getApplicationContext(), "Succesfully Loged In", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to Log In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




    }

    public void loginBasedOnAccess(FirebaseUser user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference adminRefrence = db.collection("admins").document("IDS");
        adminRefrence.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                boolean loggedIn = false;
                if(document.exists()){
                    for(Map.Entry<String,Object> i : document.getData().entrySet()){
                        // Todo: REMOVE AFTER Finishing The CW2
                        Log.d(TAG,"Admind ID: " + i.getValue().toString());
                        Log.d(TAG,"USER ID: " + user.getUid());

                        if(user.getUid().equals(i.getValue().toString())){
                            loggedIn = true;
                            Intent intent = new Intent(getBaseContext(),AdminActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    if(loggedIn == false){
                        // Stars Normal User Activity
                        Intent intent = new Intent(getBaseContext(),UserActivity.class);
                        startActivity(intent);
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Failed To Get Data: " + e);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: SIGN OUT USER

    }
}