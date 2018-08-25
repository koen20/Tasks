package nl.koenhabets.tasks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.ReminderItem;


public class ReminderAdapter extends ArrayAdapter<ReminderItem> {
    public ReminderAdapter(Context context, List<ReminderItem> reminderItems) {
        super(context, 0, reminderItems);
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        ReminderItem reminderItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_item, parent, false);
        }

        //TextView textViewTag = convertView.findViewById(R.id.textViewTag);

        //textViewTag.setText(reminderItem.getName());
        return convertView;
    }
}
