package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.CustomAnswerAdapter;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SingleQuestionActivity extends AppCompatActivity {
    private TextView questionNumber;
    private TextView questionContent;
    private EditText answerContent;
    private ListView listViewOfAsnwers;
    private Button nextQuestionBtn;
    private String userId;
    private Group group;
    private int number;
    private List<SingleQuestion> allQuestions = new ArrayList<>();
    private List<VoteAnswer> voteAnswers = new ArrayList<>();
    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;
    private String typeOfQuestion;
    CustomAnswerAdapter adapter;

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
        group = (Group) intent.getSerializableExtra("group");
        userId = intent.getStringExtra("userId");
        number = intent.getIntExtra("questionNumber", 0);
        Bundle args = intent.getBundleExtra("BUNDLE");
        allQuestions = (List<SingleQuestion>) args.getSerializable("questionsList");
        voteAnswers = (List<VoteAnswer>) args.getSerializable("voteAnswers");

        setupQuestion();

        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
                if (nextQuestionBtn.getText().toString().trim().equals("Next question")) {
                    Intent intent = new Intent(SingleQuestionActivity.this, SingleQuestionActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("questionsList", (Serializable) allQuestions);
                    args.putSerializable("voteAnswers", (Serializable) voteAnswers);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("userId", userId);
                    intent.putExtra("group", group);
                    intent.putExtra("questionNumber", number + 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    try {
                        sendPostRequestToSaveAnswers();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveAnswer() {
        List<String> listOfAnswers = new ArrayList<>();
        if (typeOfQuestion.equals("Picklist")) {
            listOfAnswers = adapter.getClickedItems();
        } else {
            listOfAnswers.add(answerContent.getText().toString());
        }
        VoteAnswer voteAnswer = new VoteAnswer(allQuestions.get(number).getId(), allQuestions.get(number).getVote_id(), Long.valueOf(userId), listOfAnswers);
        voteAnswers.add(voteAnswer);
    }

    private void setupQuestion() {
        questionNumber.setText((number + 1) + " of " + allQuestions.size());
        questionContent.setText(allQuestions.get(number).getQuestionContent());
        typeOfQuestion = allQuestions.get(number).getQuestionType();
        if (typeOfQuestion.equals("Picklist")) {
            listViewOfAsnwers.setVisibility(View.VISIBLE);
            answerContent.setVisibility(View.INVISIBLE);
            adapter = new CustomAnswerAdapter(this, allQuestions.get(number).getPicklistValues(), false);
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
            nextQuestionBtn.setText("Submit survey");
        }
    }

    private void sendPostRequestToSaveAnswers() throws JSONException {
        Gson gson = new Gson();
        String element = gson.toJson(
                voteAnswers,
                new TypeToken<ArrayList<VoteAnswer>>() {
                }.getType());
        JSONArray list = new JSONArray(element);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("answers", list);
        Log.e("SingleQuestionActivity", "" + jsonObject);

        String URL = "https://voteaplication.herokuapp.com/saveAnswers";
        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        backToGroupView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("SingleQuestionActivity", "sendPostRequestToSaveAnswers:failure", error.getCause());
                        Toast.makeText(SingleQuestionActivity.this, "Submitting failed",
                                Toast.LENGTH_LONG).show();
                    }
                });
        requestManager.addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        backToGroupView();
    }

    private void backToGroupView() {
        Intent intent = new Intent(SingleQuestionActivity.this, GroupView.class);
        intent.putExtra("userId", userId);
        intent.putExtra("group", group);
        startActivity(intent);
    }
}