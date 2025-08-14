package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Epic extends Task {

    private String title;
    private String description;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;
    LocalDateTime endTime;


    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
    }

    @Override
    public void setDuration(int duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getEpicDuration() {
        return duration;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
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

    @Override
    public String toString() {
        return String.format("%s" + " " + "%s" + " " + "%s" + " " + "%s" + " " + "%s" + " " + "%s" + " " + "%s" + ";", getId(),
                getClass().getSimpleName(), getTitle(), status, getDescription(), getEpicDuration(), getStartTime());
    }}
