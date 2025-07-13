package manager.taskManagement;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path file;

    private FileBackedTaskManager(Path file) {
        this.file = file;
    }

    public void save() throws ManagerSaveException {
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
    public void deleteAllTasks() throws ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws ManagerSaveException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public int addTask(Task task) throws ManagerSaveException {
        int result = super.addTask(task);
        save();
        return result;
    }

    @Override
    public int updateTask(Task task) throws ManagerSaveException {
        int result = super.updateTask(task);
        save();
        return result;
    }

    @Override
    public void deleteTaskById(long id) throws NotFoundException, ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(long id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(long id) throws ManagerSaveException {
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
            sb.append(((Subtask) task).getEpicId());
        }
        return sb.toString();
    }

    public Task fromString(String value) {
        String[] values = value.split(","); //id,type,name,status,description, duration, startTime, epic
        long id = Long.parseLong(values[0]);
        TaskTypes type = TaskTypes.parseType(values[1]);
        Status status = Status.parseStatus(values[3]);
        Duration duration = Duration.ofMinutes(Long.parseLong(values[5]));
        LocalDateTime startTime;
        if (!values[6].equals("0")) {
            startTime = LocalDateTime.parse(values[6]);
        } else {
            startTime = null;
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
