package Main;
import TaskMaster.TaskMaster;
import Tasks.Epic;
import UserInputs.UserInput;

public class Main {

    public static void main(String[] args) {
        UserInput userInput = new UserInput();
        TaskMaster taskMaster = new TaskMaster();
        System.out.println("Добро пожаловать!");
        while (true) {
            printMenu();

            switch (userInput.getUserInput()) {

                case "1" -> {
                    System.out.println("Введите тип задачи, epic или task: ");
                    String taskOrEpic = userInput.getUserInput();
                    switch(taskOrEpic.equals("epic") ? "epic" : "task") {

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


                case "6" -> {
                    System.out.println("Введите номер задачи/подзадачи");
                    int userCommand = Integer.parseInt(userInput.getUserInput());
                    if (taskMaster.taskList.containsKey(userCommand)) {
                        taskMaster.updateTask(userCommand);
                    } else if (taskMaster.subtaskList.containsKey(userCommand)){
                       taskMaster.updateSubtask(userCommand);
                    }
                }


                case "7" -> {
                    System.out.println("Введите номер удаляемой задачи: ");
                    taskMaster.deleteTaskFromList(Integer.parseInt(userInput.getUserInput()));
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
                "4 - Вывести список задач","5 - Вывести подзадачи по номеру эпика", "6 - Обновить статус задачи",
                "7 - Удалить задачу", "8 - Выход"};
        System.out.println("Выберите пункт меню:");
        for (String menuItem : menuItems) {
            System.out.println(menuItem);
        }
    }
}




