package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private String title;
    private String description;
    private int id;
    private int epicId;
    private LocalDateTime startTime;

    public Subtask(
            String title,
            String description,
            int id,
            Status status,
            int epicId) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.epicId = epicId;
    }
    @Override
    public void setDuration(int duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public int getId() {
        return id;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
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

    public Duration getDuration(){
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
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
        return String.format("%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ";", id,
                getClass().getSimpleName(), title, status, description, epicId, duration, startTime);
    }
}
