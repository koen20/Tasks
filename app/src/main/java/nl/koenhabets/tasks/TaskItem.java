package nl.koenhabets.tasks;


public class TaskItem {
    private final String subject;
    private final long date;
    private final int priority;
    private final boolean completed;
    private final String id;
    private final String tags;

    public TaskItem(String subject, long date, int priority, boolean completed, String id, String tags) {
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

    public String getTags() {
        return tags;
    }
}
