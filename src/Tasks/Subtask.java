package Tasks;

public class Subtask extends Task{
    private String title;
    private String description;
    private int id;
    private Status status;
    private int epicId;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Status getStatus() {
        return status;
    }


    public void setSubtaskStatus(Status status) {
        this.status = status;
    }

    public int getEpicId(){
        return epicId;
    }

    public void setEpicId(int epicId){
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int id, Status status, int epicId) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.epicId = epicId;
    }


    public String toString() {
        return "Task{ " +
                "title= " + title + '\'' +
                ", description= " + description + '\'' +
                ", id= " + id +
                ", status= " + status + ", class= " +
                getClass() + " " + ", epicId= " + getEpicId() + " " +
                " }";
    }
}
