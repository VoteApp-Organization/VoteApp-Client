package com.example.voteapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voteapp.model.Group;
import com.example.voteapp.model.SingleQuestion;
import com.example.voteapp.model.Survey;
import com.example.voteapp.utils.CustomAnswerAdapter;
import com.example.voteapp.utils.StaticResources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PreviewSingleQuestionActivity extends AppCompatActivity {
    private TextView questionNumber;
    private TextView questionContent;
    private TextView previewMode;
    private EditText answerContent;
    private ListView listViewOfAsnwers;
    private Button nextQuestionBtn;
    private String userId;
    private Survey survey;
    private Group group;
    private int number;
    private List<SingleQuestion> allQuestions = new ArrayList<>();
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;
    private String typeOfQuestion;
    CustomAnswerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_question);


        previewMode = findViewById(R.id.previewMode);
        previewMode.setVisibility(View.VISIBLE);
        questionNumber = findViewById(R.id.questionNumber);
        questionContent = findViewById(R.id.questionContent);
        answerContent = findViewById(R.id.answerContent);
        listViewOfAsnwers = findViewById(R.id.listViewOfAsnwers);
        nextQuestionBtn = findViewById(R.id.nextQuestionBtn);
        mProgressBar = findViewById(R.id.progressbar);

        Intent intent = getIntent();
        number = intent.getIntExtra("questionNumber", 0);
        Bundle args = intent.getBundleExtra("BUNDLE");
        allQuestions = (List<SingleQuestion>) args.getSerializable("questionsList");
        survey = (Survey) args.getSerializable("survey");
        group = (Group) intent.getSerializableExtra("group");
        userId = intent.getStringExtra("userId");

        setupQuestion();

        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextQuestionBtn.getText().toString().trim().equals("Next question")) {
                    Intent intent = new Intent(PreviewSingleQuestionActivity.this, PreviewSingleQuestionActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("questionsList", (Serializable) allQuestions);
                    args.putSerializable("survey", survey);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("questionNumber", number + 1);
                    intent.putExtra("userId", userId);
                    intent.putExtra("group", group);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    onBackPressed();
                }
            }
        });
    }

    private void setupQuestion() {
        questionNumber.setText((number + 1) + " of " + allQuestions.size());
        questionContent.setText(allQuestions.get(number).getQuestionContent());
        typeOfQuestion = allQuestions.get(number).getQuestionType();
        if (typeOfQuestion.equals("Picklist")) {
            listViewOfAsnwers.setVisibility(View.VISIBLE);
            answerContent.setVisibility(View.INVISIBLE);
            adapter = new CustomAnswerAdapter(this, allQuestions.get(number).getPicklistValues(), false);
            Log.e("test", allQuestions.get(number).getPicklistValues().toString());
            listViewOfAsnwers.setAdapter(adapter);
        } else if (typeOfQuestion.equals("Checkbox")) {
            listViewOfAsnwers.setVisibility(View.VISIBLE);
            answerContent.setVisibility(View.INVISIBLE);
            adapter = new CustomAnswerAdapter(this, StaticResources.checkboxValues, false);
            Log.e("test", allQuestions.get(number).getPicklistValues().toString());
            listViewOfAsnwers.setAdapter(adapter);
        } else {
            listViewOfAsnwers.setVisibility(View.INVISIBLE);
            answerContent.setVisibility(View.VISIBLE);
        }

        final int divide = 100 / allQuestions.size() * (number + 1);
        mProgressStatus += divide;
        mProgressBar.setProgress(mProgressStatus);
        if (number + 1 == allQuestions.size()) {
            mProgressBar.setProgress(100);
            nextQuestionBtn.setText("Close Preview");
        }
    }


    @Override
    public void onBackPressed() {
        backToGroupView();
    }

    private void backToGroupView() {
        Intent intent = new Intent(PreviewSingleQuestionActivity.this, CreateSurveyActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("questionsList", (Serializable) allQuestions);
        args.putSerializable("survey", survey);
        intent.putExtra("BUNDLE", args);
        intent.putExtra("userId", userId);
        intent.putExtra("group", group);
        intent.putExtra("flag", true);
        startActivity(intent);
    }
}