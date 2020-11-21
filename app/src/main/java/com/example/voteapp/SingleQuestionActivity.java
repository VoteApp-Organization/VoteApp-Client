package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SingleQuestionActivity extends AppCompatActivity {
    private TextView questionNumber;
    private TextView questionContent;
    private EditText answerContent;
    private ListView listViewOfAsnwers;
    private Button nextQuestionBtn;
    private String groupId;
    private String userId;
    private String groupTitle;
    private int number;
    private List<SingleQuestion> allQuestions = new ArrayList<>();
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_question);


        questionNumber = findViewById(R.id.questionNumber);
        questionContent = findViewById(R.id.questionContent);
        answerContent = findViewById(R.id.answerContent);
        listViewOfAsnwers = findViewById(R.id.listViewOfAsnwers);
        nextQuestionBtn = findViewById(R.id.nextQuestionBtn);
        mProgressBar = findViewById(R.id.progressbar);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        userId = intent.getStringExtra("userId");
        groupTitle = intent.getStringExtra("groupTitle");
        number = intent.getIntExtra("questionNumber", 0);
        Bundle args = intent.getBundleExtra("BUNDLE");
        allQuestions = (List<SingleQuestion>) args.getSerializable("questionsList");

        setupQuestion();

        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextQuestionBtn.getText().toString().trim().equals("Next question")) {
                    Intent intent = new Intent(SingleQuestionActivity.this, SingleQuestionActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("questionsList", (Serializable) allQuestions);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("userId", userId);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("groupTitle", groupTitle);
                    intent.putExtra("questionNumber", number + 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    backToGroupView();
                }
            }
        });
    }

    private void setupQuestion() {
        questionContent.setText(allQuestions.get(number).getQuestionContent());
        questionNumber.setText((number + 1) + " of " + allQuestions.size());

        final int divide = 100 / allQuestions.size() * (number + 1);
        mProgressStatus += divide;
        mProgressBar.setProgress(mProgressStatus);
        if (number + 1 == allQuestions.size()) {
            mProgressBar.setProgress(100);
            nextQuestionBtn.setText("Submit survey");
        }
    }

    @Override
    public void onBackPressed() {
        backToGroupView();
    }

    private void backToGroupView() {
        Intent intent = new Intent(SingleQuestionActivity.this, GroupView.class);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupTitle", groupTitle);
        startActivity(intent);
    }
}