package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task("Abc", "some description", Status.NEW);
        task1.setId(10);
        Task task2 = new Task("Qwe", "some description", Status.DONE);
        task2.setId(10);
        Assertions.assertEquals(task1, task2);
    }
}