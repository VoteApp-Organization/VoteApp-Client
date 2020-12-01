package com.example.voteapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.voteapp.R;

public class CustomSpinner extends BaseAdapter {
    Context context;
    String[] myArray;


    public CustomSpinner(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return StaticResources.mapOfIcons.keySet().size();
    }

    @Override
    public Object getItem(int i) {
        return myArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_spinner_icons, null);
        ImageView icon = (ImageView) view.findViewById(R.id.image_view_eclipse);
        myArray = new String[StaticResources.mapOfIcons.keySet().size()];
        StaticResources.mapOfIcons.keySet().toArray(myArray);
        icon.setImageResource(StaticResources.mapOfIcons.get(myArray[i]));
        return view;
    }
}
