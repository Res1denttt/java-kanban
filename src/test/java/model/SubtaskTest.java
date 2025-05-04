package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void epicsWithEqualIdShouldBeEqual() {
        Epic epic = new Epic("Abc", "some description", Status.NEW);
        Subtask task1 = new Subtask("Abc", "some description", Status.NEW, epic);
        task1.setId(10);
        Subtask task2 = new Subtask("Qwe", "some description", Status.DONE, epic);
        task2.setId(10);
        Assertions.assertEquals(task1, task2);
    }
}