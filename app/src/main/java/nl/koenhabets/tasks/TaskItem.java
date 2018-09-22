package nl.koenhabets.tasks;

import java.util.List;

public class TaskItem {
    private final String subject;
    private final long date;
    private final int priority;
    private final boolean completed;
    private final String id;
    private final List<TagItem> tags;
    private final List<ReminderItem> reminders;

    public TaskItem(String subject, long date, int priority, boolean completed, String id, List<TagItem> tags, List<ReminderItem> reminders) {
        this.subject = subject;
        this.date = date;
        this.priority = priority;
        this.completed = completed;
        this.id = id;
        this.tags = tags;
        this.reminders = reminders;
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

    public List<TagItem> getTags() {
        return tags;
    }

    public List<ReminderItem> getReminders() {
        return reminders;
    }
}
