package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import api.RetrofitClient;
import api.TriviaApi;
import model.Question;
import model.TriviaResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; // Correct TAG variable

    private List<Question> questions;
    private TextView t, Logo;
    private Button ans1, ans2, ans3, ans4;
    private int idx;
    private String[] answers, selectanswers;
    private int counterCorrect;
    private Intent showAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        idx = 0;
        this.answers = new String[10];
        this.selectanswers = new String[10];
        this.counterCorrect = 0;
        // Initialize the TextView
        t = findViewById(R.id.textView);
        this.showAnswers = new Intent(MainActivity.this, ShowResult.class);
        ans1 = findViewById(R.id.button1);
        ans2 = findViewById(R.id.button2);
        ans3 = findViewById(R.id.button3);
        ans4 = findViewById(R.id.button4);
        // Fetch trivia questions
        fetchTriviaQuestions();
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerSelection(ans1.getText().toString());
            }
        });
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerSelection(ans2.getText().toString());
            }
        });
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerSelection(ans3.getText().toString());
            }
        });
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswerSelection(ans4.getText().toString());
            }
        });
    }

    private void handleAnswerSelection(String selectedAnswer) {
        if (selectedAnswer.equals(answers[idx])) {
            counterCorrect += 1;
        }
        selectanswers[idx] = selectedAnswer;
        idx++;
        checkFinish();
        updateTriviaQuestions();
    }

    private void fetchTriviaQuestions() {
        TriviaApi triviaApi = RetrofitClient.getClient().create(TriviaApi.class);
        Call<TriviaResponse> call = triviaApi.getQuestions(10, 18, "multiple");

        call.enqueue(new Callback<TriviaResponse>() {
            @Override
            public void onResponse(Call<TriviaResponse> call, Response<TriviaResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Response not successful: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    return;
                }
                int i = 0;
                TriviaResponse triviaResponse = response.body();
                if (triviaResponse != null && triviaResponse.getResponseCode() == 0) {
                    questions = triviaResponse.getResults(); // Corrected method name

                    if (questions != null && !questions.isEmpty()) {
                        // Decode HTML entities and update questions
                        for (Question question : questions) {
                            if (i >= answers.length) {
                                Log.e(TAG, "More questions received than the size of the answers array.");
                                break;
                            }
                            // Decode question and correct answer
                            String decodedQuestion = HtmlCompat.fromHtml(question.getQuestion(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                            String decodedCorrectAnswer = HtmlCompat.fromHtml(question.getCorrectAnswer(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                            question.setQuestion(decodedQuestion);
                            question.setCorrectAnswer(decodedCorrectAnswer);
                            answers[i] = decodedCorrectAnswer;

                            // Decode incorrect answers
                            List<String> decodedIncorrectAnswers = new ArrayList<>();
                            if (question.getIncorrectAnswers() != null) {
                                for (String incorrect : question.getIncorrectAnswers()) {
                                    decodedIncorrectAnswers.add(HtmlCompat.fromHtml(incorrect, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                }
                                decodedIncorrectAnswers.add(decodedCorrectAnswer);
                                Collections.shuffle(decodedIncorrectAnswers);
                                if (decodedIncorrectAnswers.size() >= 4) { // Ensure at least 4 options
                                    ans1.setText(decodedIncorrectAnswers.get(0));
                                    ans2.setText(decodedIncorrectAnswers.get(1));
                                    ans3.setText(decodedIncorrectAnswers.get(2));
                                    ans4.setText(decodedIncorrectAnswers.get(3));
                                } else {
                                    Log.e(TAG, "Not enough answers to assign to buttons for question: " + decodedQuestion);
                                    Toast.makeText(MainActivity.this, "Not enough answers for a question.", Toast.LENGTH_SHORT).show();
                                }
                                question.setIncorrectAnswers(decodedIncorrectAnswers);
                            } else {
                                Log.e(TAG, "Incorrect answers list is null for question: " + decodedQuestion);
                            }

                            // Log the decoded question and answers
                            Log.d(TAG, "Question: " + decodedQuestion);
                            Log.d(TAG, "Correct Answer: " + decodedCorrectAnswer);
                            Log.d(TAG, "Incorrect Answers: " + question.getIncorrectAnswers());

                            i++;
                        }

                        // Update the UI with the first question
                        if (idx < questions.size()) {
                            String firstQuestion = questions.get(idx).getQuestion();
                            t.setText(firstQuestion);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "No questions available", Toast.LENGTH_SHORT).show();
                    }

                    // Log all questions
                    for (Question question : questions) {
                        Log.d(TAG, "Question: " + question.getQuestion());
                        Log.d(TAG, "Correct Answer: " + question.getCorrectAnswer());
                        Log.d(TAG, "Incorrect Answers: " + question.getIncorrectAnswers());
                    }
                } else {
                    Log.e(TAG, "Invalid response");
                    Toast.makeText(MainActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TriviaResponse> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTriviaQuestions() {
        if (idx < 10) {
            Question q = questions.get(idx);
            if (q != null) {
                t.setText(q.getQuestion());
                List<String> options = q.getIncorrectAnswers();
                if (options.size() >= 4) {
                    ans1.setText(options.get(0));
                    ans2.setText(options.get(1));
                    ans3.setText(options.get(2));
                    ans4.setText(options.get(3));
                } else {
                    Log.e(TAG, "Not enough options for question: " + q.getQuestion());
                    Toast.makeText(this, "Not enough answers for the question.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkFinish() {
        if (this.idx >= 10) {
            // Ensure that arrays are fully populated
            for (int i = 0; i < 10; i++) {
                if (selectanswers[i] == null) {
                    selectanswers[i] = "No Answer";
                }
                if (answers[i] == null) {
                    answers[i] = "No Answer";
                }
            }

            showAnswers.putExtra("answers", answers);
            showAnswers.putExtra("selectanswers", selectanswers);
            showAnswers.putExtra("CounterCorrect", counterCorrect);
            startActivity(showAnswers);
            finish(); // Optional: Finish MainActivity to prevent going back
        }
    }
}
