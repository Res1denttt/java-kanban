package manager.taskManagement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import manager.Managers;
import manager.historyManagement.HistoryManager;
import model.*;

public class InMemoryTaskManager implements TaskManager {
    private long i;
    protected final Map<Long, Task> tasks = new HashMap<>();
    protected final Map<Long, Epic> epics = new HashMap<>();
    protected final Map<Long, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().get()));

    @Override
    public long generateId() {
        return ++i;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getEndTime().isPresent() && task.getClass() == Task.class);
        List<Task> tasksToRemove = historyManager.getHistory().stream()
                .filter(task -> task.getClass() == Task.class)
                .toList();
        tasksToRemove.forEach(task -> historyManager.remove(task.getId()));
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubtasks();
        prioritizedTasks.removeIf(task -> task.getEndTime().isPresent() && task.getClass() != Epic.class);
        List<Task> tasksToRemove = historyManager.getHistory().stream()
                .filter(task -> task.getClass() == Epic.class)
                .toList();
        tasksToRemove.forEach(task -> historyManager.remove(task.getId()));
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.setStatus(Status.NEW);
            epic.getSubtaskList().clear();
            updateEpic(epic);
        });
        prioritizedTasks.removeIf(task -> task.getEndTime().isPresent() && task.getClass() == Subtask.class);
        List<Task> tasksToRemove = historyManager.getHistory().stream()
                .filter(task -> task.getClass() == Subtask.class)
                .toList();
        tasksToRemove.forEach(task -> historyManager.remove(task.getId()));
    }

    @Override
    public Task getTaskById(long id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(long id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(long id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtaskList();
    }

    @Override
    public void addTask(Task task) {
        if (task == null) return;
        while (taskExistsInMap(task)) {
            task.setId(generateId());
        }
        if (crossOtherTaskInManager(task)) return;
        putInMap(task);
        addPrioritizedTasks(task);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !taskExistsInMap(task) || crossOtherTaskInManager(task)) return;
        if (task instanceof Epic) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpic().getId() == task.getId()) {
                    Epic epic = (Epic) task;
                    epic.addSubtask(subtask);
                    subtask.setEpic(epic);
                }
            }
        }
        putInMap(task);
        addPrioritizedTasks(task);
    }

    @Override
    public void deleteTaskById(long id) {
        if (!tasks.containsKey(id)) return;
        deleteFromPrioritizedTasks(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(long id) {
        if (!epics.containsKey(id)) return;
        Epic epic = epics.get(id);
        deleteFromPrioritizedTasks(epic);
        for (Subtask subtask : epic.getSubtaskList()) {
            deleteFromPrioritizedTasks(subtask);
            subtasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(long id) {
        if (!subtasks.containsKey(id)) return;
        Subtask subtask = subtasks.get(id);
        deleteFromPrioritizedTasks(subtask);
        subtasks.remove(id);
        Epic epic = subtask.getEpic();
        List<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(subtask);
        updateEpic(epic);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void updateEpic(Epic epic) {
        updateEpicSchedule(epic);
        updateEpicStatus(epic);
    }

    private void putInMap(Task task) {
        switch (task) {
            case Epic e -> {
                updateEpic(e);
                epics.put(task.getId(), e);
            }
            case Subtask s -> {
                updateEpic(s.getEpic());
                subtasks.put(task.getId(), s);
            }
            case Task t -> tasks.put(task.getId(), task);
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtaskList = epic.getSubtaskList();
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

    private void updateEpicSchedule(Epic epic) {
        List<Subtask> subtaskList = getEpicSubtasks(epic);
        Duration epicDuration = Duration.ZERO;
        for (Subtask subtask : subtaskList) {
            Optional<Duration> duration = subtask.getDuration();
            if (duration.isPresent()) {
                epicDuration = epicDuration.plus(duration.get());
            }
            Optional<LocalDateTime> startTime = subtask.getStartTime();
            Optional<LocalDateTime> epicStartTime = epic.getStartTime();
            if (startTime.isPresent() && (epicStartTime.isEmpty() || startTime.get().isBefore(epicStartTime.get()))) {
                epic.setStartTime(startTime.get());
            }
            Optional<LocalDateTime> endTime = subtask.getEndTime();
            Optional<LocalDateTime> epicEndTime = epic.getEndTime();
            if (endTime.isPresent() && (epicEndTime.isEmpty() || endTime.get().isAfter(epicEndTime.get()))) {
                epic.setEndTime(endTime.get());
            }
        }
        epic.setDuration(epicDuration);
    }

    private void addPrioritizedTasks(Task task) {
        if (task.getEndTime().isPresent()) {
            prioritizedTasks.add(task);
        }
    }

    private void deleteFromPrioritizedTasks(Task task) {
        if (task.getEndTime().isPresent()) {
            prioritizedTasks.remove(task);
        }
    }

    private boolean crossOtherTaskInManager(Task other) {
        if (other.getEndTime().isEmpty()) return false;
        Set<Task> tasks = getPrioritizedTasks();
        for (Task task : tasks) {
            if (task.crossAnotherTask(other)) return true;
        }
        return false;
    }


    private boolean taskExistsInMap(Task task) {
        long id = task.getId();
        return tasks.containsKey(id) || epics.containsKey(id) || subtasks.containsKey(id);
    }
}
