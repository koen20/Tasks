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

import java.util.ArrayList;
import java.util.List;

import nl.koenhabets.tasks.AddTaskActivity;
import nl.koenhabets.tasks.Api;
import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TaskItem;
import nl.koenhabets.tasks.adapters.TasksAdapter;

public class TasksFragment extends Fragment implements RemoveTaskDialogFragment.NoticeDialogListener {
    ListView listView;
    private List<TaskItem> taskItems = new ArrayList<>();
    private TasksAdapter adapter;
    int positionL;

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
        taskItems = Api.getTaskItems(getContext());

        adapter = new TasksAdapter(getContext(), taskItems);
        listView = (ListView) rootView.findViewById(R.id.tasksListview);
        listView.setAdapter(adapter);


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
                //DialogFragment newFragment = new RemoveTaskDialogFragment();
                //newFragment.show(getFragmentManager(), "Task");
                taskItems.remove(positionL);
                Api.update(taskItems, getContext());
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String subject = null;
        long ts = 0;
        try {
            subject = data.getStringExtra("subject");
            ts = data.getLongExtra("date", 0);
            Log.i("subject", subject);
        } catch (NullPointerException ignored) {
        }
        if (subject != null) {
            TaskItem item = new TaskItem(subject, ts, 0, false);
            taskItems.add(item);
            Api.update(taskItems, getContext());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogRemoveClick(DialogFragment dialog) {
        taskItems.remove(positionL);
        Api.update(taskItems, getContext());
        adapter.notifyDataSetChanged();
    }
}
