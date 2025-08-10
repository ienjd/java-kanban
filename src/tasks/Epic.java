package tasks;

import com.sun.jdi.request.DuplicateRequestException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Epic extends Task {

    private String title;
    private String description;
    private int id;
    private Status status;
    public Duration duration;
    LocalDateTime startTime;


    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
    }


    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Epic epic = (Epic) object;
        return id == epic.id && Objects.equals(title, epic.title) && Objects.equals(description, epic.description) && status == epic.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, id, status);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
