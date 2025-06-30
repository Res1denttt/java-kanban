package model;

import manager.Managers;
import manager.taskManagement.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task("Abc", "some description", Status.NEW, Duration.ofMinutes(25),
                LocalDateTime.now());
        task1.setId(10);
        Task task2 = new Task("Qwe", "some description", Status.DONE, Duration.ofMinutes(25),
                LocalDateTime.of(2024, 8, 18, 16, 28));
        task2.setId(10);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void setIdShouldNotBeNegative() {
        Task task = new Task("Abc", "some description", Status.NEW, Duration.ofMinutes(25),
                LocalDateTime.now());
        manager.addTask(task);
        long oldId = task.getId();
        task.setId(-25);
        Assertions.assertEquals(oldId, task.getId());
    }
}