package manager;

import java.util.*;

import model.*;

public class TaskManager {
    private long i;
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();

    private long generateId() {
        return ++i;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(long id) {
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        if (task == null) return;
        task.setId(generateId());
        putInMap(task);
    }

    public void updateTask(Task task) {
        if (task == null) return;
        if (task.getClass() == Epic.class) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpic().getId() == task.getId()) {
                    Epic epic = (Epic) task;
                    epic.addSubtask(subtask);
                    subtask.setEpic(epic);
                }
            }
        }
        putInMap(task);
    }

    private void putInMap(Task task) {
        switch (task) {
            case Epic e -> {
                updateEpicStatus(e);
                epics.put(task.getId(), e);
            }
            case Subtask s -> {
                updateEpicStatus(s.getEpic());
                subtasks.put(task.getId(), s);
            }
            case Task t -> tasks.put(task.getId(), task);
        }
    }

    public void deleteTaskById(long id) {
        if (!tasks.containsKey(id)) return;
        tasks.remove(id);
    }

    public void deleteEpicById(long id) {
        if (!epics.containsKey(id)) return;
        Epic epic = epics.get(id);
        for (Subtask subtask : epic.getSubtaskSet()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(long id) {
        if (!subtasks.containsKey(id)) return;
        Subtask subtask = subtasks.get(id);
        subtasks.remove(id);
        Epic epic = subtask.getEpic();
        Set<Subtask> subtaskList = epic.getSubtaskSet();
        subtaskList.remove(subtask);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        Set<Subtask> subtaskList = epic.getSubtaskSet();
        if (subtaskList.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        Set<Status> uniqueStatuses = new HashSet<>();
        for (Subtask subtaskInList : subtaskList) {
            uniqueStatuses.add(subtaskInList.getStatus());
        }
        if (uniqueStatuses.size() == 1) {
            List<Status> statuses = new ArrayList<>(uniqueStatuses);
            Status status = statuses.getFirst();
            epic.setStatus(status);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
