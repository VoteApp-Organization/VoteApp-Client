package com.example.voteapp.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

import com.example.voteapp.Group;
import com.example.voteapp.GroupView;
import com.example.voteapp.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.voteapp.utils.CommonUtils.sendPostLeaveGroup;

public class CustomAllGroupsAdapter extends BaseAdapter {
    private String userId;
    private final Context context;
    private final List<Group> groups;
    private List<Integer> imageList = new ArrayList<>();
    private ImageView buttonMenu;
    private TextView titleTextView;
    private ImageView groupIcon;
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
        convertView = inflater.inflate(R.layout.custom_allgroups_item, null);
        titleTextView = convertView.findViewById(R.id.surveyTitle);
        numberOfQuestions = convertView.findViewById(R.id.surveyNumberOfQuestions);
        groupIcon = convertView.findViewById(R.id.surveyIcon);
        buttonMenu = convertView.findViewById(R.id.buttonMenu);

        Group grp = groups.get(position);
        titleTextView.setText(grp.getName());
        groupIcon.setImageResource(StaticResources.mapOfIcons.get(grp.getPicture_name()));
        numberOfQuestions.setText(grp.getDescription());
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                leaveGroupWarn(context, position, 0);
                                break;
                            case "Delete":
                                leaveGroupWarn(context, position, 1);
                                break;
                            default:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("password", groups.get(position).getGroup_password());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context,
                                        "Password has been copied to clipboard", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                prepareMenu(groups.get(position), popup);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupView.class);
                intent.putExtra("group", groups.get(position));
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
        return convertView;
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
                                notifyDataSetChanged();
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
                            Toast.makeText(context,
                                    "Group \"" + groups.get(position).getName() + "\" has been deleted", Toast.LENGTH_SHORT).show();
                            groups.remove(position);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}