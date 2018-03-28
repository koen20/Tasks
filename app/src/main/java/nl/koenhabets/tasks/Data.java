package nl.koenhabets.tasks;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Data {

    public static void newTask(String userId,  DatabaseReference database, String subject, long ts, int priority, JSONArray jsonArray){
        DatabaseReference datas = database.child("users").child(userId).child("items").push();
        datas.child("subject").setValue(subject);
        datas.child("date").setValue(ts);
        datas.child("id").setValue(datas.getKey());
        datas.child("priority").setValue(priority);
        datas.child("tags").setValue(jsonArray.toString());
    }

    public static JSONArray getTags(DatabaseReference database, String userId) {
        final JSONArray jsonArray = new JSONArray();
        database.child("users").child(userId).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    JSONObject jsonObject = new JSONObject();
                    String name = (String) snap.child("name").getValue();
                    String color = "";
                    String id = snap.getKey();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("color", color);
                        jsonObject.put("id", id);
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TasksFragment", "Failed to read tags.", error.toException());
            }
        });
        return jsonArray;
    }

    public static JSONArray getJsonArrayTagsString(String tags, JSONArray jsonArrayTags) {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONArray jsonArray1 = new JSONArray(tags);
            for (int i = 0; i < jsonArray1.length(); ++i) {
                for (int d = 0; d < jsonArrayTags.length(); ++d) {
                    JSONObject tagItem = jsonArrayTags.getJSONObject(d);
                    String id = tagItem.getString("id");
                    if (id.equals(jsonArray1.getString(i))) {
                        jsonArray.put(tagItem);
                    }
                }
            }
        } catch (JSONException | NullPointerException ignored) {
        }
        return jsonArray;
    }
}
