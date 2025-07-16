import manager.InMemoryTaskManager;

public class Main {
    public void main(String[] args) {
        InMemoryTaskManager taskMaster = new InMemoryTaskManager();
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
        taskMaster.addTaskToList((taskMaster.createSubtask("Подзадача 3", "Описание подзадачи 3", 3)),
                taskMaster.subtaskList);
        taskMaster.addTaskToList((taskMaster.createEpic("Эпик 2", "Описание эпика 2")),
                taskMaster.epicList);
        for (Object task : taskMaster.taskList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.epicList.values()) {
            System.out.println(task);
        }
        for (Object task : taskMaster.subtaskList.values()) {
            System.out.println(task);
        }
        System.out.println("_____________________________________________________________________________");
        taskMaster.findTask(1);
        taskMaster.findTask(2);
        taskMaster.findTask(7);
        taskMaster.findTask(3);
        taskMaster.findTask(5);
        taskMaster.findTask(4);
        taskMaster.findTask(3);
        taskMaster.findTask(7);
        taskMaster.findTask(6);
        taskMaster.findTask(2);
        taskMaster.findTask(4);
        taskMaster.getHistory().forEach(task -> System.out.println(task));
        System.out.println("_____________________________________________________________________________");
        taskMaster.findTask(7);
        taskMaster.findTask(4);
        taskMaster.findTask(6);
        taskMaster.findTask(5);
        taskMaster.findTask(7);
        taskMaster.findTask(2);
        taskMaster.findTask(1);
        taskMaster.findTask(4);
        taskMaster.findTask(7);
        taskMaster.findTask(2);
        taskMaster.findTask(1);
        taskMaster.getHistory().forEach(task -> System.out.println(task));
        System.out.println("_____________________________________________________________________________");
        taskMaster.deleteTaskFromList(1);
        taskMaster.deleteTaskFromList(3);
        taskMaster.getHistory().forEach(task -> System.out.println(task));
        System.out.println("_____________________________________________________________________________");
    }
}





