package model;

import java.util.*;

public class Epic extends Task {
    private final List<Subtask> subtaskList;

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
