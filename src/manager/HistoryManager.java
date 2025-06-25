package manager;
import tasks.Task;

import java.util.List;

public interface HistoryManager{

   List<Task> getViewHistory();

   <T extends Task> void add (T task) throws CloneNotSupportedException;


}
