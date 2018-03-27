package nl.koenhabets.tasks.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.koenhabets.tasks.activities.AddTaskActivity;
import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TaskItem;
import nl.koenhabets.tasks.adapters.TasksAdapter;

public class TasksFragment extends Fragment {
    private ListView listView;
    private final List<TaskItem> taskItems = new ArrayList<>();
    private TasksAdapter adapter;
    private int positionL;
    private DatabaseReference database;
    private String userId;
    private boolean showCompleted;
    private JSONArray jsonArrayTags = new JSONArray();

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
        getTags();

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
                    String tags;
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
                        tags = (String) snap.child("tags").getValue();
                    } catch (NullPointerException ignored) {
                        tags = "";
                    }

                    if (showCompleted && completed) {
                        TaskItem item = new TaskItem(subject, date, (int) priority, true, id, getJsonArrayTagsString(tags));
                        taskItems.add(item);
                    } else if (!showCompleted && !completed) {
                        TaskItem item = new TaskItem(subject, date, (int) priority, false, id, getJsonArrayTagsString(tags));
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
                intent.putExtra("subject", taskItems.get(pos).getSubject());
                intent.putExtra("date", taskItems.get(pos).getDate());
                intent.putExtra("priority", taskItems.get(pos).getPriority());
                intent.putExtra("id", taskItems.get(pos).getId());
                intent.putExtra("tags", taskItems.get(pos).getTags().toString());
                startActivityForResult(intent, 989);
            }
        });

        return rootView;
    }

    private void getTags() {
        database.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    JSONObject jsonObject = new JSONObject();
                    String name = (String) snap.child("name").getValue();
                    String color = "";
                    String id = snap.getKey();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("color", color);
                        jsonObject.put("id", id);
                        jsonArrayTags.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TasksFragment", "Failed to read tags.", error.toException());
            }
        });
    }

    private JSONArray getJsonArrayTagsString(String tags) {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONArray jsonArray1 = new JSONArray(tags);
            for (int i = 0; i < jsonArray1.length(); ++i) {
                for (int d = 0; d < jsonArrayTags.length(); ++d) {
                    JSONObject tagItem = jsonArrayTags.getJSONObject(d);
                    String id = tagItem.getString("id");
                    if (id.equals(jsonArray1.getString(i))) {
                        jsonArray.put(tagItem);
                    }
                }
            }
        } catch (JSONException ignored) {
        } catch (NullPointerException ignored) {
        }
        return jsonArray;
    }
}
