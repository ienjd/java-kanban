package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    public String title;
    public String description;
    public int id;
    Status status;
    public Duration duration;
    LocalDateTime startTime;

    public Task() {
    }
    public Task(
            String title,
            String description,
            int id,
            Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }
    public Task(
            String title,
            String description,
            int id,
            Status status,
            Duration duration,
            LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
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

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
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
        return String.format("%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ", " + "%s" + ";", id,
                getClass().getSimpleName(), title, status, description, startTime, duration, startTime, duration);
    }
}

