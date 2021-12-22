package com.example.co3102_cw2;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.co3102_cw2.Adapter.QuestionAdapter;
import com.example.co3102_cw2.Model.Question;
import com.example.co3102_cw2.Model.QuestionListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionList;
    private FloatingActionButton floatingActionButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference questionsDB = db.collection("questions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();
        questionList = new ArrayList<>();
        questionRecyclerView = findViewById(R.id.QuestionsRecyclerViewAdmin);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this);
        questionRecyclerView.setAdapter(questionAdapter);
        floatingActionButton = findViewById(R.id.floatingActionButtonAdmin);

        QuestionListItem item = new QuestionListItem();
        item.setId(0);
        item.setQuestion("What colour is the sky?");
        item.setStatus(false);

        InitialData();

//        questionList.add(item);
//        questionList.add(item);
//        questionList.add(item);
//        questionList.add(item);
//        questionList.add(item);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),AddNewQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    // Populates Questions from the Database (Pretty sure I dont actually need to pass the adapter)
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
        // KINDA CHEATING
        InitialData();
    }

    //    public void InitialData(QuestionAdapter questionAdapter){
//        questionList = new ArrayList<>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("general").document("Questions");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    if(documentSnapshot.exists()){
//                        for(Map.Entry<String,Object> i : documentSnapshot.getData().entrySet()){
//                          Log.d(TAG,"Question?: "+i.getValue().toString());
//                          QuestionListItem item = new QuestionListItem();
//                          item.setStatus(false);
//                          item.setQuestion(i.getValue().toString());
//                          questionList.add(item);
//                        }
//                        questionAdapter.setQuestionList(questionList);
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //TODO: action if it fails
//            }
//        });
//
//    }

}