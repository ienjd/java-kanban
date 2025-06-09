package Main;

import Enums.Statuses;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import UserInputs.UserInputs;

import java.util.ArrayList;
import java.util.HashMap;
public class Main {

    public static void main(String[] args) {
        UserInputs userInputs = new UserInputs();
        System.out.println("Добро пожаловать!");
        while (true) {
            printMenu();

            switch (userInputs.getUserInput()) {

                case "1" -> {

                    String taskOrEpic = userInputs.getUserInput();
                    switch(taskOrEpic.equals("epic") ? "epic" : "task") {

                        case "task" -> {
                            Task.addTaskToList(Task.createTask(userInputs.getTitle(), userInputs.getDescription()));
                        }


                        case "epic" -> {
                            Epic.addTaskToList(Epic.createTask(userInputs.getTitle(), userInputs.getDescription()));
                        }

                    }
                }

                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    boolean userAnswer = userInputs.getUserInput().trim().toLowerCase().equals("да");
                    switch (userAnswer) {
                        case true -> {
                            TaskMaster.TASK_LIST.clear();
                            TaskMaster.EPIC_LIST.clear();
                            TaskMaster.SUBTASK_LIST.clear();
                            System.out.println("Список задач очищен");
                        }
                        case false -> System.out.println("Здорово, что вы передумали!");
                    }
                }

                case "3" -> {
                    System.out.println("Введите id искомого объекта");
                    int findObject = Integer.parseInt(userInputs.getUserInput());
                    System.out.println(TaskMaster.findTask(findObject));
                }

                case "4" -> {
                    for (Object task : TaskMaster.TASK_LIST.values()) {
                        System.out.println(task);
                    }
                    for (Object task : TaskMaster.EPIC_LIST.values()) {
                        System.out.println(task);
                    }
                    for (Object task : TaskMaster.SUBTASK_LIST.values()) {
                        System.out.println(task);
                    }
                }

                case "5" -> {
                    System.out.println("Введите id эпика объекта");
                    System.out.println(Epic.subtasksThisEpic(Integer.parseInt(userInputs.getUserInput())));
                }


                case "6" -> {
                    System.out.println("Введите номер задачи/подзадачи");
                    int userCommand = Integer.parseInt(userInputs.getUserInput());
                    if (TaskMaster.getTaskList().containsKey(userCommand)) {
                        Task.updateTask(userCommand);
                    } else if (TaskMaster.getSubtaskList().containsKey(userCommand)){
                        Subtask.updateSubtask(userCommand);
                    }
                }


                case "7" -> {
                    TaskMaster.deleteTaskFromList(Integer.parseInt(userInputs.getUserInput()));
                }

                case "8" -> {
                    return;
                }

                default -> System.out.println("Такой команды нет, введите команду из списка");
            }
        }
    }

    public static void printMenu() {
        String[] menuItems = new String[]{"1 - Создать задачу", "2 - Очистить список задач", "3 - Найти задачу",
                "4 - Вывести список задач","5 - Вывести подзадачи по номеру эпика", "6 - Обновить статус задачи", "7 - Удалить задачу", "8 - Выход"};
        System.out.println("Выберите пункт меню:");
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }

    public static class TaskMaster {
        public static int idCount = 0;
        public static final HashMap<Integer, Task> TASK_LIST = new HashMap<>();
        public static final HashMap<Integer, Epic> EPIC_LIST = new HashMap<>();
        public static final HashMap<Integer, Subtask> SUBTASK_LIST = new HashMap<>();

        public static HashMap<Integer, Task> getTaskList() {
            return TASK_LIST;
        }

        public static HashMap<Integer, Epic> getEpicList() {
            return EPIC_LIST;
        }

        public static HashMap<Integer, Subtask> getSubtaskList() {
            return SUBTASK_LIST;
        }

        public static int getIdCount() {
            return idCount;
        }

        public static void setIdCount() {
            idCount++;
        }

        public static Object findTask(int findId){

            Object object = null;
            if (TaskMaster.getTaskList().containsKey(findId)) {
                object = TaskMaster.getTaskList().get(findId);
            } else if (TaskMaster.getEpicList().containsKey(findId)) {
                object = TaskMaster.getEpicList().get(findId);
            } else if (TaskMaster.getSubtaskList().containsKey(findId)) {
                object = TaskMaster.getSubtaskList().get(findId);
            }
            return object;
        }

        private static void deleteTaskFromList(int id){

            if (getTaskList().containsKey(id)){
                TaskMaster.getTaskList().remove(id);
            } else if (getEpicList().containsKey(id)){
                TaskMaster.getEpicList().remove(id);
            } else if (getSubtaskList().containsKey(id)) {
                TaskMaster.getSubtaskList().remove(id);
            } else {
                System.out.println("Данного элемента уже нет в списке");
            }
        }


    }
}




