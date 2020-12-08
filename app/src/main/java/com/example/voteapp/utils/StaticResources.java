package com.example.voteapp.utils;

import com.example.voteapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticResources {
    public static Map<String, Integer> mapOfIcons = new HashMap<String, Integer>()
    {{
        put("budget", R.drawable.budget);
        put("tools", R.drawable.tools);
        put("flag", R.drawable.flag);
        put("politicians", R.drawable.politicians);
        put("city", R.drawable.city);
        put("party", R.drawable.party);
        put("school", R.drawable.school);
        put("dorm", R.drawable.dorm);
    }};

    public static String[] questionTypes = { "Text", "Picklist",
            "Checkbox"};

    public static List<String> checkboxValues = new ArrayList<String>() {{
        add("True");
        add("False");
    }};
}
