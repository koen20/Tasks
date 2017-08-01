package nl.koenhabets.tasks.Fragments;

import android.content.Context;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nl.koenhabets.tasks.AddTaskActivity;
import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TaskItem;
import nl.koenhabets.tasks.adapters.TasksAdapter;

public class TasksFragment extends Fragment {
    ListView listView;
    private List<TaskItem> taskItems = new ArrayList<>();
    private TasksAdapter adapter;
    int positionL;
    DatabaseReference database;
    String userId;

    public TasksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        //taskItems = Api.getTaskItems(getContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        adapter = new TasksAdapter(getContext(), taskItems);
        listView = (ListView) rootView.findViewById(R.id.tasksListview);
        listView.setAdapter(adapter);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();

        database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userId).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskItems.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String subject = (String) snap.child("subject").getValue();
                    String id = (String) snap.child("id").getValue();
                    boolean completed = false;
                    long priority;
                    long date;
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
                    TaskItem item = new TaskItem(subject, date, (int) priority, completed, id);
                    taskItems.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TaskFragment", "Failed to read value.", error.toException());
            }
        });

        adapter.notifyDataSetChanged();


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabAddTask);
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

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String subject = null;
        long ts = 0;
        int priority = 1;
        try {
            subject = data.getStringExtra("subject");
            ts = data.getLongExtra("date", 0);
            priority = data.getIntExtra("priority", 1);
        } catch (NullPointerException ignored) {
        }
        if (subject != null) {
            DatabaseReference datas = database.child("users").child(userId).child("items").push();
            datas.child("subject").setValue(subject);
            datas.child("date").setValue(ts);
            datas.child("id").setValue(datas.getKey());
            datas.child("priority").setValue(priority);
            TaskItem item = new TaskItem(subject, ts, priority, false, datas.getKey());
            taskItems.add(item);
            adapter.notifyDataSetChanged();
        }
    }
}
