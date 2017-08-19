package nl.koenhabets.tasks;


public class TaskItem {
    private final String subject;
    private final long date;
    private final int priority;
    private final boolean completed;
    private final String id;

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
