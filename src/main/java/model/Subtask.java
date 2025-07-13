package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    long epicId;

    public Subtask(String name, String description, Status status, Epic epic, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epic.getId();
    }

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epicId = epic.getId();
    }

    public long getEpicId() {
        return epicId;
    }

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.SUBTASK;
    }

}
