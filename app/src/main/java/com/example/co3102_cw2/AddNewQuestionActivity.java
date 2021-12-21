package com.example.co3102_cw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.co3102_cw2.Adapter.OptionAdapter;
import com.example.co3102_cw2.Model.Option;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddNewQuestionActivity extends AppCompatActivity {

    private RecyclerView optionRecyclerView;
    private OptionAdapter optionAdapter;
    private List<Option> optionList;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        getSupportActionBar().hide();
        optionList = new ArrayList<>();
        optionRecyclerView = findViewById(R.id.OptionsRecyclerView);
        optionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        optionAdapter = new OptionAdapter(this);
        optionRecyclerView.setAdapter(optionAdapter);
        floatingActionButton = findViewById(R.id.FABQuestionCreation);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        Option item = new Option();
//        item.setId(0);
//        item.setText("2");
//        item.setStatus(false);
//
//        optionList.add(item);
//        optionList.add(item);
//        optionList.add(item);
//        optionList.add(item);
//        optionAdapter.setOptionList(optionList);
    }
}