package nl.koenhabets.tasks.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TaskItem;

public class TasksAdapter extends ArrayAdapter<TaskItem> {
    public TasksAdapter(Context context, List<TaskItem> taskItems) {
        super(context, 0, taskItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TaskItem taskItem = getItem(position);

        String subject = taskItem.getSubject();
        boolean isCompleted = taskItem.isCompleted();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        TextView textViewContent = (TextView) convertView.findViewById(R.id.textViewSubject);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

        textViewContent.setText(subject);
        checkBox.setChecked(isCompleted);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = checkBox.isChecked();

            }
        });

        return convertView;
    }
}