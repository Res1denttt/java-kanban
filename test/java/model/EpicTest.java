package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    Epic epic1 = new Epic("Abc", "some description", Status.NEW);
    Epic epic2 = new Epic("Qwe", "some description", Status.DONE);

    @Test
    void epicsWithEqualIdShouldBeEqual() {
        epic1.setId(10);
        epic2.setId(10);
        Assertions.assertEquals(epic1, epic2);
    }
}