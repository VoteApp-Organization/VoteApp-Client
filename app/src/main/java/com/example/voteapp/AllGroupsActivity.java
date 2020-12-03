package com.example.voteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.model.Group;
import com.example.voteapp.utils.CustomAllGroupsAdapter;
import com.example.voteapp.utils.CustomSpinner;
import com.example.voteapp.utils.RequestManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.voteapp.utils.CommonUtils.sendPostJoinGroup;

public class AllGroupsActivity extends AppCompatActivity {

    private List<Group> groups = new ArrayList<>();
    private String userId;
    private ListView list;
    private TextView groupTitle;
    private Button leaveButton;
    private Button createGroupButton;
    private Button buttonCreate2;
    private Button backButton;
    private Dialog customDialog;
    private Switch isPublic;
    private EditText createGroupName;
    private EditText groupDescriptionEditText;
    private EditText passwordEditText;
    private Context context;
    private Spinner spinnerIcons;
    private String selectedIcon;
    private CustomAllGroupsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        list = findViewById(R.id.listView);
        groupTitle = findViewById(R.id.groupTitle);
        leaveButton = findViewById(R.id.buttonLeaveGroup);
        createGroupButton = findViewById(R.id.createSurveyButton);
        backButton = findViewById(R.id.backButton);
        groupTitle.setText("Your groups");
        createGroupButton.setText("Create new group");
        context = this;

        customDialog = new Dialog(this);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.setContentView(R.layout.custom_dialog_create_group);
                createGroupName = customDialog.findViewById(R.id.groupNameEditText);
                groupDescriptionEditText = customDialog.findViewById(R.id.groupDescriptionEditText);
                isPublic = customDialog.findViewById(R.id.isPublic);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);
                spinnerIcons = customDialog.findViewById(R.id.spinner);

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


                buttonCreate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Group grp = new Group(createGroupName.getText().toString(), groupDescriptionEditText.getText().toString(), isPublic.isChecked(), selectedIcon, Long.valueOf(userId));
                            sendPostCreateGroup(grp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        customDialog.dismiss();
                    }
                });
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
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
        Bundle args = intent.getBundleExtra("BUNDLE");
        groups = (List<Group>) args.getSerializable("groups");
        leaveButton.setText("Join");
        adapter = new CustomAllGroupsAdapter(this, groups, userId);
        list.setAdapter(adapter);

        customDialog = new Dialog(this);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.setContentView(R.layout.custom_dialog_enter_group_password);
                passwordEditText = customDialog.findViewById(R.id.passwordEditText);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);

                buttonCreate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            sendPostJoinGroup(context, userId, "", passwordEditText.getText().toString());
                            getUserInfoApiRequest(userId);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        customDialog.dismiss();
                    }
                });
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllGroupsActivity.this, DashboardActivity.class);
        //  setIntent.addCategory(Intent.CATEGORY_HOME);
        //   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void sendPostCreateGroup(Group grp) throws JSONException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(grp);
        JSONObject obj = new JSONObject(jsonString);

        String URL = "https://voteaplication.herokuapp.com/createNewGroup";
        Log.w("Dashboard", obj.toString());

        RequestManager requestManager = RequestManager.getInstance(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        getUserInfoApiRequest(userId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("Dashboard", "createGroup:failure", error.getCause());
                    }
                });
        requestManager.addToRequestQueue(jsObjRequest);
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

    private void parseUserGroups(Object response) throws JSONException {
        groups.clear();
        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            groups.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Group.class));
        }
        CustomAllGroupsAdapter adapter = new CustomAllGroupsAdapter(this, groups, userId);
        list.setAdapter(adapter);
    }
}