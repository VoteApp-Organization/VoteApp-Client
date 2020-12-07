package com.example.voteapp.utils;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voteapp.model.Group;
import com.example.voteapp.R;
import com.example.voteapp.model.Survey;
import com.example.voteapp.SurveyContainerActivity;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private String userId;
    private Group group;
    private final Context context;
    private final List<Survey> surveyList;

    public CustomAdapter(Context context, List<Survey> surveyList, String userId, Group group) {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.surveyList = surveyList;
        this.userId = userId;
        this.group = group;
    }

    @Override
    public int getCount() {
        return surveyList.size();
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
        final TextView titleTextView = (TextView) convertView.findViewById(R.id.surveyTitle);
        TextView numberOfQuestions = (TextView) convertView.findViewById(R.id.surveyNumberOfQuestions);
        ImageView surveyIcon = (ImageView) convertView.findViewById(R.id.surveyIcon);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.groupItemLayout);

        PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(Color.rgb(230, 230, 230), PorterDuff.Mode.MULTIPLY);

        for (int i = 0; i < surveyList.size(); i++) {
            Survey str = surveyList.get(position);
            titleTextView.setText(str.getVoteTitle());
            numberOfQuestions.setText("(" + str.getNumberOfQuestions() + " questions)");
            surveyIcon.setImageResource(StaticResources.mapOfIcons.get(str.getSurveyPicture()));
            if (str.answerHasBeenGiven != null) {
                if (str.answerHasBeenGiven) {
                    layout.getBackground().setColorFilter(greyFilter);
                }
            }
            if (userId.equals(String.valueOf(str.getAuthor_id())) && str.authorIsVoting == null) {
                layout.getBackground().setColorFilter(greyFilter);
            }
        }

        return convertView;
    }
}