package nl.koenhabets.tasks;

public class ReminderItem {
    private final String type;
    private final long date;

    public ReminderItem(String type, long date) {
        this.type = type;
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
