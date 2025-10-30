package app.android.todomvc;

public class Todo {
    private String id;
    private String title;
    private boolean completed;

    public Todo(String id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
