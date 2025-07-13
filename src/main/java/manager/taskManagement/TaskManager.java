package manager.taskManagement;

import model.Epic;
import model.Subtask;
import model.Task;


import java.util.List;
import java.util.Set;


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

    int addTask(Task task);

    int updateTask(Task task);

    void deleteTaskById(long id);

    void deleteEpicById(long id);

    void deleteSubtaskById(long id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}

