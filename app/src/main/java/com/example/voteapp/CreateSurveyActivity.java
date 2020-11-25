package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.voteapp.utils.CustomAnswerAdapter;
import com.example.voteapp.utils.CustomQuestionAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateSurveyActivity extends AppCompatActivity {
    private ListView listViewOfQuestions;
    private Button previewSurveyBtn;
    private Button cancelSurveyBtn;
    private Button shareSurveyButton;
    private String groupId;
    private String userId;
    private String groupTitle;
    private List<SingleQuestion> allQuestions = new ArrayList<>();
    private List<String> addedQuestions = new ArrayList<>();
    private List<String> addedQuestions2 = new ArrayList<>();
    private CustomQuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);

        previewSurveyBtn = findViewById(R.id.buttonPreviewSurvey);
        cancelSurveyBtn = findViewById(R.id.buttonCancelSurvey);
        shareSurveyButton = findViewById(R.id.createSurveyShare);

        addedQuestions2.add("Answer 1");
        addedQuestions2.add("Answer 2");
        addedQuestions2.add("Answer 3");

        addedQuestions.add("Answer 1");
        addedQuestions.add("Answer 2");
        addedQuestions.add("Answer 3");
        addedQuestions.add("Answer 4");
        addedQuestions.add("Answer 5");

        allQuestions.add(new SingleQuestion(Long.parseLong(String.valueOf(10)), Long.parseLong(String.valueOf(10)), "Question 1", false, true, 50, "String", null));
        allQuestions.add(new SingleQuestion(Long.parseLong(String.valueOf(11)), Long.parseLong(String.valueOf(10)), "Question 2", false, true, 50, "Picklist", addedQuestions));
        allQuestions.add(new SingleQuestion(Long.parseLong(String.valueOf(12)), Long.parseLong(String.valueOf(10)), "Question 3", false, true, 50, "String", null));
        allQuestions.add(new SingleQuestion(Long.parseLong(String.valueOf(13)), Long.parseLong(String.valueOf(10)), "Question 4", false, true, 50, "Picklist", addedQuestions2));
        allQuestions.add(new SingleQuestion(Long.parseLong(String.valueOf(14)), Long.parseLong(String.valueOf(10)), "Question 5", false, true, 50, "String", null));
        allQuestions.add(new SingleQuestion(Long.parseLong(String.valueOf(15)), Long.parseLong(String.valueOf(10)), "Question 6", false, true, 50, "Picklist", addedQuestions));

        listViewOfQuestions = findViewById(R.id.questionsListView);
        adapter = new CustomQuestionAdapter(this, allQuestions);
        listViewOfQuestions.setAdapter(adapter);

        previewSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listViewOfQuestions.setVisibility(View.INVISIBLE);
            }
        });

        cancelSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shareSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        userId = intent.getStringExtra("userId");
        groupTitle = intent.getStringExtra("groupTitle");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateSurveyActivity.this, GroupView.class);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupTitle", groupTitle);
        startActivity(intent);
    }
}