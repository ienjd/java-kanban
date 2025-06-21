package Tasks;

import java.util.List;
import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private int id;
    private Status status;


    public Task() {
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public Task (String title, String description, int id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Task{ " +
                "title= " + title + '\'' +
                ", description= " + description + '\'' +
                ", id= " + id +
                ", status= " + status + ", class= " +
                getClass() + " " +
                " }";
    }
}

