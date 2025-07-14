
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

class InMemoryTaskManagerTest {

    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    /*Тест "проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера" невозможно провести
    поскольку в рамках менеджера задач невозможно задавать id вручную, все id генерируются;*/

    /* тест на невозможность добавления Epicа в число своих подзадач не представляется возможным реализовать, поскольку
     связь Subtask с Epicом реализована через создание у Subtask приватного поля epicId. Таким образом, Epic в принципе
     невозможно сделать Subtaskом */

    @Test
// Тест проверяет, что задачи добавляемые в менеджер неизменны, а также, что менеджер возвращает корректные
        // задачи при использовании поиска
    void tasksAddedToManagerAreNotChangedAndManagerReturnCorrectTasksInFindMethod() {
        Task firstTask = inMemoryTaskManager.createTask("Первая задача", "Описание первой задачи");
        inMemoryTaskManager.addTaskToList(firstTask, inMemoryTaskManager.taskList);
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        inMemoryTaskManager.addTaskToList(firstEpic, inMemoryTaskManager.epicList);
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 2);
        inMemoryTaskManager.addTaskToList(firstSubtask, inMemoryTaskManager.subtaskList);
        Assertions.assertEquals(firstTask, inMemoryTaskManager.findTask(1));
        Assertions.assertEquals(firstEpic, inMemoryTaskManager.findTask(2));
        Assertions.assertEquals(firstSubtask, inMemoryTaskManager.findTask(3));
    }


    @Test
        //Тест проверяет, что удаление эпика влечет за собой удаление сабтасков данного эпика
    void deleteEpicDeletingSubtasksThisEpic() {
        inMemoryTaskManager.deleteTaskFromList(2);
        Assertions.assertTrue(inMemoryTaskManager.getEpicSubtasks(2).isEmpty());
    }

}