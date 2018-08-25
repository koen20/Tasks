package nl.koenhabets.tasks;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static void newTask(String userId, DatabaseReference database, String subject, long ts, int priority, List<String> jsonArray) {
        DatabaseReference datas = database.child("users").child(userId).child("items").push();
        datas.child("subject").setValue(subject);
        datas.child("date").setValue(ts);
        datas.child("id").setValue(datas.getKey());
        datas.child("priority").setValue(priority);
        datas.child("tags").setValue(jsonArray);
    }

    public static List<TagItem> getTags(DatabaseReference database, String userId) {
        final List<TagItem> tagItemList = new ArrayList<>();
        database.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String name = (String) snap.child("name").getValue();
                    String color = "";
                    String id = snap.getKey();
                    TagItem tagItem = new TagItem(name, color, id);
                    tagItemList.add(tagItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TasksFragment", "Failed to read tags.", error.toException());
            }
        });
        return tagItemList;
    }

    public static List<TagItem> getListTagsString(List<String> tags, List<TagItem> tagItemList) {
        List<TagItem> tagItemListNew = new ArrayList<>();
        try {
            for (int i = 0; i < tags.size(); ++i) {
                for (int d = 0; d < tagItemList.size(); ++d) {
                    String id = tagItemList.get(d).getId();
                    if (id.equals(tags.get(i))) {
                        tagItemListNew.add(tagItemList.get(d));
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
        return tagItemListNew;
    }
}
