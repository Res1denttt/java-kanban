package practice;

import manager.Managers;
import model.*;
import manager.taskManagement.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("Сделать ужин", "Приготовить ужин на 2 персоны", Status.NEW));
        manager.addTask(new Task("Убраться в квартире", "Вымыть все полы", Status.IN_PROGRESS));
        Epic epic1 = new Epic("Построить дом", "Построить большой дом для всей семьи", Status.NEW);
        manager.addTask(epic1);
        Subtask subtask1 = new Subtask("Накопить деньги", "Нужно накопить деньги на участок и на дом",
                Status.IN_PROGRESS, epic1);
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Купить участок", "Найти хороший участок для дома", Status.DONE,
                epic1);
        manager.addTask(subtask2);
        Subtask subtask3 = new Subtask("Купить стройматериалы", "Купить все необходимые материалы",
                Status.NEW, epic1);
        manager.addTask(subtask3);
        Epic epic2 = new Epic("Найти работу", "Найти новую работу с большей зарплатой", Status.NEW);
        manager.addTask(epic2);
        manager.getSubtaskById(subtask1.getId());
        System.out.println(manager.getHistory());
        System.out.println();
        manager.getEpicById(epic2.getId());
        System.out.println(manager.getHistory());
        System.out.println();
        manager.getSubtaskById(subtask1.getId());
        System.out.println(manager.getHistory());
        System.out.println();
        manager.getSubtaskById(subtask3.getId());
        System.out.println(manager.getHistory());
        System.out.println();
        manager.getEpicById(epic2.getId());
        System.out.println(manager.getHistory());
        System.out.println();

        manager.deleteSubtaskById(subtask1.getId());
        System.out.println(manager.getHistory());
        System.out.println();
        manager.getEpicById(epic1.getId());
        System.out.println(manager.getHistory());
        System.out.println();
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getHistory());
        System.out.println();
    }
}
