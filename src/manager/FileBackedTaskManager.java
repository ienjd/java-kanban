package manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import tasks.Status;
import java.util.ArrayList;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager{
    public final String filePath = "C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv";

    public FileBackedTaskManager() {
    }

    public FileBackedTaskManager(String path) {
    }

    public void save() throws IOException {

        Writer fileWriter = new FileWriter(filePath, Charset.forName("Windows-1251"));
        try (BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write(String.format("%-5s" + "%-20s" + "%-20s" + "%-20s" + "%-20s"+ "\n", "id", "Class", "title", "status", "description"));
            for (Task item : taskList.values()) {
                bw.write(item.toString() + "\n");

            }
            for (Epic item : epicList.values()) {
                bw.write(item.toString() + "\n");

            }
            for (Subtask item : subtaskList.values()) {
                bw.write(item.toString() + "\n");

            }
        }
    }

    public void deleteFile() {
        try {
            Files.delete(Path.of(filePath));
            System.out.println("Файл успешно удален");
        } catch (IOException e) {
            System.out.println("проблемка");
        }
    }


    public Path createFileForSaving (){
        Path fileForSaving = null;
        try {
            fileForSaving = Files.createFile(Path.of(filePath));
            if(Files.exists(fileForSaving)){
                System.out.println("Файл успешно создан");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
        }
        return fileForSaving;
    }

    public static void loadFromFile(File file){
        try(FileReader fr = new FileReader(file, Charset.forName("Windows-1251")); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()){
                String line = br.readLine();
                String[] tasks = line.split(";");
                for (String task : tasks) {
                    String[] attributes = task.split(",");
                    for (String attribute : attributes) {
                        System.out.println(attribute.trim());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
