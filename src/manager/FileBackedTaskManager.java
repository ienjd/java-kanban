package manager;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public static void main(String[] args) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(
                "C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        System.out.println("Загружено из fileBackedTaskManager1");
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 1", "Описание задачи 1")),
                fileBackedTaskManager1.taskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 2", "Описание задачи 2")),
                fileBackedTaskManager1.taskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createEpic("Эпик 1", "Описание эпика 1")),
                fileBackedTaskManager1.epicList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createSubtask("Подзадача 1", "Описание подзадачи 1", 3)),
                fileBackedTaskManager1.subtaskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createSubtask("Подзадача 2", "Описание подзадачи 2", 3)),
                fileBackedTaskManager1.subtaskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createSubtask("Подзадача 3", "Описание подзадачи 3", 3)),
                fileBackedTaskManager1.subtaskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createEpic("Эпик 2", "Описание эпика 2")),
                fileBackedTaskManager1.epicList);
        for (Task value : fileBackedTaskManager1.taskList.values()) {
            System.out.println(value);
        }
        for (Epic value : fileBackedTaskManager1.epicList.values()) {
            System.out.println(value);
        }
        for (Subtask value : fileBackedTaskManager1.subtaskList.values()) {
            System.out.println(value);
        }


        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 666", "Описание задачи 666")),
                fileBackedTaskManager1.taskList);

        System.out.println("добавлены новые задачи, выгружены в fileBackedTaskManager2 из уже имеющегося файла");

        FileBackedTaskManager fileBackedTaskManager2 = new FileBackedTaskManager();

        fileBackedTaskManager2.loadFromFile(new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));

        for (Task value : fileBackedTaskManager1.taskList.values()) {
            System.out.println(value);
        }
        for (Epic value : fileBackedTaskManager1.epicList.values()) {
            System.out.println(value);
        }
        for (Subtask value : fileBackedTaskManager1.subtaskList.values()) {
            System.out.println(value);
        }
    }

    public String filePath;

    public FileBackedTaskManager() {
    }

    public FileBackedTaskManager(String filePath) {
        this.filePath = filePath;
        if (!Files.exists(Path.of(filePath))) {
            createFileForSaving();
        }
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(filePath, Charset.forName("Windows-1251"), true);
             BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write(String.format("%s" + "," + "%s" + "," + "%s" + "," + "%s" + "," + "%s" + "," + "\n", "id",
                    "Class", "title", "status", "description"));
            for (Task item : taskList.values()) {
                if (findTask(item.getId()).equals(null)) {
                    bw.write(item.toString() + "\n");
                }

            }
            for (Epic item : epicList.values()) {
                if (findTask(item.getId()).equals(null)) {
                    bw.write(item.toString() + "\n");
                }
            }
            for (Subtask item : subtaskList.values()) {
                if (findTask(item.getId()).equals(null)) {
                    bw.write(item.toString() + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения!");
        }
    }

    public void deleteFile() throws ManagerSaveException {
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException e) {
            throw new ManagerSaveException("Есть проблемы");
        }
    }


    public Path createFileForSaving() {
        Path fileForSaving = null;
        try {
            fileForSaving = Files.createFile(Path.of(filePath));
            if (Files.exists(fileForSaving)) {
                System.out.println("Файл успешно создан");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
        }
        return fileForSaving;
    }

    public void loadFromFile(File file) {
        try (FileReader fr = new FileReader(file, Charset.forName("Windows-1251"));
             BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                String line = br.readLine();
                String[] tasks = line.split(";");
                for (String task : tasks) {
                    fromString(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    <T extends Task> void fromString(String value) throws ManagerSaveException {
        String[] attributes = value.split(",");
        T object;
        if (attributes[1].equals("Task")) {
            object = (T) new Task(attributes[2], attributes[4], Integer.parseInt(attributes[0]), Status.valueOf(attributes[3]));
            addTaskToList(object, taskList);
        } else if (attributes[1].equals("Epic")) {
            object = (T) new Epic(attributes[2], attributes[4], Integer.parseInt(attributes[0]), Status.valueOf(attributes[3]));
            addTaskToList(object, epicList);
        } else if (attributes[1].equals("Subtask")) {
            object = (T) new Subtask(attributes[2], attributes[4], Integer.parseInt(attributes[0]), Status.valueOf(attributes[3]),
                    Integer.parseInt(attributes[5]));
            object.setId(Integer.parseInt(attributes[0]));
            object.setStatus(Status.valueOf(attributes[3]));
            addTaskToList(object, subtaskList);
        }
    }


    @Override
    public void addTaskToList(Task task, HashMap hashMap) throws ManagerSaveException {
        super.addTaskToList(task, hashMap);
        save();
    }

    @Override
    public void deleteTaskFromList(int id) throws IOException {
        super.deleteTaskFromList(id);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteEpicSubtasks(int epicId) throws ManagerSaveException {
        super.deleteEpicSubtasks(epicId);
        save();
    }
}
