package com.example.voteapp.utils;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.voteapp.R;
import java.util.ArrayList;
import java.util.List;

public class CustomAnswerAdapter extends BaseAdapter {
    private List<String> possibleAnswers;
    private Context context;
    private TextView titleTextView;
    private List<Integer> clickedAnswers = new ArrayList<>();
    private boolean multipleChoice;

    public CustomAnswerAdapter(Context context, List<String> possibleAnswers, boolean multipleChoice) {
        // TODO Auto-generated constructor stub
        this.multipleChoice = multipleChoice;
        this.context = context;
        this.possibleAnswers = possibleAnswers;
    }

    @Override
    public int getCount() {
        return possibleAnswers.size();
    }

    @Override
    public Object getItem(int position) {

        return titleTextView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<String> getClickedItems() {
        List<String> answers = new ArrayList<>();
        for(Integer position : clickedAnswers){
            answers.add(possibleAnswers.get(position));
        }
        return answers;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_answer_item, null);
        titleTextView = (TextView) convertView.findViewById(R.id.singleAnswerContent);
        final View finalConvertView = convertView;

        titleTextView.setText(possibleAnswers.get(position));
        if (clickedAnswers.contains(position)) {
            finalConvertView.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_single_answer_container_clicked));
            titleTextView.setTextColor(Color.WHITE);
        } else {
            finalConvertView.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_single_answer_container));
            titleTextView.setTextColor(Color.GRAY);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedAnswers.contains(position)) {
                    clickedAnswers.remove(position);
                } else {
                    if(!multipleChoice){
                        clickedAnswers.clear();
                    }
                    clickedAnswers.add(position);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}