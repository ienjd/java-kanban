package manager;

import java.util.ArrayList;

public interface HistoryManager<T>{

   ArrayList<T> getTasks();

   void removeNode(Node node);

   Node linkLast(T task);

   void add (T task);
}
