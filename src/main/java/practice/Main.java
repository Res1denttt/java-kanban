package practice;

import manager.Managers;
import model.*;
import manager.taskManagement.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("Сделать ужин", "Приготовить ужин на 2 персоны", Status.NEW));
        manager.addTask(new Task("Убраться в квартире", "Вымыть все полы", Status.IN_PROGRESS));
        Epic epic1 = new Epic("Найти работу", "Найти новую работу с большей зарплатой", Status.NEW);
        manager.addTask(epic1);
        Subtask subtask1 = new Subtask("Выложить резюме", "Подготовить резюме и разослать работадателям", Status.IN_PROGRESS, epic1);
        manager.addTask(subtask1);
        Epic epic2 = new Epic("Построить дом", "Построить большой дом для всей семьи", Status.NEW);
        manager.addTask(epic2);
        Subtask subtask2 = new Subtask("Купить участок", "Найти хороший участок для дома", Status.DONE, epic2);
        manager.addTask(subtask2);
        Subtask subtask3 = new Subtask("Купить стройматериалы", "Купить все необходимые материалы", Status.NEW, epic2);
        manager.addTask(subtask3);
        printAllTasks(manager);
        System.out.println();

        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getSubtaskById(7);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getSubtaskById(7);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getSubtaskById(7);
        manager.getEpicById(5);
        printAllTasks(manager);
        System.out.println();

        manager.getTaskById(1);
        printAllTasks(manager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
