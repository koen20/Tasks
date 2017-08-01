package nl.koenhabets.tasks;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
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
                //TaskItem item = new TaskItem(object.getString("subject"), object.getLong("date"), 0, object.getBoolean("completed"));
                //taskItems.add(item);
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

    public static void getData(final Context context){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.i("idToken", idToken);
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            final GetTasks request = new GetTasks(idToken, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("response", response);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("getData", error + "");
                                }
                            });
                            requestQueue.add(request);
                        } else {
                        }
                    }
                });
    }
}
