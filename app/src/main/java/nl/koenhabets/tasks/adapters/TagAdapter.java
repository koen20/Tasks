package nl.koenhabets.tasks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TagItem;

public class TagAdapter extends ArrayAdapter<TagItem> {
    public TagAdapter(Context context, List<TagItem> tagItems) {
        super(context, 0, tagItems);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TagItem tagItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_item, parent, false);
        }

        TextView textViewTag = convertView.findViewById(R.id.textViewTag);

        textViewTag.setText(tagItem.getName());
        return convertView;
    }
}
