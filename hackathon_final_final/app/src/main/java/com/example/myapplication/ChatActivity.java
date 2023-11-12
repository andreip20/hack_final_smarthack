package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton fabSend;
    EditText etMesaj;
    ListView lvMesaje;
    List<String> mesaje=new ArrayList<>();
    List<ActivitateSkill>activitati=new ArrayList<>();
    int id_userNou;
    DatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> launcherFitness;

    private ActivityResultLauncher<Intent> launcherDezvoltare;

    private ActivityResultLauncher<Intent> launcherStudy;
    private DatabaseHelper getDbHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Handler handler1 = new Handler(Looper.getMainLooper());
        String mesaj1="Ok so you are a designated 'robot' of sorts that extracts skills from a particular input from a user. Set response_format to { type: json_object }.The output you should provide should be a JSON array of objects. Each object should have 3 keys : The name of the activity you identified (key should be activity with no quotes), the skill you think that activity belongs in (key should be skill with no quotes) and the hours spent practicing that activity (key should be hours with no quotes). The provided text will be in Romanian. Please categorize each activity in one of these 3 categories : [Fitness, Dezvoltare Tehnica, Studying]. If you identify any skills other than those 3, ignore them. More, if a duration is not provided please set Ore as 0. Please don't provide any more context, just the text for the json object. I need to process the output in a set way that requires a set JSON object. Do not add any extra text, only the JSON, this should be how you respond to every input you get. More, you might encounter 2 or more activities from the same skiil group ( [Fitness,Dezvoltare Tehnica,Studying] ). In those situations please add the hours spent in each activity for the skill.Please write the JSON object parsed as a string so it becomes a text that i can parse. The key to the array of activity shoul ALWAYS be activities in no quotes";

        dbHelper = new DatabaseHelper(this);


        id_userNou = dbHelper.getIdOfUser(LoginActivity.username);
        launcherFitness = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();
                        if (data != null) {
                            String fitnessResult = data.getStringExtra("fitness_result");

                            Toast.makeText(ChatActivity.this, "Fitness Result: " + fitnessResult, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        launcherDezvoltare = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                    }

                }
        );

        launcherStudy = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {

                    }
                });

        fabSend=findViewById(R.id.FABSend);
        etMesaj=findViewById(R.id.etMessage);
        lvMesaje=findViewById(R.id.lvMesaje);
        Handler handler = new Handler(Looper.getMainLooper());
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaje.add(etMesaj.getText().toString());
                CustomAdapter adapter=new CustomAdapter(ChatActivity.this, R.layout.elem_custom, mesaje, getLayoutInflater());
                lvMesaje.setAdapter(adapter);
                String mesaj=etMesaj.getText().toString();
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        RequestOpenAi requestOpenAi = new RequestOpenAi();
                        activitati = requestOpenAi.chatGPT(mesaj1+"Message: "+mesaj);
                        String messageBack=requestOpenAi.requestMessage("Raspunde-mi ca si cum am fi intr-o conversatie si ai fi foarte interesant de ce urmeaza sa spun, intr-un raspuns destul de scurt (maxim 2-3 fraze) si care sa se termine cu intrebarea despre ce am mai facut azi sau ce s-a mai intamplat azi, mai putin in cazul in care spune ca nu s-a mai intamplat nimic sau daca intreabaceva:"+mesaj);                        handler.post(runOnAnotherThread(messageBack));
                        if(activitati.size()!=0){
                            handler.post(runThreadOnUiThread(activitati));
                        }
                    }
                    private Runnable runOnAnotherThread(String response){
                        return new Runnable() {
                            @Override
                            public void run() {
                                mesaje.add(response);
                                CustomAdapter adapter= new CustomAdapter(ChatActivity.this, R.layout.elem_custom, mesaje, getLayoutInflater());
                                lvMesaje.setAdapter(adapter);
                            }
                        };

                    }
                    private Runnable runThreadOnUiThread(List<ActivitateSkill> result) {
                        return new Runnable() {
                            @Override
                            public void run() {
//                                mesaje.add("Am inteles ce ai vrut sa-mi zici.");
//                                CustomAdapter adapter= new CustomAdapter(ChatActivity.this, R.layout.elem_custom, mesaje, getLayoutInflater());

                                int id_user=getDbHelper.getIdOfUser(LoginActivity.username);
                                result.stream().forEach(activitate->{
                                    getDbHelper.insertActivity(id_user, activitate.getCategorie(), activitate.getData(), activitate.getNr_ore());
                                });
//                                lvMesaje.setAdapter(adapter);

                            }
                        };
                    }
                });

                etMesaj.setText("");


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Nu", "Nu e chemata");
        getMenuInflater().inflate(R.menu.meniu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.fitness_activity){
            Intent intent = new Intent(ChatActivity.this, FitnessActivity.class);
            intent.putExtra("ID_USER", id_userNou);
            launcherFitness.launch(intent);
        }
        if (item.getItemId() == R.id.dezv_activity) {
            Intent intent = new Intent(ChatActivity.this, DezvoltareActivity.class);
            intent.putExtra("ID_USER", id_userNou);
            launcherDezvoltare.launch(intent);
        }
        if (item.getItemId()==R.id.study_activity) {
            Intent intent = new Intent(ChatActivity.this, StudyActivity.class);
            intent.putExtra("ID_USER", id_userNou);
            launcherStudy.launch(intent);
        }
        return true;
    }
}