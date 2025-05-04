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

    /*
    В ТЗ сказано, что нужно проверить, что объект Epic нельзя добавить в самого себя в виде подзадачи, но у меня
    в таком случае код даже не скопмпилируется. Вероятно, этот тест актуален только для тех, кто делал связи через id,
    а не через сами объекты.
     */

}