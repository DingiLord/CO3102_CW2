package com.example.co3102_cw2;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText rEmail,rFullName,rDOB,rHomeAdd,rPassword,rConfirmPassword,rSNI;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        rEmail = findViewById(R.id.EmailRegisterBox);
        rFullName = findViewById(R.id.FullNameRegister);
        rDOB = findViewById(R.id.DobRegister);
        rHomeAdd = findViewById(R.id.HomeAddressRegister);
        rPassword = findViewById(R.id.PasswordRegister);
        rConfirmPassword = findViewById(R.id.ConfirmPasswordRegister);
        rSNI = findViewById(R.id.SNIRegister);
        register = findViewById(R.id.CompleteRegistrationButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = rEmail.getText().toString().trim();
                String fullName = rFullName.getText().toString().trim();
                String dob = rDOB.getText().toString().trim();
                String homeAddress = rHomeAdd.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String confirmPassword = rConfirmPassword.getText().toString().trim();
                String sni = rSNI.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    rFullName.setError("Full Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(dob)){
                    rDOB.setError("Date of Birth is Required");
                    return;
                }
                if(TextUtils.isEmpty(homeAddress)){
                    rHomeAdd.setError("Home Address is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Password is Required");
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)){
                    rConfirmPassword.setError("Password is Required");
                    return;
                }
                if(password.length() < 8){
                    rPassword.setError("Password has to be atleast 8 characters");
                    return;
                }
                if(password != confirmPassword){
                    rConfirmPassword.setError("Passwords must be matching");
                }

                if(TextUtils.isEmpty(sni)){
                    rSNI.setError("Shangri-La National Insurance Number Is Required");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });

            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        // Checking if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           // Toast.makeText(getApplicationContext(), "You're already logged in", Toast.LENGTH_SHORT).show();
            //TODO: Block the button
        }
    }


}