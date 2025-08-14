package ui;

import manager.InMemoryTaskManager;

/*
public class MainUI {

    public static void main() throws IOException {

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        fileBackedTaskManager.loadFromFile(new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));
        while (true) {
            System.out.println("Добро пожаловать!");
            printMenu();

            switch (MainUI.getUserInput()) {

                case "1" -> {
                    System.out.println("Введите тип задачи, epic, task или subtask: ");
                    switch (getUserInput()) {

                        case "task" -> {
                            Task newTask = fileBackedTaskManager.createTask(getTitle(), getDescription());
                            fileBackedTaskManager.addTaskToList(newTask, fileBackedTaskManager.taskList);
                        }

                        case "epic" -> {
                            Epic newEpic = fileBackedTaskManager.createEpic(getTitle(), getDescription());
                            fileBackedTaskManager.addTaskToList(newEpic, fileBackedTaskManager.epicList);
                            System.out.println("Введите количество подзадач данного эпика");
                            int quantitySubtasks = Integer.parseInt(getUserInput());
                            for (int i = 0; i < quantitySubtasks; i++) {
                                fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createSubtask(getTitle(), getDescription(),
                                        newEpic.getId())), fileBackedTaskManager.subtaskList);
                            }
                            fileBackedTaskManager.updateEpic(newEpic);
                        }

                        case "subtask" -> {
                            System.out.println("Введите поочередно заголовок задачи, описание, и " +
                                    "номер эпика для данной подзадачи");
                            int epicId;
                            Subtask newSubtask = fileBackedTaskManager.createSubtask(getTitle(), getDescription(),
                                    epicId = Integer.parseInt(getUserInput()));
                            fileBackedTaskManager.addTaskToList(newSubtask, fileBackedTaskManager.subtaskList);
                            fileBackedTaskManager.updateEpic(fileBackedTaskManager.epicList.get(epicId));
                        }

                        default ->
                                System.out.println("Нужно указать один из предложенных типов задач: task, epic или subtask");
                    }
                }

                case "2" -> {
                    System.out.println("Вы действительно хотите очистить список задач? да / нет");
                    boolean userAnswer = getUserInput().trim().toLowerCase().equals("да");
                    if (userAnswer) {
                        fileBackedTaskManager.taskList.clear();
                        fileBackedTaskManager.epicList.clear();
                        fileBackedTaskManager.subtaskList.clear();
                        System.out.println("Список задач очищен");
                    } else {
                        System.out.println("Здорово, что вы передумали!");
                    }
                }

                case "3" -> {
                    System.out.println("Введите id искомого объекта");
                    int findObject = Integer.parseInt(getUserInput());
                    System.out.println(fileBackedTaskManager.findTask(findObject));
                }

                case "4" -> printItems(fileBackedTaskManager).forEach(task -> System.out.println(task));

                case "5" -> {
                    System.out.println("Введите id эпика объекта");
                    System.out.println(fileBackedTaskManager.getEpicSubtasks(Integer.parseInt(getUserInput())));
                }

                case "6" -> fileBackedTaskManager.deleteEpicSubtasks(Integer.parseInt(getUserInput()));

                case "7" -> {
                    System.out.println("Введите номер задачи/подзадачи");
                    int userCommand = Integer.parseInt(getUserInput());
                    if (fileBackedTaskManager.taskList.containsKey(userCommand)) {
                        fileBackedTaskManager.updateTask(fileBackedTaskManager.taskList.get(userCommand));
                    } else if (fileBackedTaskManager.subtaskList.containsKey(userCommand)) {
                        fileBackedTaskManager.updateSubtask(fileBackedTaskManager.subtaskList.get(userCommand));
                    }
                }

                case "8" -> {
                    System.out.println("Введите номер удаляемой задачи: ");
                    fileBackedTaskManager.deleteTaskFromList(Integer.parseInt(getUserInput()));
                }

                case "9" -> fileBackedTaskManager.getHistory().forEach(task -> System.out.println(task));

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
*/

