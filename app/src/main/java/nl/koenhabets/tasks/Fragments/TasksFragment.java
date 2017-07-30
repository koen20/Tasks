package nl.koenhabets.tasks.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

        adapter = new TasksAdapter(getContext(), taskItems);
        listView = (ListView) rootView.findViewById(R.id.tasksListview);
        listView.setAdapter(adapter);

        taskItems.clear();
        Gson parser = new Gson();

        try {
            JSONArray array = new JSONArray(readFromFile(getContext()));
            for (int i = 0;i < array.length() ;i++){
                JSONObject object = new JSONObject(array.get(i).toString());
                TaskItem item = new TaskItem(object.getString("subject"), "", 0, false);
                taskItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabAddTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(intent, 989);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //if( requestCode == 989 ) {
        String subject = null;
        try {
            subject = data.getStringExtra("subject");
            Log.i("subject", subject);
        } catch (NullPointerException ignored) {
        }
        if (subject != null) {
            TaskItem item = new TaskItem(subject, "", 0, false);
            taskItems.add(item);
            Gson parser = new Gson();
            Log.i("json", parser.toJson(taskItems) + "");
            writeToFile(parser.toJson(taskItems), getContext());
            adapter.notifyDataSetChanged();
        }
        //}
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("tasks", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("tasks");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
