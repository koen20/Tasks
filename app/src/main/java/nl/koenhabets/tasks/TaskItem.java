package nl.koenhabets.tasks;


public class TaskItem {
    private String subject;
    private long date;
    private int priority;
    private boolean completed;

    public TaskItem(String subject, long date, int priority, boolean completed) {
        this.subject = subject;
        this.date = date;
        this.priority = priority;
        this.completed = completed;
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
}
