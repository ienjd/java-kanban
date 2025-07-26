package ui;

import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainUI {

    void main() throws IOException {

        InMemoryTaskManager taskMaster = new InMemoryTaskManager();
        while (true) {
            System.out.println("Добро пожаловать!");
            printMenu();

            switch (MainUI.getUserInput()) {

                case "1" -> {
                    System.out.println("Введите тип задачи, epic, task или subtask: ");
                    switch (getUserInput()) {

                        case "task" -> {
                            Task newTask = taskMaster.createTask(getTitle(), getDescription());
                            taskMaster.addTaskToList(newTask, taskMaster.taskList);
                        }

                        case "epic" -> {
                            Epic newEpic = taskMaster.createEpic(getTitle(), getDescription());
                            taskMaster.addTaskToList(newEpic, taskMaster.epicList);
                            System.out.println("Введите количество подзадач данного эпика");
                            int quantitySubtasks = Integer.parseInt(getUserInput());
                            for (int i = 0; i < quantitySubtasks; i++) {
                                taskMaster.addTaskToList((taskMaster.createSubtask(getTitle(), getDescription(),
                                        newEpic.getId())), taskMaster.subtaskList);
                            }
                            taskMaster.updateEpic(newEpic);
                        }

                        case "subtask" -> {
                            System.out.println("Введите поочередно заголовок задачи, описание, и " +
                                    "номер эпика для данной подзадачи");
                            int epicId;
                            Subtask newSubtask = taskMaster.createSubtask(getTitle(), getDescription(),
                                    epicId = Integer.parseInt(getUserInput()));
                            taskMaster.addTaskToList(newSubtask, taskMaster.subtaskList);
                            taskMaster.updateEpic(taskMaster.epicList.get(epicId));
                        }

                        default ->
                                System.out.println("Нужно указать один из предложенных типов задач: task, epic или subtask");
                    }
                }

                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    boolean userAnswer = getUserInput().trim().toLowerCase().equals("да");
                    if (userAnswer) {
                        taskMaster.taskList.clear();
                        taskMaster.epicList.clear();
                        taskMaster.subtaskList.clear();
                        System.out.println("Список задач очищен");
                    } else {
                        System.out.println("Здорово, что вы передумали!");
                    }
                }

                case "3" -> {
                    System.out.println("Введите id искомого объекта");
                    int findObject = Integer.parseInt(getUserInput());
                    System.out.println(taskMaster.findTask(findObject));
                }

                case "4" -> printItems(taskMaster).forEach(task -> System.out.println(task));

                case "5" -> {
                    System.out.println("Введите id эпика объекта");
                    System.out.println(taskMaster.getEpicSubtasks(Integer.parseInt(getUserInput())));
                }

                case "6" -> taskMaster.deleteEpicSubtasks(Integer.parseInt(getUserInput()));

                case "7" -> {
                    System.out.println("Введите номер задачи/подзадачи");
                    int userCommand = Integer.parseInt(getUserInput());
                    if (taskMaster.taskList.containsKey(userCommand)) {
                        taskMaster.updateTask(taskMaster.taskList.get(userCommand));
                    } else if (taskMaster.subtaskList.containsKey(userCommand)) {
                        taskMaster.updateSubtask(taskMaster.subtaskList.get(userCommand));
                    }
                }

                case "8" -> {
                    System.out.println("Введите номер удаляемой задачи: ");
                    taskMaster.deleteTaskFromList(Integer.parseInt(getUserInput()));
                }

                case "9" -> taskMaster.getHistory().forEach(task -> System.out.println(task));

                case "10" -> {
                    return;
                }

                default -> System.out.println("Такой команды нет, введите команду из списка");
            }
        }
    }

    public static void printMenu() {
        String[] menuItems = new String[]{"1 - Создать задачу", "2 - Очистить список задач", "3 - Найти задачу",
                "4 - Вывести список задач", "5 - Вывести подзадачи по номеру эпика", "6 - Удалить подзадачи по номеру эпика",
                "7 - Обновить статус задачи",
                "8 - Удалить задачу", "9 - Вывести историю просмотров задач", "10 - Выход"};
        System.out.println("Выберите пункт меню:");
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }

    public static <T extends Task> ArrayList<T> printItems(InMemoryTaskManager taskMaster) {
        ArrayList<T> allItems = new ArrayList<>();
        taskMaster.taskList.values().forEach(task -> allItems.add((T) task));
        taskMaster.epicList.values().forEach(epic -> allItems.add((T) epic));
        taskMaster.subtaskList.values().forEach(subtask -> allItems.add((T) subtask));
        return allItems;
    }

    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        return command;
    }

    public static String getTitle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название задачи");
        String title = scanner.nextLine();
        return title;
    }

    public static String getDescription() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        return description;
    }
}

