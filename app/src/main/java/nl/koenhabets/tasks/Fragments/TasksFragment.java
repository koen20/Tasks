package nl.koenhabets.tasks.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import nl.koenhabets.tasks.Data;
import nl.koenhabets.tasks.ReminderItem;
import nl.koenhabets.tasks.TagItem;
import nl.koenhabets.tasks.activities.AddTaskActivity;
import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TaskItem;
import nl.koenhabets.tasks.adapters.TasksAdapter;

import static nl.koenhabets.tasks.Data.getTags;

public class TasksFragment extends Fragment {
    private ListView listView;
    private final List<TaskItem> taskItems = new ArrayList<>();
    private TasksAdapter adapter;
    private int positionL;
    private DatabaseReference database;
    private String userId;
    private boolean showCompleted;
    private List<TagItem> tagItemList = new ArrayList<>();

    public TasksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        adapter = new TasksAdapter(getContext(), taskItems);
        listView = rootView.findViewById(R.id.tasksListview);
        listView.setAdapter(adapter);

        showCompleted = getArguments().getBoolean("showCompleted");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();

        database = FirebaseDatabase.getInstance().getReference();
        tagItemList = getTags(database, userId);

        database.child("users").child(userId).child("items").orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskItems.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String subject = (String) snap.child("subject").getValue();
                    String id = (String) snap.child("id").getValue();
                    boolean completed = false;
                    long priority;
                    long date;
                    List<String> tags = new ArrayList<>();
                    List<ReminderItem> reminders = new ArrayList<>();
                    try {
                        completed = (Boolean) snap.child("completed").getValue();
                    } catch (NullPointerException ignored) {
                    }
                    try {
                        priority = (long) snap.child("priority").getValue();
                    } catch (NullPointerException ignored) {
                        priority = 1;
                    }
                    try {
                        date = (long) snap.child("date").getValue();
                    } catch (NullPointerException ignored) {
                        date = 0;
                    }
                    try {
                        tags = (List<String>) snap.child("tags").getValue();
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                    try {
                        reminders = (List<ReminderItem>) snap.child("reminders").getValue();
                    } catch (ClassCastException ignored) {
                    }

                    if (showCompleted && completed) {
                        TaskItem item = new TaskItem(subject, date, (int) priority, true, id, Data.getListTagsString(tags, tagItemList), reminders);
                        taskItems.add(item);
                    } else if (!showCompleted && !completed) {
                        TaskItem item = new TaskItem(subject, date, (int) priority, false, id, Data.getListTagsString(tags, tagItemList), reminders);
                        taskItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TaskFragment", "Failed to read value.", error.toException());
            }
        });

        adapter.notifyDataSetChanged();


        FloatingActionButton fab = rootView.findViewById(R.id.fabAddTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(intent, 989);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                positionL = pos;
                DialogFragment newFragment = RemoveTaskDialogFragment.newInstance(taskItems.get(pos).getId());
                newFragment.show(getFragmentManager(), "Task");
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                Gson gson = new Gson();
                intent.putExtra("task", gson.toJson(taskItems.get(pos)));
                startActivityForResult(intent, 989);
            }
        });

        return rootView;
    }
}
