package model;

import manager.Managers;
import manager.taskManagement.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task("Abc", "some description", Status.NEW);
        task1.setId(10);
        Task task2 = new Task("Qwe", "some description", Status.DONE);
        task2.setId(10);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void setIdShouldNotBeNegative() {
        Task task = new Task("Abc", "some description", Status.NEW);
        manager.addTask(task);
        long oldId = task.getId();
        task.setId(-25);
        Assertions.assertEquals(oldId, task.getId());
    }
}