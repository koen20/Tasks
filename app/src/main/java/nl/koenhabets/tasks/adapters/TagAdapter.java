package nl.koenhabets.tasks.adapters;


import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class TagAdapter extends ArrayAdapter<String> {
    public TagAdapter(Context context, List<String> tags) {
        super(context, android.R.layout.simple_list_item_1, tags);
    }


}
