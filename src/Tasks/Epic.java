package Tasks;

public class Epic extends Task {

    private String title;
    private String description;
    private int id;
    private Status status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
    }
}
