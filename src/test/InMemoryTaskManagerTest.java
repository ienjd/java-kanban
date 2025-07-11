package test;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    /*Тест "проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера" невозможно провести
    поскольку в рамках менеджера задач невозможно задавать id вручную, все id генерируются;*/

    /* тест на невозможность добавления Epicа в число своих подзадач не представляется возможным реализовать, поскольку
     связь Subtask с Epicом реализована через создание у Subtask приватного поля epicId. Таким образом, Epic в принципе
     невозможно сделать Subtaskом */

    @Test// Тест проверяет, что задачи добавляемые в менеджер неизменны, а также, что менеджер возвращет корректные
        // задачи при использовании поиска
    void TasksAddedToManagerAreNotChangedAndManagerReturnCorrectTasksInFindMethod(){
        Task firstTask = inMemoryTaskManager.createTask("Первая задача", "Описание первой задачи");
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", firstEpic.getId());
        inMemoryTaskManager.addTaskToList(firstTask, inMemoryTaskManager.taskList);
        inMemoryTaskManager.addTaskToList(firstEpic, inMemoryTaskManager.epicList);
        inMemoryTaskManager.addTaskToList(firstSubtask, inMemoryTaskManager.subtaskList);

        Assertions.assertEquals(firstTask, inMemoryTaskManager.findTask(1));
        Assertions.assertEquals(firstEpic, inMemoryTaskManager.findTask(2));
        Assertions.assertEquals(firstSubtask, inMemoryTaskManager.findTask(3));
    }

}