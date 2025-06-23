package Main;
import HistoryManager.InMemoryHistoryManager;
import InMemoryTaskManager.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Task;
import UserInputs.UserInput;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        UserInput userInput = new UserInput();
        InMemoryTaskManager taskMaster = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        System.out.println("Тесты практикума");

        taskMaster.addTaskToList((taskMaster.createTask("Задача 1", "Описание задачи 1")),
                taskMaster.taskList);
        taskMaster.addTaskToList((taskMaster.createTask("Задача 2", "Описание задачи 2")),
                taskMaster.taskList);
        taskMaster.addTaskToList((taskMaster.createEpic("Эпик 1", "Описание эпика 1")),
                taskMaster.epicList);
        taskMaster.addTaskToList((taskMaster.createSubtask("Подзадача 1", "Описание подзадачи 1", 3)),
                taskMaster.subtaskList);
        taskMaster.addTaskToList((taskMaster.createSubtask("Подзадача 2", "Описание подзадачи 2", 3)),
                taskMaster.subtaskList);
        taskMaster.addTaskToList((taskMaster.createEpic("Эпик 2", "Описание эпика 2")),
                taskMaster.epicList);
        taskMaster.addTaskToList((taskMaster.createSubtask("Подзадача 3", "Описание подзадачи 3", 6)),
                taskMaster.subtaskList);
        System.out.println("_____________________________________________________________________________");
        for (Object task : taskMaster.taskList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.epicList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.subtaskList.values()) {
            System.out.println(task);
        }

        taskMaster.updateTask(1);
        taskMaster.updateTask(2);
        taskMaster.updateSubtask(4);
        taskMaster.updateSubtask(5);
        taskMaster.updateSubtask(7);
        System.out.println("_____________________________________________________________________________");
        for (Object task : taskMaster.taskList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.epicList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.subtaskList.values()) {
            System.out.println(task);
        }

        taskMaster.deleteTaskFromList(1);
        taskMaster.deleteSubtasksThisEpic(3);
        taskMaster.updateEpic(3);
        System.out.println("_____________________________________________________________________________");
        for (Object task : taskMaster.taskList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.epicList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.subtaskList.values()) {
            System.out.println(task);
        }


        taskMaster.deleteTaskFromList(3);
        System.out.println("_____________________________________________________________________________");
        for (Object task : taskMaster.taskList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.epicList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.subtaskList.values()) {
            System.out.println(task);
        }

        System.out.println("Добро пожаловать!");
        while (true) {
            printMenu();

            switch (userInput.getUserInput()) {

                case "1" -> {
                    System.out.println("Введите тип задачи, epic, task или subtask: ");
                    switch(userInput.getUserInput()) {

                        case "task" -> taskMaster.addTaskToList((taskMaster.createTask(userInput.getTitle(),
                                    userInput.getDescription())), taskMaster.taskList);



                        case "epic" -> {
                            Epic newEpic;
                            taskMaster.addTaskToList((newEpic = taskMaster.createEpic(userInput.getTitle(),
                                    userInput.getDescription())), taskMaster.epicList);
                                    System.out.println("Введите количество подзадач данного эпика");
                                    int quantitySubtasks = Integer.parseInt(userInput.getUserInput());
                                    for (int i = 0; i < quantitySubtasks ; i++) {
                                        taskMaster.addTaskToList((taskMaster.createSubtask(userInput.getTitle(),
                                                userInput.getDescription(), newEpic.getId())), taskMaster.subtaskList);

                            }
                                    taskMaster.updateEpic(newEpic.getId());
                        }

                        case "subtask" -> {
                            int epicId;
                            System.out.println("Введите поочередно заголовок задачи, описание, и " +
                                    "номер эпика для данной подзадачи");
                            taskMaster.addTaskToList(taskMaster.createSubtask(userInput.getTitle(),
                                userInput.getDescription(), epicId = Integer.parseInt(userInput.getUserInput())),
                                taskMaster.subtaskList);

                        taskMaster.updateEpic(epicId);
                        }



                        default -> System.out.println("Нужно указать один из предложенных типов задач: task, epic или subtask");
                    }
                }

                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    boolean userAnswer = userInput.getUserInput().trim().toLowerCase().equals("да");
                    switch (userAnswer) {
                        case true -> {
                            taskMaster.taskList.clear();
                            taskMaster.epicList.clear();
                            taskMaster.subtaskList.clear();
                            System.out.println("Список задач очищен");
                        }
                        case false -> System.out.println("Здорово, что вы передумали!");
                    }
                }

                case "3" -> {
                    System.out.println("Введите id искомого объекта");
                    int findObject = Integer.parseInt(userInput.getUserInput());
                    System.out.println(taskMaster.findTask(findObject));
                }

                case "4" -> {
                    for (Object task : taskMaster.taskList.values()) {
                        System.out.println(task);
                    }
                    for (Object task : taskMaster.epicList.values()) {
                        System.out.println(task);
                    }
                    for (Object task : taskMaster.subtaskList.values()) {
                        System.out.println(task);
                    }
                }

                case "5" -> {
                    System.out.println("Введите id эпика объекта");
                    System.out.println(taskMaster.subtasksThisEpic(Integer.parseInt(userInput.getUserInput())));
                }

               case "6" -> taskMaster.deleteSubtasksThisEpic(Integer.parseInt(userInput.getUserInput()));

                case "7" -> {
                    System.out.println("Введите номер задачи/подзадачи");
                    int userCommand = Integer.parseInt(userInput.getUserInput());
                    if (taskMaster.taskList.containsKey(userCommand)) {
                        taskMaster.updateTask(userCommand);
                    } else if (taskMaster.subtaskList.containsKey(userCommand)){
                       taskMaster.updateSubtask(userCommand);
                    }
                }


                case "8" -> {
                    System.out.println("Введите номер удаляемой задачи: ");
                    taskMaster.deleteTaskFromList(Integer.parseInt(userInput.getUserInput()));
                }

                case "9" -> {
                    inMemoryHistoryManager.getHistory();
                }


                case "10" -> {
                    return;
                }

                default -> System.out.println("Такой команды нет, введите команду из списка");
            }
        }
    }

    public static void printMenu() {
        String[] menuItems = new String[]{"1 - Создать задачу", "2 - Очистить список задач", "3 - Найти задачу",
                "4 - Вывести список задач","5 - Вывести подзадачи по номеру эпика", "6 - Удалить подзадачи по номеру эпика",
                "7 - Обновить статус задачи",
                "8 - Удалить задачу", "9 - вывести историю просмотров задач",  "10 - Выход"};
        System.out.println("Выберите пункт меню:");
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }
}




