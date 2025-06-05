
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

                case "1" ->
                        TaskMaster.addTaskToList(TaskMaster.createTask(userInputs.getTitle(), userInputs.getDescription()));

                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    boolean userAnswer = userInputs.getUserInput().trim().toLowerCase().equals("да");
                    switch (userAnswer) {
                        case true -> {
                            TaskMaster.TASK_LIST.clear();
                            System.out.println("Список задач очищен");
                        }
                        case false -> System.out.println("Здорово, что вы передумали!");
                    }
                }

                case "3" -> {
                    System.out.println("Введите id искомого объекта");
                    System.out.println(TaskMaster.findTask(Integer.parseInt(userInputs.getUserInput())));
                }

                case "4" -> {
                    for (Object task : TaskMaster.TASK_LIST.values()) {
                        System.out.println(task);
                    }
                }

                case "5" ->  TaskMaster.updateTask(Integer.parseInt(userInputs.getUserInput()));


                case "6" -> TaskMaster.deleteTaskFromList(Integer.parseInt(userInputs.getUserInput()));


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
        public static final HashMap<Integer, Object> TASK_LIST = new HashMap<>();

        public static HashMap<Integer, Object> getTaskList() {
            return TASK_LIST;
        }

        public static int getIdCount() {
            return idCount;
        }

        private static void setIdCount() {
            idCount++;
        }

        private static Task createTask(String title, String description) {
            Task newTask = new Task(title, description, TaskMaster.getIdCount() + 1, Statuses.NEW);
            setIdCount();
            return newTask;
        }

        private static void addTaskToList(Task task) {
            TaskMaster.TASK_LIST.put(task.getId(), task);
        }

        private static Task findTask(int taskId) {
            Task task = new Task();
            for (Integer key : TASK_LIST.keySet()) {
                if (key.equals(taskId)) {
                    task = (Task) TASK_LIST.get(taskId);
                }
            }
            return task;
        }

        private static void deleteTaskFromList(int id){
            TaskMaster.getTaskList().remove(id);
        }

        private static Task updateTask(int id){

            Statuses status = findTask(id).getStatus()==Statuses.NEW ? Statuses.IN_PROGRESS : Statuses.DONE;
           Task task = new Task(findTask(id).getTitle(), findTask(id).getDescription(), findTask(id).getId(), status);
           return task;
        }
    }
}




