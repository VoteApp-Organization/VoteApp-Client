package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.voteapp.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    private List<TextView> textViewList = new ArrayList<>();
    private List<LinearLayout> cardGroupsList = new ArrayList<>();
    private Map<Integer, String> groupMap = new HashMap<Integer, String>();
    private List<String> groupNames = new ArrayList<>();
    private String userId;
    private Button buttonCreate;
    private Button buttonCreate2;
    private Dialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        buttonCreate = findViewById(R.id.buttonCreate);

        final LinearLayout groupCard1 = findViewById(R.id.cardGroup1);
        final LinearLayout groupCard2 = findViewById(R.id.cardGroup2);
        final LinearLayout groupCard3 = findViewById(R.id.cardGroup3);
        final LinearLayout groupCard4 = findViewById(R.id.cardGroup4);

        cardGroupsList.add(groupCard1);
        cardGroupsList.add(groupCard2);
        cardGroupsList.add(groupCard3);
        cardGroupsList.add(groupCard4);


        final TextView group1 = findViewById(R.id.group1);
        final TextView group2 = findViewById(R.id.group2);
        final TextView group3 = findViewById(R.id.group3);
        final TextView group4 = findViewById(R.id.group4);

        textViewList.add(group1);
        textViewList.add(group2);
        textViewList.add(group3);
        textViewList.add(group4);
        checkVisibility();

        groupCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group1.getId()));
                intent.putExtra("groupTitle", groupNames.get(0));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group2.getId()));
                intent.putExtra("groupTitle", groupNames.get(1));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group3.getId()));
                intent.putExtra("groupTitle", groupNames.get(2));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        groupCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, GroupView.class);
                intent.putExtra("groupId", groupMap.get(group4.getId()));
                intent.putExtra("groupTitle", groupNames.get(3));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        customDialog = new Dialog(this);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.setContentView(R.layout.custom_dialog);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);
                buttonCreate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });

                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.e("Dashboard", "user Id= " + userId);
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
        for (int i = 0; i < textViewList.size(); i++){
            if (textViewList.get(i).getText().toString().isEmpty()) {
                cardGroupsList.get(i).setVisibility(View.INVISIBLE);
            } else {
                cardGroupsList.get(i).setVisibility(View.VISIBLE);
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