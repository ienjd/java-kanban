package Tasks;

import Enums.Statuses;

import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, Subtask> subtasksInEpic = new HashMap<>();
    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }

}
