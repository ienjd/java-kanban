package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
        this.duration = Duration.ofMinutes(0);
        this.startTime = LocalDateTime.of(2021, 1, 1, 1, 1);
    }

    public void clearSubtasksList() {
        subtasks.clear();
    }

    public void deleteSubtasks(int id) {
        subtasks.remove(id);
    }


    public void setSubtasks(Subtask subtask) {
        if (subtask == null) {
            throw new IllegalArgumentException("subtask is null");
        }
        subtasks.put(subtask.getId(), subtask);
    }


    public List<Subtask> getSubtasks() {
        return ((List<Subtask>) subtasks.values());
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

}
