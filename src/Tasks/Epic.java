package Tasks;

import Enums.Statuses;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> idSubtasksThisEpic = new ArrayList<>();


    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }

}
