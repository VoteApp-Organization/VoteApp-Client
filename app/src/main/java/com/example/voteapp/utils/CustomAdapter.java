package com.example.voteapp.utils;

import android.annotation.SuppressLint;

import android.content.Context;
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

import com.example.voteapp.R;
import com.example.voteapp.Survey;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final List<Survey> surveyList;
    private List<Integer> imageList = new ArrayList<>();

    public CustomAdapter(Context context, List<Survey> surveyList) {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.surveyList = surveyList;

        imageList.add(R.drawable.politicians);
        imageList.add(R.drawable.budget);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_groups_item, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.surveyTitle);
        TextView numberOfQuestions = (TextView) convertView.findViewById(R.id.surveyNumberOfQuestions);
        ImageView surveyIcon = (ImageView) convertView.findViewById(R.id.surveyIcon);
        Button buttonOpenSurvey = (Button) convertView.findViewById(R.id.buttonOpenSurvey);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.groupItemLayout);

        System.out.println(position);
        System.out.println("TU " + surveyList.get(position));

        PorterDuffColorFilter greyFilter = new PorterDuffColorFilter(Color.rgb(230, 230, 230), PorterDuff.Mode.MULTIPLY);


        for (int i = 0; i < surveyList.size(); i++) {
            Survey str = surveyList.get(position);
            titleTextView.setText(str.getVoteTitle());
            String number = "10";
            numberOfQuestions.setText('(' + number + " questions)");
            surveyIcon.setImageResource(imageList.get(position));
            if(position != 0){
                layout.getBackground().setColorFilter(greyFilter);
                buttonOpenSurvey.setBackgroundResource(R.drawable.custom_button_disabled);
                buttonOpenSurvey.setText("Already voted");
            }
        }

        return convertView;
    }
}