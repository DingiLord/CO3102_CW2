package com.example.co3102_cw2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    BarChart barChart;
    private String questionName;
    //Todo: Change to get all options from the question
    private String[] Test = {"Option 1","Option 2","Option 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChart = findViewById(R.id.bargraph);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            questionName = extras.getString("questionText");

        BarData data = createChartData();


    }

    private BarData createChartData(){
        ArrayList<BarEntry> values = new ArrayList<>();
        //TODO: A loop to get all of the votes
        values.add(new BarEntry(1,50));
        values.add(new BarEntry(2,100));
        values.add(new BarEntry(0,900));

        BarDataSet set = new BarDataSet(values,questionName);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        BarData data = new BarData(dataSets);
        configuration();
        data.setValueTextSize(12f);
        barChart.setData(data);
        barChart.invalidate();

        return data;
    }

    private void configuration(){
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Test[(int) value];
            }
        });
    }
}