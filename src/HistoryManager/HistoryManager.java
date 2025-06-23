package HistoryManager;
import Tasks.Task;

import java.util.List;

public interface HistoryManager{

   <T extends Task> void add (T task) throws CloneNotSupportedException;

   List<Task> getHistory();
}
