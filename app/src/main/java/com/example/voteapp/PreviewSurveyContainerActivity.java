package com.example.voteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voteapp.model.Group;
import com.example.voteapp.model.SingleQuestion;
import com.example.voteapp.model.Survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PreviewSurveyContainerActivity extends AppCompatActivity {

    private TextView surveyTitle;
    private TextView surveyDescription;
    private TextView surveyStartTime;
    private TextView surveyEndsTime;
    private TextView surveyNumberOfQuestions;
    private TextView surveyDateOfVote;
    private TextView previewMode;
    private Button startSurvey;
    private Button backButton;
    private Button showAnswersButton;
    private Group group;
    private String userId;
    private Survey survey;
    private List<SingleQuestion> allQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_container);

        surveyTitle = findViewById(R.id.surveyTitle);
        surveyDescription = findViewById(R.id.surveyDescription);
        surveyStartTime = findViewById(R.id.surveyStartTime);
        surveyEndsTime = findViewById(R.id.surveyEndsTime);
        surveyNumberOfQuestions = findViewById(R.id.surveyNumberOfQuestions);
        surveyDateOfVote = findViewById(R.id.surveyDateOfVote);
        previewMode = findViewById(R.id.previewMode);
        startSurvey = findViewById(R.id.startSurveyBtn);
        backButton = findViewById(R.id.backButton);
        showAnswersButton = findViewById(R.id.showAnswersButton);
        previewMode.setVisibility(View.VISIBLE);
        showAnswersButton.setVisibility(View.INVISIBLE);

        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewSurveyContainerActivity.this, PreviewSingleQuestionActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("questionsList", (Serializable) allQuestions);
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
        surveyDescription.setText(survey.getSurveyDescription());
        surveyStartTime.setText(survey.getStartDate());
        surveyEndsTime.setText(survey.getEndDate());
        surveyNumberOfQuestions.setText(String.valueOf(survey.getNumberOfQuestions()));
        surveyDateOfVote.setText("Not voted yet");
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