package nl.koenhabets.tasks;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.koenhabets.tasks.adapters.TagAdapter;

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
    private String subject;
    private long date;
    private int priority;
    private String id;
    private RadioButton radioLow;
    private RadioButton radioMedium;
    private RadioButton radioHigh;
    private DatabaseReference database;
    private List<String> tags = new ArrayList<>();
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");
        date = intent.getLongExtra("date", 0);
        priority = intent.getIntExtra("priority", 1);
        id = intent.getStringExtra("id");


        editTextSubject = findViewById(R.id.editText);
        editTextdate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        actv = findViewById(R.id.autoCompleteTextView);

        radioGroup = findViewById(R.id.radioGroup);
        radioLow = findViewById(R.id.radioLow);
        radioMedium = findViewById(R.id.radioMedium);
        radioHigh = findViewById(R.id.radioHigh);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String name = (String) snap.child("name").getValue();
                    tags.add(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("AddTaskActivity", "Failed to read value.", error.toException());
            }
        });

        TagAdapter adapter = new TagAdapter(this, tags);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
        actv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        if (priority == 0) {
            radioLow.setChecked(true);
        } else if (priority == 1) {
            radioMedium.setChecked(true);
        } else if (priority == 2) {
            radioHigh.setChecked(true);
        }

        if (date != 0) {
            Date d = new Date(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            editTextdate.setText(cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.YEAR));
            editTextTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        }

        editTextSubject.setText(subject);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int ye, int mo,
                                  int da) {
                year = ye;
                month = mo + 1;
                day = da;
                editTextdate.setText(day + "-" + month + " " + year);
                ts();
            }

        };

        editTextdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(AddTaskActivity.this, date, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
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
                new TimePickerDialog(AddTaskActivity.this, time, cal
                        .get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                        true).show();
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
        int idd = item.getItemId();
        int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
        if (idd == R.id.action_add && id == null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("subject", editTextSubject.getText().toString());
            returnIntent.putExtra("date", ts);
            returnIntent.putExtra("priority", index);
            setResult(Activity.RESULT_OK, returnIntent);
            String tex = actv.getText() + "";
            String[] tagsSplit = tex.split(",");
            for (int i = 0; i < tagsSplit.length; ++i){
                boolean ads = false;
                for(String te : tags){
                    if(te.contains(tagsSplit[i])){
                        ads = true;
                    }
                }
                if(!ads){
                    DatabaseReference datas = database.child("users").child(userId).child("tags").push();
                    datas.child("name").setValue(tagsSplit[i].trim());
                }
            }
            returnIntent.putExtra("tags", tex);
            finish();
            return true;
        } else if (id != null) {
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentFirebaseUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("items").child(id);
            database.child("subject").setValue(editTextSubject.getText().toString());
            database.child("priority").setValue(index);
            database.child("date").setValue(ts);
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
