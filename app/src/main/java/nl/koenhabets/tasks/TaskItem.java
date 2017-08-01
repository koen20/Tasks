package nl.koenhabets.tasks;


public class TaskItem {
    private String subject;
    private long date;
    private int priority;
    private boolean completed;
    private String id;

    public TaskItem(String subject, long date, int priority, boolean completed, String id) {
        this.subject = subject;
        this.date = date;
        this.priority = priority;
        this.completed = completed;
        this.id = id;
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
}
