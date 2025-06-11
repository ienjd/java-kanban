package Tasks;

public class Epic extends Task {

    private String title;
    private String description;
    private int id;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
    }
}
