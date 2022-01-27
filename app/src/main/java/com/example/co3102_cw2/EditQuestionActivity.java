package com.example.co3102_cw2;

import static com.example.co3102_cw2.AddNewOption.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.co3102_cw2.Adapter.OptionAdapter;
import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class EditQuestionActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView optionRecyclerView;
    private OptionAdapter optionAdapter;
    private ArrayList<Option> optionList;
    private FloatingActionButton floatingActionButton;
    private EditText questionText;
    private Button finish, remove;
    private TextView questionTitle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference quest = db.collection("questions");
    CollectionReference tmp = db.collection("tmp");
    private String questionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //Uses the same layout as adding new question
        setContentView(R.layout.activity_add_new_question);
        optionList = new ArrayList<>();
        questionText = findViewById(R.id.QuestionTextCreate);
        optionRecyclerView = findViewById(R.id.OptionsRecyclerView);
        optionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        optionAdapter = new OptionAdapter(this);
        questionTitle = findViewById(R.id.CreateAQuetionText);
        optionRecyclerView.setAdapter(optionAdapter);
        finish = findViewById(R.id.finishCreatingQuestionButton);
        floatingActionButton = findViewById(R.id.FABQuestionCreation);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(optionAdapter));
        itemTouchHelper.attachToRecyclerView(optionRecyclerView);
        remove = findViewById(R.id.deleteQuestion);


        Bundle extras = getIntent().getExtras();
        questionName = extras.getString("questionText");

        questionTitle.setText("Edit Question");
        // Populate Data from the database
        if(questionName != null)
            quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        if (documentSnapshot.get("question") != null)
                            if(documentSnapshot.get("question").toString().equals(questionName)){
                                Question question = documentSnapshot.toObject(Question.class);
                                questionText.setText(question.getQuestion());
                                optionList.addAll(question.getOptions());
                                optionAdapter.setOptionList(optionList);
                                optionAdapter.notifyDataSetChanged();
                            }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewOption.newInstance().show(getSupportFragmentManager(), TAG);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean exists = false;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Question question = documentSnapshot.toObject(Question.class);
                            if(question.getQuestion().equals(questionText.getText().toString())){
                                exists = true;
                                Toast.makeText(getBaseContext(), "This Question already exists", Toast.LENGTH_SHORT).show();
                            }

                        }
                        if(exists == false){
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                if (documentSnapshot.get("question") != null)
                                    if(documentSnapshot.get("question").toString().equals(questionName)){
                                        documentSnapshot.getReference().update("question",questionText.getText().toString());
                                        for(Option o: optionList){
                                            o.setParent(questionText.getText().toString());
                                        }
                                        documentSnapshot.getReference().update("options",optionList);
                                    }
                            }
                            Intent intent = new Intent();
                            setResult(1,intent);
                            finish();
                        }

                    }
                });
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (documentSnapshot.get("question") != null)
                                if(documentSnapshot.get("question").toString().equals(questionName)){
                                    documentSnapshot.getReference().delete();
                                }
                        }
                    }
                });
                Intent intent = new Intent();
                setResult(1,intent);
                finish();
            }
        });

    }
    //Once the dialog fragment closes this function populates the option list with newly created option
    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {


        tmp.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Reset The OptionList Since contains does not capture dupes
                optionList.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Option option = documentSnapshot.toObject(Option.class);
                    option.setParent(questionName);
                    optionList.add(option);

                }
                quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (documentSnapshot.get("question") != null)
                                if(documentSnapshot.get("question").toString().equals(questionName)){
                                    Question question = documentSnapshot.toObject(Question.class);
                                    optionList.addAll(question.getOptions());
                                }
                        }
                        optionAdapter.setOptionList(optionList);
                        optionAdapter.notifyDataSetChanged();
                    }
                });
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //REMOVE TMP FROM THE DATABASE
        tmp.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    documentSnapshot.getReference().delete();
                }
            }
        });
    }

}