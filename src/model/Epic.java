package model;

import java.util.*;

public class Epic extends Task {
    private final Set<Subtask> subtaskSet; // заменил на хэшсет, чтобы не было задвоения при update subtask

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskSet = new HashSet<>();
    }

    public Set<Subtask> getSubtaskSet() {
        return subtaskSet;
    }

    public void addSubtask(Subtask subtask) {
        subtaskSet.add(subtask);
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
}
