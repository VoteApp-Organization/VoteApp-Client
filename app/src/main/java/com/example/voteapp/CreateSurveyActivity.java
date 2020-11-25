package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.CustomAnswerAdapter;
import com.example.voteapp.utils.CustomQuestionAdapter;
import com.example.voteapp.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSurveyActivity extends AppCompatActivity {
    private ListView listViewOfQuestions;
    private Button previewSurveyBtn;
    private Button cancelSurveyBtn;
    private Button shareSurveyButton;
    private String groupId;
    private String userId;
    private String groupTitle;
    private String surveyName;
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
        surveyName = intent.getStringExtra("surveyName");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateSurveyActivity.this, GroupView.class);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", groupId);
        intent.putExtra("surveyName", surveyName);
        startActivity(intent);
    }

    private void sendSurvey(final String idToken) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("voteTitle", surveyName);
        obj.put("description", "");
        obj.put("author_id", Long.valueOf(userId));

        String URL = "https://voteaplication.herokuapp.com/createNewSurvey";

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.w("CreateSurveyActivity", "createSurvey:SUCCESS" + response.toString());
                        onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("CreateSurveyActivity", "createSurvey:failure", error.getCause());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID-TOKEN", idToken);
                params.put("group_Id", groupId);
                return params;
            }
        };
        requestManager.addToRequestQueue(jsObjRequest);
    }
}