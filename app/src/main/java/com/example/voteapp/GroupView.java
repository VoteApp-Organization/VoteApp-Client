package com.example.voteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.CustomAdapter;
import com.example.voteapp.utils.CustomSpinner;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupView extends AppCompatActivity {
    final List<Survey> allSurveys = new ArrayList<>();
    private TextView groupTitle;
    private Button backButton;
    private Button leaveButton;
    private Button createSurveyButton;
    private ListView list;
    private String userId;
    private Dialog customDialog;
    private EditText surveyNameEditText;
    private EditText surveyDescriptionEditText;
    private TextView noSurveysTextView;
    private Button buttonCreate3;
    private Spinner spinnerIcons;
    private String selectedIcon;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        groupTitle = findViewById(R.id.groupTitle);
        backButton = findViewById(R.id.backButton);
        leaveButton = findViewById(R.id.buttonLeaveGroup);
        createSurveyButton = findViewById(R.id.createSurveyButton);
        noSurveysTextView = findViewById(R.id.noSurveysTextView);
        list = findViewById(R.id.listView);

        customDialog = new Dialog(this);
        createSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSurvey();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GroupView.this)
                        .setTitle("Leave group")
                        .setMessage("Are you sure you want to leave from this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sendPostLeaveGroup();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void createSurvey() {
        customDialog.setContentView(R.layout.custom_dialog_create_survey);
        surveyNameEditText = customDialog.findViewById(R.id.surveyNameEditText);
        surveyDescriptionEditText = customDialog.findViewById(R.id.surveyDescriptionEditText);
        spinnerIcons = customDialog.findViewById(R.id.spinnerIcons);
        buttonCreate3 = customDialog.findViewById(R.id.buttonCreate3);

        final CustomSpinner customAdapter = new CustomSpinner(getApplicationContext());
        spinnerIcons.setAdapter(customAdapter);
        spinnerIcons.setSelection(0);
        spinnerIcons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIcon = String.valueOf(parent.getAdapter().getItem(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonCreate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupView.this, CreateSurveyActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("userId", userId);
                intent.putExtra("surveyName", surveyNameEditText.getText().toString());
                intent.putExtra("surveyDesc", surveyDescriptionEditText.getText().toString());
                intent.putExtra("surveyPicture", selectedIcon);
                startActivity(intent);
                customDialog.dismiss();
            }
        });
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("group");
        userId = intent.getStringExtra("userId");
        groupTitle.setText(group.getName());
        getGroupInfoApiRequest(group.getId());
        if(group.getOwner_id().equals(userId)){
            leaveButton.setVisibility(View.INVISIBLE);
        }else{
            leaveButton.setVisibility(View.VISIBLE);
        }
    }

    private void getGroupInfoApiRequest(String groupId) {
        RequestManager requestManager = RequestManager.getInstance(this);

        String URL = "https://voteaplication.herokuapp.com/getGroupSurveys/" + groupId + "/" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("Response", response.toString());
                try {
                    parseGroupInfo(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API error: ", "#onErrorResponse in GroupView");
                if(allSurveys != null && !allSurveys.isEmpty()){
                    noSurveysTextView.setVisibility(View.INVISIBLE);
                }else{
                    noSurveysTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        requestManager.addToRequestQueue(request);
    }

    private void parseGroupInfo(Object response) throws JSONException {
        allSurveys.clear();
        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            allSurveys.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Survey.class));
        }
        
        if (allSurveys.isEmpty()) {
            noSurveysTextView.setVisibility(View.VISIBLE);
        } else {
            noSurveysTextView.setVisibility(View.INVISIBLE);
        }
        CustomAdapter adapter = new CustomAdapter(this, allSurveys, userId, group);
        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GroupView.this, Dashboard.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void sendPostLeaveGroup() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("user_Id", Long.valueOf(userId));
        obj.put("vote_Id", 10);
        obj.put("group_Id", Long.valueOf(group.getId()));

        String URL = "https://voteaplication.herokuapp.com/leaveGroup";
        Log.w("GroupView", obj.toString());

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("GroupView", "leaveGroup:failure", error.getCause());
                    }
                });
        requestManager.addToRequestQueue(jsObjRequest);
    }
}