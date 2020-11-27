package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyContainerActivity extends AppCompatActivity {

    private TextView surveyTitle;
    private Button startSurvey;
    private Button backButton;
    private Group group;
    private String surveyId;
    private String userId;
    private String groupTitle;
    private String title;
    final List<SingleQuestion> allQuestions = new ArrayList<>();
    final List<VoteAnswer> voteAnswers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_container);

        surveyTitle = findViewById(R.id.surveyTitle);
        startSurvey = findViewById(R.id.startSurveyBtn);
        backButton = findViewById(R.id.backButton);

        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyContainerActivity.this, SingleQuestionActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("questionsList",(Serializable)allQuestions);
                args.putSerializable("voteAnswers",(Serializable)voteAnswers);
                intent.putExtra("BUNDLE", args);
                intent.putExtra("userId", userId);
                intent.putExtra("group", group);
                intent.putExtra("questionNumber", 0);
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
        surveyId = intent.getStringExtra("surveyId");
        group = (Group) intent.getSerializableExtra("group");
        title = intent.getStringExtra("surveyTitle");
        surveyTitle.setText(title);

        getUserInfoApiRequest();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SurveyContainerActivity.this, GroupView.class);
        intent.putExtra("userId", userId);
        intent.putExtra("group", group);
        startActivity(intent);
    }

    private void parseUserGroups(Object response) throws JSONException {
        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            allQuestions.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), SingleQuestion.class));
        }
    }

    private void getUserInfoApiRequest() {
        RequestManager requestManager = RequestManager.getInstance(this);
        System.out.println(surveyId);
        String URL = "https://voteaplication.herokuapp.com/getQuestionsOnSurvey/" + surveyId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("Response", response.toString());
                try {
                    parseUserGroups(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API error: ", "#onErrorResponse in Dashboard");
            }
        });

        requestManager.addToRequestQueue(request);
    }
}