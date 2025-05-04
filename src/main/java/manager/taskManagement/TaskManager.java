package manager.taskManagement;

import model.Epic;
import model.Subtask;
import model.Task;


import java.util.List;
import java.util.Set;


public interface TaskManager {
    public long generateId();

    public List<Task> getAllTasks();

    public List<Epic> getAllEpics();

    public List<Subtask> getAllSubtasks();

    public void deleteAllTasks();

    public void deleteAllEpics();

    public void deleteAllSubtasks();

    public Task getTaskById(long id);

    public Epic getEpicById(long id);

    public Subtask getSubtaskById(long id);

    public Set<Subtask> getEpicSubtasks(Epic epic);

    public void addTask(Task task);

    public void updateTask(Task task);

    public void deleteTaskById(long id);

    public void deleteEpicById(long id);

    public void deleteSubtaskById(long id);

    public List<Task> getHistory();

}

