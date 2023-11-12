package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StudyActivity extends AppCompatActivity {

    AppCompatButton btn_cancel;

    private DatabaseHelper dbHelper;

    private LineChart lc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        dbHelper = new DatabaseHelper(this);
        lc = findViewById(R.id.linechart_study);
        btn_cancel = findViewById(R.id.btn_cancel_study);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        List<Entry> entries = new ArrayList<>();

            HashMap<String, String[]> query = new HashMap<>();

        int idOfUser = dbHelper.getIdOfUser(LoginActivity.username);
            query = dbHelper.findByCategory(idOfUser, "Studying");
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










        LineDataSet dataSet = new LineDataSet(entries, "Numar de ore"); // Label for the dataset


        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.MAGENTA);

        LineData lineData = new LineData(dataSet);
        lc.setData(lineData);


        XAxis xAxis = lc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        List<String> dateLabels = generateDateLabels(query);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));

        YAxis leftYAxis = lc.getAxisLeft();
        YAxis rightYAxis = lc.getAxisRight();
        leftYAxis.setAxisMinimum(0f); // Set the minimum value for the Y-axis


        Legend legend = lc.getLegend();
        legend.setForm(Legend.LegendForm.LINE);


        dataSet.setLineWidth(4.0f);
        lc.setDrawGridBackground(false);


        lc.invalidate(); // Refresh the chart
    }
    private List<String> generateDateLabels(HashMap<String, String[]> query) {
        // Replace this with your actual date labels
        List<String> dateLabels = new ArrayList<>();
        for(String key : query.keySet()) {
            dateLabels.add(key);
        }


        return dateLabels;
    }
}