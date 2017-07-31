package nl.koenhabets.tasks;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

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
import java.util.Calendar;
import java.util.List;

public class Api {

    public static void update(List<TaskItem> taskItems, Context context) {
        Gson parser = new Gson();
        JSONArray array = null;
        try {
            Calendar cal = Calendar.getInstance();
            array = new JSONArray(parser.toJson(taskItems));
            array.put(cal.getTimeInMillis() / 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeToFile(array + "", context);
    }

    public static List<TaskItem> getTaskItems(Context context) {
        List<TaskItem> taskItems = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(readFromFile(context));
            long ts = array.getLong(array.length() - 1);
            Log.i("tsRead", ts + "");
            array.remove(array.length() - 1);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = new JSONObject(array.get(i).toString());
                TaskItem item = new TaskItem(object.getString("subject"), object.getLong("date"), 0, false);
                taskItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taskItems;
    }

    private static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("tasks", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("tasks");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("TasksFragment", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("TasksFragment", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
