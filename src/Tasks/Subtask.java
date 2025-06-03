package Tasks;

public class Subtask extends Task{
    private int epicId;

    public Subtask(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }
}
