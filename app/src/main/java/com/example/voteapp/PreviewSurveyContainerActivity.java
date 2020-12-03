package com.example.voteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PreviewSurveyContainerActivity extends AppCompatActivity {

    private TextView surveyTitle;
    private TextView previewMode;
    private Button startSurvey;
    private Button backButton;
    private Group group;
    private String userId;
    private Survey survey;
    private List<SingleQuestion> allQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_container);

        surveyTitle = findViewById(R.id.surveyTitle);
        previewMode = findViewById(R.id.previewMode);
        startSurvey = findViewById(R.id.startSurveyBtn);
        backButton = findViewById(R.id.backButton);
        previewMode.setVisibility(View.VISIBLE);

        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewSurveyContainerActivity.this, PreviewSingleQuestionActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("questionsList",(Serializable)allQuestions);
                args.putSerializable("survey", survey);
                intent.putExtra("BUNDLE", args);
                intent.putExtra("questionNumber", 0);
                intent.putExtra("userId", userId);
                intent.putExtra("group", group);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        group = (Group) intent.getSerializableExtra("group");
        Bundle args = intent.getBundleExtra("BUNDLE");
        survey = (Survey) args.getSerializable("survey");
        allQuestions = (List<SingleQuestion>) args.getSerializable("questionsList");
        surveyTitle.setText(survey.getVoteTitle());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PreviewSurveyContainerActivity.this, CreateSurveyActivity.class);
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