
import Enums.Statuses;
import Tasks.Task;
import UserInputs.UserInputs;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        UserInputs userInputs = new UserInputs();
        System.out.println("Добро пожаловать!");
        while (true) {
            printMenu();

            switch (userInputs.getUserInput()) {

                case "1" -> TaskMaster.addTaskToList(TaskMaster.createTask(userInputs.getTitle(), userInputs.getDescription()));

                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    boolean userAnswer = userInputs.getUserInput().trim().toLowerCase().equals("да");
                    switch (userAnswer) {
                        case true -> {
                            TaskMaster.taskList.clear();
                            System.out.println("Список задач очищен");
                        }
                        case false -> System.out.println("Здорово, что вы передумали!");
                    }
                }

                case "3" -> {
                }
                case "4" -> {
                    for (Object task : TaskMaster.taskList.values()) {
                        System.out.println(task);
                    }
                }
                case "5" -> {
                }
                case "6" -> {
                }
                case "7" -> {
                    return;
                }
                default -> System.out.println("Такой команды нет, введите команду из списка");
            }
        }
    }

    public static void printMenu() {
        String[] menuItems = new String[]{"1 - Создать задачу", "2 - Очистить список задач", "3 - Найти задачу",
                "4 - Вывести список задач", "5 - Выполнить задачу", "6 - Удалить задачу", "7 - Выход"};
        System.out.println("Выберите пункт меню:");
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }



    public static class TaskMaster {
        private static int idCount = 0;
        static HashMap<Integer, Object> taskList = new HashMap<>();

        public static HashMap<Integer, Object> getTaskList() {
            return taskList;
        }

        public static int getIdCount() {
            return idCount;
        }

        private static Task createTask(String title, String description){
            Task newTask = new Task(title, description, TaskMaster.idCount+1, Statuses.NEW);
            idCount++;
            return newTask;
        }

        private static void addTaskToList(Task task){
            TaskMaster.taskList.put(task.getId(), task);
        }
    }
}



