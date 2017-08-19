package nl.koenhabets.tasks.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TaskItem;

public class TasksAdapter extends ArrayAdapter<TaskItem> {
    public TasksAdapter(Context context, List<TaskItem> taskItems) {
        super(context, 0, taskItems);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TaskItem taskItem = getItem(position);

        final String subject = taskItem.getSubject();
        boolean isCompleted = taskItem.isCompleted();
        long ts = taskItem.getDate();
        final String id = taskItem.getId();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView textViewSubject = convertView.findViewById(R.id.textViewSubject);
        final CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);

        if(ts != 0) {
            Date d = new Date(ts);
            Calendar cal = Calendar.getInstance();
            long da = cal.getTimeInMillis();
            cal.setTime(d);
            textViewDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
            if(da > ts) {
                textViewDate.setTextColor(Color.parseColor("#F44336"));
            }
        }

        textViewSubject.setText(subject);
        checkBox.setChecked(isCompleted);

        if (taskItem.getPriority() == 0){
            textViewSubject.setTextColor(Color.parseColor("#757575"));
        } else if (taskItem.getPriority() == 1){
            textViewSubject.setTextColor(Color.parseColor("#FB8C00"));
        } else if (taskItem.getPriority() == 2){
            textViewSubject.setTextColor(Color.parseColor("#F44336"));
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = checkBox.isChecked();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final String userId = currentFirebaseUser.getUid();
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                Query queryRef = database.child("users").child(userId).child("items").orderByChild("id").equalTo(id);

                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        String key = snapshot.getRef().getKey();
                        database.child("users").child(userId).child("items").child(key).child("completed").setValue(isChecked);
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        return convertView;
    }
}
