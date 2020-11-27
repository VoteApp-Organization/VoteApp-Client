package com.example.voteapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voteapp.Group;
import com.example.voteapp.GroupView;
import com.example.voteapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAllGroupsAdapter extends BaseAdapter {
    private String userId;
    private final Context context;
    private final List<Group> groups;
    private List<Integer> imageList = new ArrayList<>();
    private Button buttonOpenGroup;
    private TextView titleTextView;
    private ImageView surveyIcon;
    private TextView publicPrivate;
    private TextView numberOfQuestions;

    public CustomAllGroupsAdapter(Context context, List<Group> groups, String userId) {
        this.context = context;
        this.groups = groups;
        this.userId = userId;

        Log.e("ads", groups.toString());

        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);

    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_groups_item, null);
        titleTextView = convertView.findViewById(R.id.surveyTitle);
        numberOfQuestions = convertView.findViewById(R.id.surveyNumberOfQuestions);
        publicPrivate = convertView.findViewById(R.id.publicPrivate);
        surveyIcon = convertView.findViewById(R.id.surveyIcon);
        buttonOpenGroup = convertView.findViewById(R.id.buttonOpenSurvey);

        buttonOpenGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupView.class);
                intent.putExtra("group", groups.get(position));
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
        Group grp = groups.get(position);
        System.out.println(grp.getGroupPassword());
        titleTextView.setText(grp.getName());
        numberOfQuestions.setText(grp.getDescription());
        //  surveyIcon.setImageResource(grp.getpictureName());
        buttonOpenGroup.setText("Open group");
           /* if (grp.getPublic()) {
                publicPrivate.setText("Public");
            } else {
                publicPrivate.setText(grp.getGroupPassword());
            }*/

        return convertView;
    }
}