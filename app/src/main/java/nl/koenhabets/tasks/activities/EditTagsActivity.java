package nl.koenhabets.tasks.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.koenhabets.tasks.R;
import nl.koenhabets.tasks.TagItem;
import nl.koenhabets.tasks.adapters.TagAdapter;

public class EditTagsActivity extends AppCompatActivity {
    String userId;
    DatabaseReference database;
    ListView listView;
    private final List<TagItem> tagItems = new ArrayList<>();
    TagAdapter adapter;
    JSONArray jsonArray = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tags);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentFirebaseUser.getUid();
        database = FirebaseDatabase.getInstance().getReference();

        listView = findViewById(R.id.listViewTags);

        adapter = new TagAdapter(this, tagItems);
        listView.setAdapter(adapter);

        database.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tagItems.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    JSONObject jsonObject = new JSONObject();
                    String name = (String) snap.child("name").getValue();
                    String color = "";
                    String id = snap.getKey();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("color", color);
                        jsonObject.put("id", id);
                        TagItem tagItem = new TagItem(name, color, id);
                        tagItems.add(tagItem);
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("EditTagsActivity", "Failed to read tags.", error.toException());
            }
        });
    }
}
