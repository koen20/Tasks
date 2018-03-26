package nl.koenhabets.tasks;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;

public class Data {

    public static void newTask(String userId,  DatabaseReference database, String subject, long ts, int priority, JSONArray jsonArray){
        DatabaseReference datas = database.child("users").child(userId).child("items").push();
        datas.child("subject").setValue(subject);
        datas.child("date").setValue(ts);
        datas.child("id").setValue(datas.getKey());
        datas.child("priority").setValue(priority);
        datas.child("tags").setValue(jsonArray.toString());
    }
}
