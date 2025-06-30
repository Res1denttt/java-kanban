package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    protected long id;
    protected String name;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id < 0) return;
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<LocalDateTime> getEndTime() {
        Optional<Duration> duration = getDuration();
        Optional<LocalDateTime> startTime = getStartTime();
        if (startTime.isPresent() && duration.isPresent()) {
            return Optional.of(startTime.get().plus(duration.get()));
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public TaskTypes getTaskType() {
        return TaskTypes.TASK;
    }

    public boolean crossAnotherTask(Task other) {
        if (getEndTime().isEmpty() || other.getEndTime().isEmpty()) return false;
        LocalDateTime taskEndTime = getEndTime().get();
        LocalDateTime otherEndTime = other.getEndTime().get();
        if (taskEndTime.isBefore(other.startTime) || (startTime.isAfter(otherEndTime))) {
            return false;
        }
        return true;
    }
}
