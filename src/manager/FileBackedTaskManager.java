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

    public File file;

    public FileBackedTaskManager() {
    }

    public FileBackedTaskManager(String filePath) {
        this.file = new File(filePath);
        if (!Files.exists(Path.of(filePath))) {
            createFileForSaving();
        }
    }

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file, Charset.forName("Windows-1251"));
             BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write(String.format("%s" + "," + "%s" + "," + "%s" + "," + "%s" + "," + "%s" + "," + "%s" + "\n", "id",
                    "Class", "title", "status", "description", "epicId"));
            for (Object item : taskList.values()) {
                bw.write(item + "\n");
            }
            for (Object item : epicList.values()) {
                bw.write(item + "\n");
            }
            for (Object item : subtaskList.values()) {
                bw.write(item + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения!");
        }
    }

    public Path createFileForSaving() {
        Path fileForSaving = null;
        try {
            fileForSaving = Files.createFile(Path.of(file.toURI()));
            if (Files.exists(fileForSaving)) {
                System.out.println("Файл успешно создан");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
        }
        return fileForSaving;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.loadingFile(file);
        return fileBackedTaskManager;
    }


    public void loadingFile(File file) {
        try (FileReader fr = new FileReader(file, Charset.forName("Windows-1251"));
             BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                String line = br.readLine();
                String[] tasks = line.split(";");
                for (String task : tasks) {
                    fromString(task);
                }
            }
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
