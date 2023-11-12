package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FitnessActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    AppCompatButton btn_cancel;

    private LineChart lc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        lc = findViewById(R.id.linechart_fitness);
        dbHelper = new DatabaseHelper(this);
        btn_cancel = findViewById(R.id.btn_cancel_fitness);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        List<Entry> entries = new ArrayList<>();

            HashMap<String, String[]> query = new HashMap<>();

            int idOfUser = dbHelper.getIdOfUser(LoginActivity.username);
            query = dbHelper.findByCategory(idOfUser, "Fitness");
            float finalSum = 0;
            int counter = 1;
//            for (String key: query.keySet()) {
//
//                entries.add(new Entry(counter, (float) Arrays.stream(query.get(key)).mapToDouble(Float::parseFloat).sum()));
//                counter++;
//            }
        for (String key:query.keySet()){
            finalSum += (float) Arrays.stream(query.get(key)).mapToDouble(Float::parseFloat).sum();
            entries.add(new Entry(counter, (finalSum/counter)));
            counter++;
        }






        // Add data to the line chart



        LineDataSet dataSet = new LineDataSet(entries, "Number of hours"); // Label for the dataset

        // Customize the line chart appearance
        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.MAGENTA);

        LineData lineData = new LineData(dataSet);
        lc.setData(lineData);

        // Customize the X and Y axes

        XAxis xAxis = lc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        List<String> dateLabels = generateDateLabels(query);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));
        YAxis leftYAxis = lc.getAxisLeft();
        YAxis rightYAxis = lc.getAxisRight();
        leftYAxis.setAxisMinimum(0f); // Set the minimum value for the Y-axis

        // Customize the legend
        Legend legend = lc.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        // Set the chart title
        dataSet.setLineWidth(4.0f);
        lc.setDrawGridBackground(false);

        // Display the chart
        lc.invalidate(); // Refresh the chart
    }
    private List<String> generateDateLabels(HashMap<String, String[]> query) {
        // Replace this with your actual date labels
        List<String> dateLabels = new ArrayList<>();
        for(String key : query.keySet()) {
            dateLabels.add(key);
        }
        // Add more date labels as needed

        return dateLabels;
    }
}