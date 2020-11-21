package com.example.voteapp.utils;

import android.annotation.SuppressLint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

        System.out.println(position);
        System.out.println("TU " + surveyList.get(position));

        for (int i = 0; i < surveyList.size(); i++) {
            Survey str = surveyList.get(position);
            titleTextView.setText(str.getVoteTitle());
            String number = "10";
            numberOfQuestions.setText('(' + number + " questions)");
            surveyIcon.setImageResource(imageList.get(position));
        }

        return convertView;
    }


/*    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
     //   View rowView = inflater.inflate(R.layout.custom_groups_item, null, true);
        View rowView = LayoutInflater.from(getContext()).inflate(R.layout.custom_groups_item, parent, false);
        TextView titleText = (TextView) rowView.findViewById(R.id.textView1);
        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imageView1);
        TextView questions = (TextView) rowView.findViewById(R.id.textView2);


        titleText.setText(surveyList.get(position).getVoteTitle());
       // imageView1.setImageResource(imgid[position]);
  //      questions.setText(surveyList.get(position).getAuthor_id().toString());

        return rowView;

    }*/

    ;
}