package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.model.Group;
import com.example.voteapp.model.Survey;
import com.example.voteapp.model.SurveyResultWrapper;
import com.example.voteapp.utils.CustomResultsAdapter;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SurveyAnswersActivity extends AppCompatActivity {
    private TextView surveyTitle;
    private ListView chartsListView;
    private Button backButton;
    private Group group;
    private Survey survey;
    private String userId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_answers);

        surveyTitle = findViewById(R.id.surveyTitle);
        chartsListView = findViewById(R.id.chartsListView);
        backButton = findViewById(R.id.backButton);

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
        survey = (Survey) intent.getSerializableExtra("survey");
        group = (Group) intent.getSerializableExtra("group");
        title = intent.getStringExtra("surveyTitle");
        surveyTitle.setText(title);
        getSurveyAnswersRequest();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SurveyAnswersActivity.this, SurveyContainerActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("group", group);
        intent.putExtra("survey", survey);
        intent.putExtra("surveyTitle", title);
        startActivity(intent);
    }

    private void parseSurveyAnswers(Object response){
        JSONObject jsonObject = ((JSONObject) response);
        Gson gson = new Gson();
        SurveyResultWrapper srw = gson.fromJson(jsonObject.toString(), SurveyResultWrapper.class);
        CustomResultsAdapter adapter = new CustomResultsAdapter(this, srw);
        chartsListView.setAdapter(adapter);
    }

    private void getSurveyAnswersRequest() {
        RequestManager requestManager = RequestManager.getInstance(this);
        String URL = "https://voteaplication.herokuapp.com/getSurveyAnswers/" + survey.getVote_Id();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("Response", response.toString());
                parseSurveyAnswers(response);
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