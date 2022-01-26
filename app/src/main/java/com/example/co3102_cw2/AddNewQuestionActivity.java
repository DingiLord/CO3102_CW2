package com.example.co3102_cw2;

import static com.example.co3102_cw2.AddNewOption.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddNewQuestionActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView optionRecyclerView;
    private OptionAdapter optionAdapter;
    private ArrayList<Option> optionList;
    private FloatingActionButton floatingActionButton;
    private EditText questionText;
    private Button finish, remove;
    private TextView questionTitle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference tmp = db.collection("tmp");
    CollectionReference quest = db.collection("questions");

    //TODO: Implement edit and remove on options


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        optionList = new ArrayList<>();
        questionTitle = findViewById(R.id.CreateAQuetionText);
        questionText = findViewById(R.id.QuestionTextCreate);
        optionRecyclerView = findViewById(R.id.OptionsRecyclerView);
        optionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        optionAdapter = new OptionAdapter();
        optionRecyclerView.setAdapter(optionAdapter);
        finish = findViewById(R.id.finishCreatingQuestionButton);
        floatingActionButton = findViewById(R.id.FABQuestionCreation);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(optionAdapter));
        itemTouchHelper.attachToRecyclerView(optionRecyclerView);
        remove = findViewById(R.id.deleteQuestion);
        remove.setVisibility(View.GONE);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewOption.newInstance().show(getSupportFragmentManager(), TAG);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Question
                Question question = new Question();
                question.setQuestion(questionText.getText().toString());
                question.setOptions(optionList);
                db.collection("questions").add(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Successfully added a Question to the Database");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error While Adding new Question : " + e);
                    }
                });
                finish();
            }
        });



    }
    //Once the dialog fragment closes this function populates the option list with newly created option
    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        // Infrom the adapter that the data has changed
//        optionList = new ArrayList<>();
//        DocumentReference docRef = db.collection("general").document("tmp");

        tmp.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                int counter = 0;
                //Reset The OptionList Since contains does not capture dupes
                optionList.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Option option = documentSnapshot.toObject(Option.class);
//                        option.setId(counter);
                        option.setParent(questionText.getText().toString());
                        optionList.add(option);
//                        counter++;
                }
                optionAdapter.setOptionList(optionList);
                optionAdapter.notifyDataSetChanged();
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