package com.example.voteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.voteapp.model.Group;
import com.example.voteapp.utils.RequestManager;
import com.example.voteapp.utils.StaticResources;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.voteapp.utils.CommonUtils.sendPostJoinGroup;

public class ExploreActivity extends AppCompatActivity {
    private List<Group> groups = new ArrayList<>();
    private Button backButton;
    private LinearLayout exploreLayout;
    private String userId;
    private View convertView;
    private LayoutInflater inflater;
    private Context context;
    private View.OnClickListener onClickListenerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        backButton = findViewById(R.id.backButton);
        exploreLayout = findViewById(R.id.exploreLayout);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_group_item, null);
        context = this;
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
        getAllPublicGroups();
        refresh();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExploreActivity.this, DashboardActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void parseUserGroups(Object response) throws JSONException {
        groups.clear();
        JSONArray jsonArray = ((JSONArray) response);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.length(); i++) {
            groups.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Group.class));
        }
        refresh();
    }

    private void getAllPublicGroups() {
        RequestManager requestManager = RequestManager.getInstance(this);

        String URL = "https://voteaplication.herokuapp.com/exploreGroupsByName/" + userId + "/";

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

    private void refresh() {
        exploreLayout.removeAllViews();
        for (int i = 0; i < groups.size(); i++) {
            final View line = convertView.inflate(this, R.layout.custom_group_item, null);
            TextView titleTextView = line.findViewById(R.id.groupTitle);
            ImageView icon = line.findViewById(R.id.groupIcon);
            ImageView menu = line.findViewById(R.id.buttonMenu);
            menu.setVisibility(View.GONE);
            titleTextView.setText(groups.get(i).getName());
            icon.setImageResource(StaticResources.mapOfIcons.get(groups.get(i).getPicture_name()));
            RelativeLayout groupItemLayout1 = line.findViewById(R.id.groupItemLayout);
            RelativeLayout groupItemLayout2 = line.findViewById(R.id.groupItemLayout2);
            groupItemLayout1.setOnClickListener(onClickListenerMenu);
            groupItemLayout1.setTag(i);
            i++;
            if (i < groups.size()) {
                groupItemLayout2.setOnClickListener(onClickListenerMenu);
                groupItemLayout2.setTag(i);
                ImageView menu2 = line.findViewById(R.id.buttonMenu2);
                menu2.setVisibility(View.GONE);
                groupItemLayout2.setVisibility(View.VISIBLE);
                TextView titleTextView2 = line.findViewById(R.id.groupTitle2);
                ImageView icon2 = line.findViewById(R.id.groupIcon2);
                titleTextView2.setText(groups.get(i).getName());
                icon2.setImageResource(StaticResources.mapOfIcons.get(groups.get(i).getPicture_name()));
            } else {
                groupItemLayout2.setVisibility(View.INVISIBLE);
            }
            exploreLayout.addView(line);
        }

        onClickListenerMenu = new View.OnClickListener() {
            public void onClick(View v) {
                final int i = (int) v.getTag();
                Log.e("asd", String.valueOf(i));

                new AlertDialog.Builder(ExploreActivity.this)
                        .setTitle("Join to group")
                        .setMessage("Are you sure you want to join to this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sendPostJoinGroup(context, userId, groups.get(i).getId(), "");
                                    Intent intent = new Intent(ExploreActivity.this, GroupViewActivity.class);
                                    intent.putExtra("group", groups.get(i));
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };
    }
}