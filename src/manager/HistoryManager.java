package manager;
import java.util.List;

public interface HistoryManager<T> {

    public List<T> getHistory();

    public void remove(int id);

    public void add(T task);
}
