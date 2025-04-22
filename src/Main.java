import manager.TaskManager;
import model.*;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.addTask(new Task("Сделать ужин", "Приготовить ужин на 2 персоны", Status.NEW));
        manager.addTask(new Task("Убраться в квартире", "Вымыть все полы", Status.IN_PROGRESS));
        Epic epic1 = new Epic("Найти работу", "Найти новую работу с большей зарплатой", Status.NEW);
        manager.addTask(epic1);
        Subtask subtask1 = new Subtask("Выложить резюме", "Подготовить резюме и разослать работадателям", Status.IN_PROGRESS, epic1);
        manager.addTask(subtask1);
        Epic epic2 = new Epic("Построить дом", "Построить большой дом для всей семьи", Status.NEW);
        manager.addTask(epic2);
        Subtask subtask2 = new Subtask("Купить участок", "Найти хороший участок для дома", Status.DONE, epic2);
        Subtask subtask3 = new Subtask("Купить стройматериалы", "Купить все необходимые материалы", Status.NEW, epic2);
        manager.addTask(subtask2);
        manager.addTask(subtask3);
        System.out.println(manager.getAllTasks());
        System.out.println();
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println();
        Subtask subtask4 = new Subtask("Купить стройматериалы", "Купить все необходимые материалы", Status.DONE, epic2);
        subtask4.setId(subtask3.getId());
        manager.updateTask(subtask4);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println();
        Epic epic3 = new Epic("Построить дом", "Построить 2 дома на участке", Status.NEW);
        epic3.setId(epic2.getId());
        manager.updateTask(epic3);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println(epic3.getSubtaskSet());
        System.out.println();
        manager.deleteSubtaskById(7);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
    }
}
