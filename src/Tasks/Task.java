package Tasks;

public class Task {
    private String title;
    private String description;
    private int id;
    private Statuses status;

    public Task(String title, String description, int id, Statuses status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }
}
