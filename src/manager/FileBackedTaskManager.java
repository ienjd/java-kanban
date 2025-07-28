package manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager{
    public static final String filePathForSave = "C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks";
    public static final String filePathForDelete = "C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv";

    public FileBackedTaskManager() {

    }

    public <T> void save() throws IOException {
        ArrayList<T> allItems = new ArrayList<>();
        taskList.values().forEach(task -> allItems.add((T) task));
        epicList.values().forEach(epic -> allItems.add((T) epic));
        subtaskList.values().forEach(subtask -> allItems.add((T) subtask));
        Writer fileWriter = new FileWriter(filePathForSave+"\\file.csv");
        try (BufferedWriter bw = new BufferedWriter(fileWriter)){
            for (T item : allItems) {
                bw.write(item.toString());
            }
        }
    }

    public void deleteFile() {
        try {
            Files.delete(Path.of(filePathForDelete));
            System.out.println("Файл успешно удален");
        } catch (IOException e) {
            System.out.println("проблемка");
        }
    }


    public Path createFileForSaving (){
        Path fileForSaving = null;
        try {
            fileForSaving = Files.createFile(Path.of(filePathForSave+"\\file.csv"));
            if(Files.exists(fileForSaving)){
                System.out.println("Файл успешно создан");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
        }
        return fileForSaving;
    }


    @Override
    public void addTaskToList(Task task, HashMap hashMap) {
        super.addTaskToList(task, hashMap);
        //save();
    }

    @Override
    public void deleteTaskFromList(int id) throws IOException {
        super.deleteTaskFromList(id);
        //save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        //save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
       // save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        //save();
    }
}
