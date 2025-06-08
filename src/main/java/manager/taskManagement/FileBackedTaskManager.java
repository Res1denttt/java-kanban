package manager.taskManagement;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.newBufferedReader;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path file;

    public FileBackedTaskManager(Path file) {
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
            throw new ManagerSaveException();
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
        String result = task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() +
                "," + task.getDescription() + ",";
        if (task instanceof Subtask) {
            result += ((Subtask) task).getEpic().getId();
        }
        return result;
    }

    public Task fromString(String value) {
        String[] values = value.split(","); //id,type,name,status,description,epic
        Status status;
        TaskTypes type;
        try {
            type = TaskTypes.parseType(values[1]);
            status = Status.parseStatus(values[3]);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return null;
        }
        long id = Long.parseLong(values[0]);
        String name = values[2];
        String description = values[4];
        switch (type) {
            case EPIC -> {
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            }
            case SUBTASK -> {
                long epicId = Long.parseLong(values[5]);
                Epic epic = epics.get(epicId);
                Subtask subtask = new Subtask(name, description, status, epic);
                subtask.setId(id);
                return subtask;
            }
            default -> {
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            }
        }
    }
}
