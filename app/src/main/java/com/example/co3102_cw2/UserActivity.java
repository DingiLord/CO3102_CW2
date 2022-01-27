package com.example.co3102_cw2;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.co3102_cw2.Adapter.QuestionAdapter;
import com.example.co3102_cw2.Model.Question;
import com.example.co3102_cw2.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements QuestionAdapter.OnQuestionListener {

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionList;
    private FloatingActionButton floatingActionButton;
    private String currentUserEmail;
    private ArrayList<String> answQuestions = new ArrayList<>();
    private Button signout;

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference quest = db.collection("questions");
    CollectionReference users = db.collection("users");

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d(TAG, "On Activity Result: ");
            // Disable the answered Question
            if(result.getResultCode() == 1) {

                users.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Intent intent = result.getData();
                        if(intent != null){
                            String questionText = intent.getStringExtra("result");
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                if (documentSnapshot.get("email") != null)
                                    if(documentSnapshot.get("email").toString().equals(currentUserEmail)){
                                        User user = documentSnapshot.toObject(User.class);
                                        user.getAnsweredQuestions().add(questionText);
                                        documentSnapshot.getReference().update("answeredQuestions",user.getAnsweredQuestions());
                                    }
                            }
                        }
                        // Restarts Activity
                        Intent refresh = getIntent();
                        finish();
                        startActivity(refresh);

                    }
                });

            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Admin Layout with hidden buttons
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        questionList = new ArrayList<>();
        questionRecyclerView = findViewById(R.id.QuestionsRecyclerViewAdmin);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this, this);
        questionRecyclerView.setAdapter(questionAdapter);
        floatingActionButton = findViewById(R.id.floatingActionButtonAdmin);
        signout = findViewById(R.id.SignOutButton);
        floatingActionButton.setVisibility(View.GONE); // Hidden for Normal Users
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            currentUserEmail = extras.getString("email");
        getAnsweredQuestions();
        InitialData();

        // Does not fully signout to remember last email entered
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onQuestionClick(int position) {

        // Check If Question was Answered
        if(!questionList.get(position).isStatus()){
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("questionText",questionList.get(position).getQuestion());
            activityResultLauncher.launch(intent);
        } else{
            Toast.makeText(getBaseContext(), "This Question was already answered", Toast.LENGTH_SHORT).show();
        }

    }

    // Populates Questions from the Database
    public void InitialData() {
        quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                questionList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Question question = documentSnapshot.toObject(Question.class);
                    if(answQuestions.contains(question.getQuestion())){
                        question.setStatus(true);
                    }
                    questionList.add(question);
                }
                questionAdapter.setQuestionList(questionList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Something went wrong: " + e);
            }
        });
    }
    public void getAnsweredQuestions(){
        users.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (documentSnapshot.get("email") != null)
                        if(documentSnapshot.get("email").toString().equals(currentUserEmail)){
                            User user = documentSnapshot.toObject(User.class);
                            answQuestions.addAll(user.getAnsweredQuestions());
                        }
                }
            }
        });
    }

}