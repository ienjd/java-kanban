package manager;
import java.util.List;

public interface HistoryManager<T> {

    List<T> getHistory();

    void remove(int id);

    void add(T task);

}
