package tasks;

import java.util.Objects;

public class Subtask extends Task {
    private String title;
    private String description;
    private int id;
    private Status status;
    private int epicId;

    public Subtask(String title, String description, int id, Status status, int epicId) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.epicId = epicId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void setSubtaskStatus(Status status) {
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (epicId != id) {
            this.epicId = epicId;
        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Subtask subtask = (Subtask) object;
        return id == subtask.id && epicId == subtask.epicId && Objects.equals(title, subtask.title) && Objects.equals(description, subtask.description) && status == subtask.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, id, status, epicId);
    }

    @Override
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
