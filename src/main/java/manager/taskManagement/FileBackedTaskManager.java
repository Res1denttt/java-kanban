package manager.taskManagement;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path file;

    private FileBackedTaskManager(Path file) {
        this.file = file;
    }

    public void save() {
        List<Task> taskList = new ArrayList<>(tasks.values());
        taskList.addAll(epics.values());
        taskList.addAll(subtasks.values());

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (Task task : taskList) {
                writer.write(toString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                manager.addTask(manager.fromString(line));
            }
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
        return manager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(long id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(long id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(long id) {
        super.deleteSubtaskById(id);
        save();
    }

    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        final String DELIMITER = ",";
        sb.append(task.getId()).append(DELIMITER)
                .append(task.getTaskType()).append(DELIMITER)
                .append(task.getName()).append(DELIMITER)
                .append(task.getStatus()).append(DELIMITER)
                .append(task.getDescription()).append(DELIMITER)
                .append(task.getDuration().orElse(Duration.ZERO).toMinutes()).append(DELIMITER)
                .append(Objects.toString(task.getStartTime().orElse(null), "0")).append(DELIMITER);
        if (task instanceof Subtask) {
            sb.append(((Subtask) task).getEpic().getId());
        }
        return sb.toString();
    }

    public Task fromString(String value) {
        String[] values = value.split(","); //id,type,name,status,description, duration, startTime, epic
        Status status;
        TaskTypes type;
        Duration duration;
        LocalDateTime startTime;
        long id;
        try {
            id = Long.parseLong(values[0]);
            type = TaskTypes.parseType(values[1]);
            status = Status.parseStatus(values[3]);
            duration = Duration.ofMinutes(Long.parseLong(values[5]));
            if (!values[6].equals("0")) {
                startTime = LocalDateTime.parse(values[6]);
            } else {
                startTime = null;
            }
        } catch (IllegalStateException | NumberFormatException | DateTimeParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
        String name = values[2];
        String description = values[4];
        switch (type) {
            case EPIC -> {
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            }
            case SUBTASK -> {
                long epicId;
                try {
                    epicId = Long.parseLong(values[7]);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
                Epic epic = epics.get(epicId);
                Subtask subtask = new Subtask(name, description, status, epic, duration, startTime);
                subtask.setId(id);
                return subtask;
            }
            default -> {
                Task task = new Task(name, description, status, duration, startTime);
                task.setId(id);
                return task;
            }
        }
    }
}
