package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    private List<TextView> textViewList = new ArrayList<>();
    private Map<Integer, String> groupMap = new HashMap<Integer, String>();
    private List<String> groupNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final TextView group1 = findViewById(R.id.group1);
        final TextView group2 = findViewById(R.id.group2);
        final TextView group3 = findViewById(R.id.group3);
        final TextView group4 = findViewById(R.id.group4);

        textViewList.add(group1);
        textViewList.add(group2);
        textViewList.add(group3);
        textViewList.add(group4);
        checkVisibility();

        group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group1.getId()));
                intent.putExtra("groupTitle", groupNames.get(0));
                startActivity(intent);
            }
        });

        group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group2.getId()));
                intent.putExtra("groupTitle", groupNames.get(1));
                startActivity(intent);
            }
        });

        group3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group3.getId()));
                intent.putExtra("groupTitle", groupNames.get(2));
                startActivity(intent);
            }
        });

        group4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group4.getId()));
                intent.putExtra("groupTitle", groupNames.get(3));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        getUserInfoApiRequest(userId);
    }

    private void parseUserGroups(Object response) throws JSONException {
        for (TextView textView : textViewList) {
            textView.setText("");
        }
        JSONArray jsonArray = ((JSONArray) response);
        for (int i = 0; i < jsonArray.length(); i++) {
            String name = jsonArray.getJSONObject(i).getString("name");
            String id = jsonArray.getJSONObject(i).getString("id");
            if (i < 4) {
                textViewList.get(i).setText(name);
            }
            groupMap.put(textViewList.get(i).getId(), id);
            groupNames.add(name);
        }
        checkVisibility();
    }

    private void checkVisibility() {
        for (TextView textView : textViewList) {
            if (textView.getText().toString().isEmpty()) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }


    private void getUserInfoApiRequest(String userId) {
        RequestManager requestManager = RequestManager.getInstance(this);

        String URL = "https://voteaplication.herokuapp.com/getUserGroups/" + userId;

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