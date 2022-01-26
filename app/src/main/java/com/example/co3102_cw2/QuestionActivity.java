package com.example.co3102_cw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.co3102_cw2.Adapter.OptionAdapter;
import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.Question;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private ArrayList<Option> optionList;
    private Button submit;
    private TextView question;
    private RecyclerView optionRecyclerView;
    private OptionAdapter optionAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference quest = db.collection("questions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().hide();
        optionList = new ArrayList<>();
        submit = findViewById(R.id.SubmitQuestionButton);
        question = findViewById(R.id.QuestionText);
        optionRecyclerView = findViewById(R.id.QuestionOptionsRecyclerView);
        optionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        optionAdapter = new OptionAdapter(this);
        optionRecyclerView.setAdapter(optionAdapter);

        Bundle extras = getIntent().getExtras();
        if(extras.getString("questionText") != null){
            question.setText(extras.getString("questionText"));
            getOptions(question.getText().toString());

        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                for(Option o: optionList){
                    if(o.isStatus())
                        counter++;
                }
                if(counter>1){
                    Toast.makeText(getBaseContext(), "Only One Option can be selected", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    private void getOptions (String questionText){
        quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (documentSnapshot.get("question") != null)
                        if(documentSnapshot.get("question").toString().equals(questionText)){
                            Question question = documentSnapshot.toObject(Question.class);
                            optionList.addAll(question.getOptions());
                            optionAdapter.setOptionList(optionList);
                            optionAdapter.notifyDataSetChanged();
                        }
                }
            }
        });

    }

}