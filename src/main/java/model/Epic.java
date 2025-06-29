package model;

import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final List<Subtask> subtaskList;
    private LocalDateTime endTime;


    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskList = new ArrayList<>();
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.EPIC;
    }

}
