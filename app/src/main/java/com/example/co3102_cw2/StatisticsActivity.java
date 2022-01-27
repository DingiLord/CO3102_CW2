package com.example.co3102_cw2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.co3102_cw2.Model.Option;
import com.example.co3102_cw2.Model.Question;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    HorizontalBarChart barChart;
    private String questionName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference quest = db.collection("questions");
    private List<String> options = new ArrayList<>();
    ArrayList<BarEntry> values = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChart = findViewById(R.id.bargraph);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            questionName = extras.getString("questionText");
        getSupportActionBar().setTitle(questionName);

        // Gets Data for the question from the database
        quest.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (documentSnapshot.get("question") != null)
                        if(documentSnapshot.get("question").toString().equals(questionName)){
                            Question question = documentSnapshot.toObject(Question.class);
                            int counter = 0;
                            for(Option o: question.getOptions()){
                                options.add(o.getText());
                                values.add(new BarEntry(counter,o.getVotes()));
                                counter++;
                            }
                        }
                }
                BarData data = createChartData();
                configuration();
                data.setValueTextSize(12f);
                barChart.setData(data);
                barChart.invalidate();
            }
        });


    }

    private BarData createChartData(){

        BarDataSet set = new BarDataSet(values,questionName);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        set.setDrawValues(true);
        dataSets.add(set);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void configuration(){
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return options.get((int) value);
            }
        });
        xAxis.setLabelCount(options.size(),true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisLineColor(android.R.color.black);
        xAxis.setDrawLabels(true);

    }
}