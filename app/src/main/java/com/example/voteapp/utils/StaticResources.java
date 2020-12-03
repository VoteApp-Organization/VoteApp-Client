package com.example.voteapp.utils;

import com.example.voteapp.R;
import java.util.HashMap;
import java.util.Map;

public class StaticResources {
    public static Map<String, Integer> mapOfIcons = new HashMap<String, Integer>()
    {{
        put("budget", R.drawable.budget);
        put("tools", R.drawable.tools);
        put("politicians", R.drawable.politicians);
        put("city", R.drawable.city);
        put("school", R.drawable.school);
        put("flag", R.drawable.flag);
        put("party", R.drawable.party);
        put("dorm", R.drawable.dorm);
    }};
}
