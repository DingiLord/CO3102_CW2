package com.example.co3102_cw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.co3102_cw2.Adapter.QuestionAdapter;
import com.example.co3102_cw2.Model.QuestionListItem;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView questionRecyclerView;
    private QuestionAdapter questionAdapter;

    private List<QuestionListItem> questionList;

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

        QuestionListItem item = new QuestionListItem();
        item.setId(0);
        item.setQuestion("What colour is the sky?");
        item.setStatus(false);

        questionList.add(item);
        questionList.add(item);
        questionList.add(item);
        questionList.add(item);
        questionList.add(item);

        questionAdapter.setQuestionList(questionList);

    }
}