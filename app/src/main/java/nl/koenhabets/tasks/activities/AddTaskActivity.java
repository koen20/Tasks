package nl.koenhabets.tasks.activities;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import nl.koenhabets.tasks.Data;
import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.ReminderItem;
import nl.koenhabets.tasks.TagItem;
import nl.koenhabets.tasks.TaskItem;
import nl.koenhabets.tasks.adapters.ReminderAdapter;

public class AddTaskActivity extends AppCompatActivity {
    private EditText editTextSubject;
    private EditText editTextdate;
    private EditText editTextTime;
    private RadioGroup radioGroup;
    private MultiAutoCompleteTextView actv;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private long ts;
    private String tag;
    private RadioButton radioLow;
    private RadioButton radioMedium;
    private RadioButton radioHigh;
    private ListView listView;
    private DatabaseReference database;
    private List<String> tags = new ArrayList<>();
    String userId;
    JSONArray jsonArrayTags = new JSONArray();
    String existingTags = "";
    ReminderAdapter adapter;
    private final List<ReminderItem> reminderItems = new ArrayList<>();
    TaskItem taskItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskItem = new TaskItem("", 0, 1, false, null, new ArrayList<TagItem>(), new ArrayList<ReminderItem>());

        Intent intent = getIntent();
        String task = intent.getStringExtra("task");
        Gson gson = new Gson();
        if(task != null) {
            taskItem = gson.fromJson(task, TaskItem.class);
        }

        editTextSubject = findViewById(R.id.editText);
        editTextdate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        actv = findViewById(R.id.autoCompleteTextView);

        radioGroup = findViewById(R.id.radioGroup);
        radioLow = findViewById(R.id.radioLow);
        radioMedium = findViewById(R.id.radioMedium);
        radioHigh = findViewById(R.id.radioHigh);

        listView = findViewById(R.id.listViewReminders);
        adapter = new ReminderAdapter(this, reminderItems);
        listView.setAdapter(adapter);
        ReminderItem item22 = new ReminderItem("time", 0);
        reminderItems.add(item22);

        if (taskItem.getDate() != 0) {
            Date date2 = new Date(taskItem.getDate());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date2);
            editTextdate.setText(cal1.get(Calendar.DAY_OF_MONTH) + "-" + cal1.get(Calendar.DAY_OF_MONTH) + " " + cal1.get(Calendar.YEAR));
            editTextTime.setText(cal1.get(Calendar.HOUR_OF_DAY) + ":" + cal1.get(Calendar.MINUTE));
        }

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String name = (String) snap.child("name").getValue();
                    tags.add(name);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", snap.getKey());
                        jsonObject.put("name", name);
                        jsonArrayTags.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < jsonArrayTags.length(); ++i) {
                    try {
                        JSONArray jsonArray = new JSONArray(tag);
                        JSONObject jsonObject = jsonArrayTags.getJSONObject(i);
                        for (int d = 0; d < jsonArray.length(); ++d) {
                            Log.i("1", jsonObject.getString("id"));
                            Log.i("2", jsonArray.getString(d));
                            if (jsonObject.getString("id").equals(jsonArray.getJSONObject(d).getString("id"))) {
                                existingTags = existingTags + jsonObject.getString("name") + ", ";
                                Log.i("existingtags", existingTags);
                                actv.setText(existingTags);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException ignored) {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AddTaskActivity", "Failed to read value.", error.toException());
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.select_dialog_item, tags);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
        actv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        if (taskItem.getPriority() == 0) {
            radioLow.setChecked(true);
        } else if (taskItem.getPriority() == 1) {
            radioMedium.setChecked(true);
        } else if (taskItem.getPriority() == 2) {
            radioHigh.setChecked(true);
        }

        Calendar cal54 = Calendar.getInstance();
        if (taskItem.getDate() != 0) {
            Date d = new Date(taskItem.getDate());
            cal54.setTime(d);
            editTextdate.setText(cal54.get(Calendar.DAY_OF_MONTH) + "-" + cal54.get(Calendar.MONTH) + " " + cal54.get(Calendar.YEAR));
            editTextTime.setText(cal54.get(Calendar.HOUR_OF_DAY) + ":" + cal54.get(Calendar.MINUTE));
        }

        year = cal54.get(Calendar.YEAR);
        month = cal54.get(Calendar.MONTH);
        day = cal54.get(Calendar.DAY_OF_MONTH);
        hour = cal54.get(Calendar.HOUR_OF_DAY);
        minute = cal54.get(Calendar.MINUTE);

        editTextSubject.setText(taskItem.getSubject());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int ye, int mo,
                                  int da) {
                year = ye;
                month = mo;
                day = da;
                editTextdate.setText(day + "-" + month + " " + year);
                ts();
            }

        };

        editTextdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTaskActivity.this, date, year, month, day).show();
            }

        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int h, int m) {
                hour = h;
                minute = m;
                editTextTime.setText(hour + ":" + minute);
                ts();
            }

        };

        editTextTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new TimePickerDialog(AddTaskActivity.this, time, hour, minute, true).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("maand", "" + month);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = currentFirebaseUser.getUid();
        final int idd = item.getItemId();
        final int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));

        final String subject = editTextSubject.getText().toString();

        String tex = actv.getText().toString();
        final String[] tagsSplit = tex.split(",");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < tagsSplit.length; ++i) {
            boolean existing = false;
            for (int d = 0; d < jsonArrayTags.length(); ++d) {
                try {
                    JSONObject jsonObject = jsonArrayTags.getJSONObject(d);
                    if (Objects.equals(jsonObject.getString("name"), tagsSplit[i].trim())) {
                        existing = true;
                        stringList.add(jsonObject.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (!existing && !Objects.equals(tagsSplit[i].trim(), "")) {
                DatabaseReference datas = database.child("users").child(userId).child("tags").push();
                datas.child("name").setValue(tagsSplit[i].trim());
                stringList.add(datas.getKey());
            }
        }

        if (idd == R.id.action_add && taskItem.getId() == null) {
            Log.i("taaaa", stringList.toString());
            Log.i("tsadd", ts + "");
            Data.newTask(userId, FirebaseDatabase.getInstance().getReference(), subject, ts, index, stringList);
            finish();
        } else if (taskItem.getId() != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("items").child(taskItem.getId());
            database.child("subject").setValue(editTextSubject.getText().toString());
            database.child("priority").setValue(index);
            database.child("date").setValue(ts);
            database.child("tags").setValue(stringList);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ts() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        ts = cal.getTimeInMillis();
    }
}
