package tasks;

public class Epic extends Task {

    private String title;
    private String description;
    private int id;
    private Status status;

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
