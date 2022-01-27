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

import com.example.co3102_cw2.Adapter.QuestionAdapter;
import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.Question;
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

public class AdminActivity extends AppCompatActivity implements QuestionAdapter.OnQuestionListener {

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionList;
    private FloatingActionButton floatingActionButton;
    private Button signout;

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference questionsDB = db.collection("questions");

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d(TAG, "On Activity Result (Admin): ");
            if(result.getResultCode() == 1){
                // Refresh
                Intent refresh = getIntent();
                finish();
                startActivity(refresh);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        questionList = new ArrayList<>();
        questionRecyclerView = findViewById(R.id.QuestionsRecyclerViewAdmin);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this,this);
        questionRecyclerView.setAdapter(questionAdapter);
        floatingActionButton = findViewById(R.id.floatingActionButtonAdmin);
        signout = findViewById(R.id.SignOutButton);


        InitialData();




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),AddNewQuestionActivity.class);
                startActivity(intent);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            finish();
        }
    }

    // Populates Questions from the Database
    public void InitialData(){
        questionsDB.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                questionList.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Question question = documentSnapshot.toObject(Question.class);
                    questionList.add(question);
                }
                questionAdapter.setQuestionList(questionList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Something went wrong: " + e);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refreshes The Home Page
        InitialData();
    }

    @Override
    public void onQuestionClick(int position) {

        boolean editable = true;

        for(Option o: questionList.get(position).getOptions()){
            if(o.getVotes() > 0)
                editable = false;
        }
        if(editable == true){
            Intent intent = new Intent(this,EditQuestionActivity.class);
            intent.putExtra("questionText",questionList.get(position).getQuestion());
            activityResultLauncher.launch(intent);
        } else {
            // Since it cannot be edited it will give admin the view of bar graph
            Intent intent = new Intent(this, StatisticsActivity.class);
            intent.putExtra("questionText",questionList.get(position).getQuestion());
            activityResultLauncher.launch(intent);
        }

    }

}