package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.voteapp.model.Group;
import com.example.voteapp.model.SingleQuestion;
import com.example.voteapp.model.Survey;
import com.example.voteapp.model.SurveyResultWrapper;
import com.example.voteapp.model.VoteAnswer;
import com.example.voteapp.utils.CustomAdapter;
import com.example.voteapp.utils.CustomResultsAdapter;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyContainerActivity extends AppCompatActivity {

    private TextView surveyTitle;
    private TextView surveyDescription;
    private TextView surveyStartTime;
    private TextView surveyEndsTime;
    private TextView surveyNumberOfQuestions;
    private TextView surveyDateOfVote;
    private Button startSurvey;
    private Button backButton;
    private Button showAnswersButton;
    private Group group;
    private Survey survey;
    private String userId;
    private String title;
    private ListView chartsListView;
    final List<SingleQuestion> allQuestions = new ArrayList<>();
    final List<VoteAnswer> voteAnswers = new ArrayList<>();

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
        startSurvey = findViewById(R.id.startSurveyBtn);
        backButton = findViewById(R.id.backButton);
        showAnswersButton = findViewById(R.id.showAnswersButton);


        showAnswersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyContainerActivity.this, SurveyAnswersActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("group", group);
                intent.putExtra("survey", survey);
                intent.putExtra("surveyTitle", title);
                startActivity(intent);
            }
        });

        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startSurvey.getText().equals("Already voted")) {
                    Toast.makeText(SurveyContainerActivity.this, "You can vote only once!",
                            Toast.LENGTH_SHORT).show();
                } else if (startSurvey.getText().equals("Author can't vote")) {
                    Toast.makeText(SurveyContainerActivity.this, "Author can't vote in this survey",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SurveyContainerActivity.this, SingleQuestionActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("questionsList", (Serializable) allQuestions);
                    args.putSerializable("voteAnswers", (Serializable) voteAnswers);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("userId", userId);
                    intent.putExtra("group", group);
                    intent.putExtra("questionNumber", 0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
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
        survey = (Survey) intent.getSerializableExtra("survey");
        group = (Group) intent.getSerializableExtra("group");
        title = intent.getStringExtra("surveyTitle");
        surveyTitle.setText(title);
        surveyDescription.setText(survey.getSurveyDescription());
        surveyStartTime.setText(survey.getStartDate());
        surveyEndsTime.setText(survey.getEndDate());
        surveyNumberOfQuestions.setText(String.valueOf(survey.getNumberOfQuestions()));
        String dateOfVote = survey.voteDate;
        if (dateOfVote != null) {
            surveyDateOfVote.setText(survey.voteDate);
        } else {
            surveyDateOfVote.setText("Not voted yet");
        }
        showAnswersButton.setVisibility(View.INVISIBLE);

        if (userId.equals(String.valueOf(survey.getAuthor_id())) && survey.authorIsVoting != null && !survey.authorIsVoting) {
            startSurvey.setBackgroundResource(R.drawable.custom_button_disabled);
            startSurvey.setText("Author can't vote");
        } else if (survey.answerHasBeenGiven != null && survey.answerHasBeenGiven) {
            startSurvey.setBackgroundResource(R.drawable.custom_button_disabled);
            startSurvey.setText("Already voted");
        }

        if (userId.equals(String.valueOf(survey.getAuthor_id()))) {
            showAnswersButton.setVisibility(View.VISIBLE);
        }
        getUserInfoApiRequest();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SurveyContainerActivity.this, GroupViewActivity.class);
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
        String URL = "https://voteaplication.herokuapp.com/getQuestionsOnSurvey/" + survey.getVote_Id();

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