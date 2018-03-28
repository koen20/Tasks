package nl.koenhabets.tasks;

public class TagItem {
    private final String name;
    private final String color;

    public TagItem(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
