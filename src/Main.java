
import Enums.Statuses;
import Tasks.Task;
import Tasks.Epic;
import Tasks.Subtask;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать!");
        while (true) {
            printMenu();

            String userInput = scanner.next();

            switch (userInput) {

                case "1" -> {
                    System.out.println("Введите название задачи");
                    String title = scanner.nextLine();
                    scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    String description = scanner.nextLine();
                    Task newTask = new Task(title, description, TaskMaster.idCount+1, Statuses.NEW);
                    TaskMaster.taskList.put(newTask.getId(), newTask);
                }
                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    String userAnswer = scanner.next().equals("да") ? "да" : (scanner.next().equals("нет") ? "нет" : "нет");
                    switch (userAnswer) {
                        case "да" -> {
                            TaskMaster.taskList.clear();
                            System.out.println("Список задач очищен");
                        }
                        case "нет" -> System.out.println("Здорово, что вы передумали!");

                    }
                }

                case "3" -> {
                }
                case "4" -> {
                    System.out.println(TaskMaster.getTaskList());
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
    }
}



