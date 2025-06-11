package manager.taskManagement;

import model.Epic;
import model.Subtask;
import model.Task;


import java.util.List;


public interface TaskManager {
    long generateId();

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(long id);

    Epic getEpicById(long id);

    Subtask getSubtaskById(long id);

    List<Subtask> getEpicSubtasks(Epic epic);

    void addTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(long id);

    void deleteEpicById(long id);

    void deleteSubtaskById(long id);

    List<Task> getHistory();
}

