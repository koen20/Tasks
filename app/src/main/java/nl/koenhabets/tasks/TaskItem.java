package nl.koenhabets.tasks;


public class TaskItem {
    private String subject;
    private String date;
    private int priority;
    private boolean completed;

    public TaskItem(String subject, String date, int priority, boolean completed) {
        this.subject = subject;
        this.date = date;
        this.priority = priority;
        this.completed = completed;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }
}
