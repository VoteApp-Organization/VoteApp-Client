package com.example.voteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.voteapp.model.Group;
import com.example.voteapp.utils.CustomSpinner;
import com.example.voteapp.utils.RequestManager;
import com.example.voteapp.utils.StaticResources;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.voteapp.utils.CommonUtils.sendPostDeleteGroup;
import static com.example.voteapp.utils.CommonUtils.sendPostJoinGroup;
import static com.example.voteapp.utils.CommonUtils.sendPostLeaveGroup;

public class AllGroupsActivity extends AppCompatActivity {

    private List<Group> groups = new ArrayList<>();
    private String userId;
    private TextView groupTitle;
    private Button joinGroupButton;
    private Button createGroupyButton;
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
    private LinearLayout exploreLayout;
    private View convertView;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListenerItem;
    private View.OnClickListener onClickListenerMenu;
    private ImageView menu;
    private ImageView menu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_groups);
        exploreLayout = findViewById(R.id.exploreLayout);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_group_item, null);
        groupTitle = findViewById(R.id.groupTitle);
        joinGroupButton = findViewById(R.id.buttonJoinGroup);
        createGroupyButton = findViewById(R.id.createGroupyButton);
        backButton = findViewById(R.id.backButton);
        groupTitle.setText("Your groups");
        createGroupyButton.setText("Create new group");
        context = this;

        customDialog = new Dialog(this);
        createGroupyButton.setOnClickListener(new View.OnClickListener() {
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
                        if (validateForm()) {
                            try {
                                Group grp = new Group(createGroupName.getText().toString(), groupDescriptionEditText.getText().toString(), isPublic.isChecked(), selectedIcon, Long.valueOf(userId));
                                sendPostCreateGroup(grp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            customDialog.dismiss();
                        } else {
                            Toast.makeText(AllGroupsActivity.this, "Please, fill missing fields",
                                    Toast.LENGTH_SHORT).show();
                        }
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
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Bundle args = intent.getBundleExtra("BUNDLE");
        groups = (List<Group>) args.getSerializable("groups");
        refresh();

        customDialog = new Dialog(this);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.setContentView(R.layout.custom_dialog_enter_group_password);
                passwordEditText = customDialog.findViewById(R.id.passwordEditText);
                buttonCreate2 = customDialog.findViewById(R.id.buttonCreate2);

                buttonCreate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!passwordEditText.getText().toString().isEmpty()) {
                            try {
                                sendPostJoinGroup(context, userId, "", passwordEditText.getText().toString());
                                getUserInfoApiRequest(userId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            customDialog.dismiss();
                        }else{
                            Toast.makeText(AllGroupsActivity.this, "Please, fill missing password",
                                    Toast.LENGTH_SHORT).show();
                        }
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
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private boolean validateForm() {
        return (!createGroupName.getText().toString().isEmpty()) && (!groupDescriptionEditText.getText().toString().isEmpty());
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
        refresh();
    }

    private void refresh() {
        exploreLayout.removeAllViews();
        for (int i = 0; i < groups.size(); i++) {
            final int finalI = i;
            final View line = convertView.inflate(this, R.layout.custom_group_item, null);
            TextView titleTextView = line.findViewById(R.id.groupTitle);
            TextView groupNumberOfSurveys = line.findViewById(R.id.groupNumberOfSurveys);
            groupNumberOfSurveys.setVisibility(View.GONE);
            TextView groupNumberOfMembers = line.findViewById(R.id.groupNumberOfMembers);
            if (groups.get(i).getDescription().equals("")) {
                groupNumberOfMembers.setText("No description");
            } else {
                groupNumberOfMembers.setText(groups.get(i).getDescription());
            }
            ImageView icon = line.findViewById(R.id.groupIcon);
            menu = line.findViewById(R.id.buttonMenu);
            menu.setVisibility(View.VISIBLE);
            menu.setOnClickListener(onClickListenerMenu);
            menu.setTag(i);
            titleTextView.setText(groups.get(i).getName());
            icon.setImageResource(StaticResources.mapOfIcons.get(groups.get(i).getPicture_name()));
            RelativeLayout groupItemLayout1 = line.findViewById(R.id.groupItemLayout);
            groupItemLayout1.setOnClickListener(onClickListenerItem);
            groupItemLayout1.setTag(i);
            RelativeLayout groupItemLayout2 = line.findViewById(R.id.groupItemLayout2);
            i++;
            if (i < groups.size()) {
                groupItemLayout2.setOnClickListener(onClickListenerItem);
                groupItemLayout2.setTag(i);
                TextView groupNumberOfSurveys2 = line.findViewById(R.id.groupNumberOfSurveys2);
                groupNumberOfSurveys2.setVisibility(View.GONE);
                TextView groupNumberOfMembers2 = line.findViewById(R.id.groupNumberOfMembers2);
                if (groups.get(i).getDescription().equals("")) {
                    groupNumberOfMembers2.setText("No description");
                } else {
                    groupNumberOfMembers2.setText(groups.get(i).getDescription());
                }
                menu2 = line.findViewById(R.id.buttonMenu2);
                menu2.setVisibility(View.VISIBLE);
                menu2.setOnClickListener(onClickListenerMenu);
                menu2.setTag(i);
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

        onClickListenerItem = new View.OnClickListener() {
            public void onClick(View v) {
                final int i = (int) v.getTag();
                Intent intent = new Intent(AllGroupsActivity.this, GroupViewActivity.class);
                intent.putExtra("group", groups.get(i));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        };

        onClickListenerMenu = new View.OnClickListener() {
            public void onClick(View v) {
                final int i = (int) v.getTag();
                PopupMenu popup = new PopupMenu(context, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case "Edit":
                                Toast.makeText(context,
                                        item.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                            case "Leave":
                                leaveGroupWarn(context, i, 0);
                                break;
                            case "Delete":
                                leaveGroupWarn(context, i, 1);
                                break;
                            default:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("password", groups.get(i).getGroup_password());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context,
                                        "Password has been copied to clipboard", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                prepareMenu(groups.get(i), popup);
            }
        };
    }

    private void prepareMenu(Group grp, PopupMenu popup) {
        String operation;
        if (userId.equals(String.valueOf(grp.getOwner_id()))) {
            popup.getMenu().add("Edit");
            operation = "Delete";
        } else {
            operation = "Leave";
        }
        if (!grp.getIs_public()) {
            popup.getMenu().add("Password: " + grp.getGroup_password());
        }
        SpannableString itemRecall = new SpannableString(operation);
        itemRecall.setSpan(new ForegroundColorSpan(Color.RED), 0, itemRecall.length(), 0);
        popup.getMenu().add(Menu.NONE, popup.getMenu().size(), 1, itemRecall);
        popup.setGravity(Gravity.END);
        popup.show();
    }

    private void leaveGroupWarn(final Context context, final int position, final int operation) {
        if (operation == 0) {
            new AlertDialog.Builder(context)
                    .setTitle("Leave group")
                    .setMessage("Are you sure you want to leave from this group?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                sendPostLeaveGroup(context, userId, groups.get(position).getId());
                                Toast.makeText(context,
                                        "Leaved group \"" + groups.get(position).getName() + "\"", Toast.LENGTH_SHORT).show();
                                groups.remove(position);
                                refresh();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("Delete group")
                    .setMessage("Are you sure you want to delete this group?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                sendPostDeleteGroup(context, userId, groups.get(position).getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(context,
                                    "Group \"" + groups.get(position).getName() + "\" has been deleted", Toast.LENGTH_SHORT).show();
                            groups.remove(position);
                            refresh();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}