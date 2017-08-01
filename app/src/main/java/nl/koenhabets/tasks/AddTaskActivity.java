package nl.koenhabets.tasks;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    EditText subject;
    EditText editTextdate;
    EditText editTextTime;
    RadioGroup radioGroup;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private long ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        subject = (EditText) findViewById(R.id.editText);
        editTextdate = (EditText) findViewById(R.id.editTextDate);
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

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
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
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
        int id = item.getItemId();

        if (id == R.id.action_add) {
            int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
            Log.i("index", index + "");
            Intent returnIntent = new Intent();
            returnIntent.putExtra("subject", subject.getText().toString());
            returnIntent.putExtra("date", ts);
            returnIntent.putExtra("priority", index);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ts(){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        ts = cal.getTimeInMillis() / 1000;
    }
}
