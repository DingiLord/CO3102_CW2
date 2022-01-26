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

import com.example.co3102_cw2.Adapter.QuestionAdapter;
import com.example.co3102_cw2.Model.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d(TAG, "On Activity Result: ");
        }
    });

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference questionsDB = db.collection("questions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Admin Layout with hidden buttons

        getSupportActionBar().hide();
        questionList = new ArrayList<>();
        questionRecyclerView = findViewById(R.id.QuestionsRecyclerViewAdmin);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this, this);
        questionRecyclerView.setAdapter(questionAdapter);
        floatingActionButton = findViewById(R.id.floatingActionButtonAdmin);
        floatingActionButton.setVisibility(View.GONE); // Hidden for Normal Users

        InitialData();
    }

    @Override
    public void onQuestionClick(int position) {
        questionList.get(position);
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("questionText",questionList.get(position).getQuestion());
        activityResultLauncher.launch(intent);

    }

    // Populates Questions from the Database
    public void InitialData() {
        questionsDB.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                questionList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Question question = documentSnapshot.toObject(Question.class);
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
}