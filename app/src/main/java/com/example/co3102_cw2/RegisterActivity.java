package com.example.co3102_cw2;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.co3102_cw2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference sniDB = db.collection("sni_code").document("SNICode");
    EditText rEmail,rFullName,rDOB,rHomeAdd,rPassword,rConfirmPassword,rSNI;
    Button register;
    ImageButton camera;

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
        camera = findViewById(R.id.CameraRegister);

        // Camera Button

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA);
            }
        });
        // Register Button
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
                if(!TextUtils.equals(password,confirmPassword)){
                    rConfirmPassword.setError("Passwords must be matching");
                    return;
                }
                if(TextUtils.isEmpty(sni)){
                    rSNI.setError("Shangri-La National Insurance Number Is Required");
                    return;
                }
                if(sni.length() != 8){
                    rSNI.setError("SNI Number is 8 Digits");
                    return;
                }

                // Firabase accesses database asycrinuosly therefore I need call this during all other checks
                sniDB.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> sniData = document.getData();

                            if(!sniData.keySet().contains(sni)){
                                rSNI.setError("This SNI Number Does Not Exist");
                            }
                            else if(sniData.keySet().contains(sni) && sniData.get(sni).equals(true)){
                                rSNI.setError("This SNI Number is Already Taken");
                            } else {
                                // Create a new account
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "createUserWithEmail:success");
                                            addUserToDatabase(email,fullName,dob,homeAddress,sni);
                                            Intent intent = new Intent(getBaseContext(),UserActivity.class);
                                            intent.putExtra("email",email);
                                            startActivity(intent);

                                            //Mark sni code as taken in the database
                                            document.getReference().update(sni,true);

                                        }else{
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if(e instanceof FirebaseAuthUserCollisionException){
                                            rEmail.setError("Email is Already Taken");
                                            return;
                                        }else
                                            rEmail.setError("Email was badly formatted");
                                            return;
                                    }
                                });
                            }
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
           //TODO : IF there is time check if user is logged in
        }
    }

    public void addUserToDatabase(String email,String name, String dob, String homeAddress, String sni){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User user = new User(email,name,dob,homeAddress,sni);

        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document: " + e);
            }
        });
    }

    ActivityResultLauncher<Intent> GetQRCode = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == 1){
                Intent intent = result.getData();
                if(intent != null){
                    rSNI.setText(intent.getStringExtra("result"));
                }
            }
        }
    });

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        if (isGranted){

            Intent intent = new Intent(getBaseContext(), CameraActivity.class);
            GetQRCode.launch(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Cannot Use the camera without permissions", Toast.LENGTH_SHORT).show();
        }
    });

//    private void getAllSni(){
//        // Get a List of all Sni Codes
//        sniDB.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    sniData = document.getData();
//                }
//            }
//        });
//    }




}