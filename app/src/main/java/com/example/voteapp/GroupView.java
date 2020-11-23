package com.example.voteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.utils.CustomAdapter;
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
    private ListView list;
    private String userId;
    private String groupId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        groupTitle = findViewById(R.id.groupTitle);
        backButton = findViewById(R.id.backButton);
        leaveButton = findViewById(R.id.buttonLeaveGroup);

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

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sendPostLeaveGroup();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        title = intent.getStringExtra("groupTitle");
        userId = intent.getStringExtra("userId");
        groupTitle.setText(title);
        getGroupInfoApiRequest(groupId);

        list = (ListView) findViewById(R.id.listView);
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
                Log.e("API error: ", "#onErrorResponse in Dashboard");
            }
        });

        requestManager.addToRequestQueue(request);
    }

    private void parseGroupInfo(Object response) throws JSONException {
        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            allSurveys.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Survey.class));
           // textViewList.get(i).setText(jsonArray.getJSONObject(i).toString());
        }
        System.out.println(allSurveys.get(0).getVoteTitle());

        //   CustomAdapter customAdapter = new CustomAdapter(allSurveys);
        //    list.setAdapter(customAdapter);

        CustomAdapter adapter = new CustomAdapter(this, allSurveys, userId, groupId, title);
        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GroupView.this, Dashboard.class);
        //  setIntent.addCategory(Intent.CATEGORY_HOME);
        //   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", userId);
        intent.putExtra("groupTitle", title);
        startActivity(intent);
    }

    private void sendPostLeaveGroup() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("user_Id", Long.valueOf(userId));
        obj.put("vote_Id", 10);
        obj.put("group_Id", Long.valueOf(groupId));

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