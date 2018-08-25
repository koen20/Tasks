package nl.koenhabets.tasks;

public class TagItem {
    private final String name;
    private final String color;
    private final String id;

    public TagItem(String name, String color, String id) {
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
