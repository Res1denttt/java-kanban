package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    @Test
    void epicsWithEqualIdShouldBeEqual() {
        Epic epic = new Epic("Abc", "some description", Status.NEW);
        Subtask task1 = new Subtask("Abc", "some description", Status.NEW, epic, Duration.ofMinutes(25),
                LocalDateTime.of(2023, 12, 1, 18, 20));
        task1.setId(10);
        Subtask task2 = new Subtask("Qwe", "some description", Status.DONE, epic, Duration.ofMinutes(25),
                LocalDateTime.now());
        task2.setId(10);
        Assertions.assertEquals(task1, task2);
    }
}