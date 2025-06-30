package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    Epic epic;

    public Subtask(String name, String description, Status status, Epic epic, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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
