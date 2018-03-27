package nl.koenhabets.tasks;


import org.json.JSONArray;

public class TaskItem {
    private final String subject;
    private final long date;
    private final int priority;
    private final boolean completed;
    private final String id;
    private final JSONArray tags;

    public TaskItem(String subject, long date, int priority, boolean completed, String id, JSONArray tags) {
        this.subject = subject;
        this.date = date;
        this.priority = priority;
        this.completed = completed;
        this.id = id;
        this.tags = tags;
    }

    public String getSubject() {
        return subject;
    }

    public long getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getId() {
        return id;
    }

    public JSONArray getTags() {
        return tags;
    }
}
