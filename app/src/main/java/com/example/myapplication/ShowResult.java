package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ShowResult extends AppCompatActivity {
    private TextView ans1, ans2, ans3, ans4, ans5, ans6, ans7, ans8, ans9, ans10;
    private TextView sel1, sel2, sel3, sel4, sel5, sel6, sel7, sel8, sel9, sel10;
    private TextView stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9, stat10;
    private String[] answers, selectanswers;
    private TextView CountCorrect,TextCount,InCorrectCounter;
    private Button btnBack;
    private Intent backMain;
    private int counterCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_result);
        TextCount=findViewById(R.id.textCorrect);
        CountCorrect=findViewById(R.id.NumberCorrect);
        InCorrectCounter=findViewById(R.id.NumberInCorrect);
        btnBack=findViewById(R.id.button1);
        backMain=new Intent(ShowResult.this,MainActivity.class);
        TextCount.setText("Count : ");
        // Initialize the TextViews
        initViews();

        // Retrieve Intent extras
        Intent intent = getIntent();
        if (intent != null) {
            answers = intent.getStringArrayExtra("answers");
            selectanswers = intent.getStringArrayExtra("selectanswers");
            counterCorrect = intent.getIntExtra("CounterCorrect", 0);
            CountCorrect.setText("Correct "+String.valueOf(counterCorrect));
            InCorrectCounter.setText("InCorrect "+String.valueOf(10-counterCorrect));
            if (answers != null && selectanswers != null && answers.length == 10 && selectanswers.length == 10) {
                populateAnswers();
                populateSelections();
                populateStatus();
            } else {
                Toast.makeText(this, "Incomplete data received!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No Intent data received!", Toast.LENGTH_LONG).show();
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(backMain);
                finish();
            }
        });
    }

    private void initViews() {
        ans1 = findViewById(R.id.textAns1);
        ans2 = findViewById(R.id.textAns2);
        ans3 = findViewById(R.id.textAns3);
        ans4 = findViewById(R.id.textAns4);
        ans5 = findViewById(R.id.textAns5);
        ans6 = findViewById(R.id.textAns6);
        ans7 = findViewById(R.id.textAns7);
        ans8 = findViewById(R.id.textAns8);
        ans9 = findViewById(R.id.textAns9);
        ans10 = findViewById(R.id.textAns10);

        sel1 = findViewById(R.id.textSel1);
        sel2 = findViewById(R.id.textSel2);
        sel3 = findViewById(R.id.textSel3);
        sel4 = findViewById(R.id.textSel4);
        sel5 = findViewById(R.id.textSel5);
        sel6 = findViewById(R.id.textSel6);
        sel7 = findViewById(R.id.textSel7);
        sel8 = findViewById(R.id.textSel8);
        sel9 = findViewById(R.id.textSel9);
        sel10 = findViewById(R.id.textSel10);

        stat1 = findViewById(R.id.textStatus1);
        stat2 = findViewById(R.id.textStatus2);
        stat3 = findViewById(R.id.textStatus3);
        stat4 = findViewById(R.id.textStatus4);
        stat5 = findViewById(R.id.textStatus5);
        stat6 = findViewById(R.id.textStatus6);
        stat7 = findViewById(R.id.textStatus7);
        stat8 = findViewById(R.id.textStatus8);
        stat9 = findViewById(R.id.textStatus9);
        stat10 = findViewById(R.id.textStatus10);
    }

    private void populateAnswers() {
        ans1.setText(answers[0]);
        ans2.setText(answers[1]);
        ans3.setText(answers[2]);
        ans4.setText(answers[3]);
        ans5.setText(answers[4]);
        ans6.setText(answers[5]);
        ans7.setText(answers[6]);
        ans8.setText(answers[7]);
        ans9.setText(answers[8]);
        ans10.setText(answers[9]);
    }

    private void populateSelections() {
        sel1.setText(selectanswers[0]);
        sel2.setText(selectanswers[1]);
        sel3.setText(selectanswers[2]);
        sel4.setText(selectanswers[3]);
        sel5.setText(selectanswers[4]);
        sel6.setText(selectanswers[5]);
        sel7.setText(selectanswers[6]);
        sel8.setText(selectanswers[7]);
        sel9.setText(selectanswers[8]);
        sel10.setText(selectanswers[9]);
    }

    private void populateStatus() {
        stat1.setText(answers[0].equals(selectanswers[0]) ? "Correct" : "Incorrect");
        ChangeColorState(stat1);
        stat2.setText(answers[1].equals(selectanswers[1]) ? "Correct" : "Incorrect");
        ChangeColorState(stat2);
        stat3.setText(answers[2].equals(selectanswers[2]) ? "Correct" : "Incorrect");
        ChangeColorState(stat3);
        stat4.setText(answers[3].equals(selectanswers[3]) ? "Correct" : "Incorrect");
        ChangeColorState(stat4);
        stat5.setText(answers[4].equals(selectanswers[4]) ? "Correct" : "Incorrect");
        ChangeColorState(stat5);
        stat6.setText(answers[5].equals(selectanswers[5]) ? "Correct" : "Incorrect");
        ChangeColorState(stat6);
        stat7.setText(answers[6].equals(selectanswers[6]) ? "Correct" : "Incorrect");
        ChangeColorState(stat7);
        stat8.setText(answers[7].equals(selectanswers[7]) ? "Correct" : "Incorrect");
        ChangeColorState(stat8);
        stat9.setText(answers[8].equals(selectanswers[8]) ? "Correct" : "Incorrect");
        ChangeColorState(stat9);
        stat10.setText(answers[9].equals(selectanswers[9]) ? "Correct" : "Incorrect");
        ChangeColorState(stat10);
    }
    private void ChangeColorState(TextView stat){
        if(stat.getText().equals("Correct")){
            stat.setTextColor(getResources().getColor(R.color.greenspecial));
        }
        else{
            stat.setTextColor(getResources().getColor(R.color.Red));
        }
    }
}
