package app.android.todomvc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TodoViewModel extends AndroidViewModel {

    private static final String PREFS_NAME = "todo_prefs";
    private static final String TODOS_KEY = "todos_list";

    private MutableLiveData<ArrayList<Todo>> allTodos = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Todo>> filteredTodos = new MutableLiveData<>();
    private MutableLiveData<String> currentFilter = new MutableLiveData<>("All");
    private MutableLiveData<Integer> activeCount = new MutableLiveData<>(0);

    private SharedPreferences prefs;
    private Gson gson = new Gson();

    public TodoViewModel(Application application) {
        super(application);
        prefs = application.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadTodos();
    }

    public LiveData<ArrayList<Todo>> getFilteredTodos() {
        return filteredTodos;
    }

    public LiveData<Integer> getActiveCount() {
        return activeCount;
    }

    public LiveData<String> getCurrentFilter() {
        return currentFilter;
    }

    private void loadTodos() {
        String json = prefs.getString(TODOS_KEY, null);
        Type type = new TypeToken<ArrayList<Todo>>() {}.getType();
        ArrayList<Todo> list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<>();
        }
        allTodos.setValue(list);
        applyFilter(currentFilter.getValue());
    }

    private void saveTodos() {
        String json = gson.toJson(allTodos.getValue());
        prefs.edit().putString(TODOS_KEY, json).apply();
    }

    public void addTodo(String title) {
        if (title == null || title.trim().isEmpty()) return;
        ArrayList<Todo> current = allTodos.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(0, new Todo(UUID.randomUUID().toString(), title.trim(), false));
        allTodos.setValue(current);
        saveTodos();
        applyFilter(currentFilter.getValue());
    }

    public void toggleTodo(String id) {
        ArrayList<Todo> current = allTodos.getValue();
        if (current == null) return;
        for (Todo todo : current) {
            if (todo.getId().equals(id)) {
                todo.setCompleted(!todo.isCompleted());
                break;
            }
        }
        allTodos.setValue(current);
        saveTodos();
        applyFilter(currentFilter.getValue());
    }

    public void updateTodo(String id, String newTitle) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            deleteTodo(id);
            return;
        }
        ArrayList<Todo> current = allTodos.getValue();
        if (current == null) return;
        for (Todo todo : current) {
            if (todo.getId().equals(id)) {
                todo.setTitle(newTitle.trim());
                break;
            }
        }
        allTodos.setValue(current);
        saveTodos();
        applyFilter(currentFilter.getValue());
    }

    public void deleteTodo(String id) {
        ArrayList<Todo> current = allTodos.getValue();
        if (current == null) return;
        current.removeIf(todo -> todo.getId().equals(id));
        allTodos.setValue(current);
        saveTodos();
        applyFilter(currentFilter.getValue());
    }

    public void clearCompleted() {
        ArrayList<Todo> current = allTodos.getValue();
        if (current == null) return;
        current.removeIf(Todo::isCompleted);
        allTodos.setValue(current);
        saveTodos();
        applyFilter(currentFilter.getValue());
    }

    public void setFilter(String filter) {
        currentFilter.setValue(filter);
        applyFilter(filter);
    }

    private void applyFilter(String filter) {
        ArrayList<Todo> all = allTodos.getValue();
        if (all == null) return;

        ArrayList<Todo> result = new ArrayList<>();
        int active = 0;
        for (Todo todo : all) {
            if (!todo.isCompleted()) active++;
            if ("All".equals(filter)) {
                result.add(todo);
            } else if ("Active".equals(filter) && !todo.isCompleted()) {
                result.add(todo);
            } else if ("Completed".equals(filter) && todo.isCompleted()) {
                result.add(todo);
            }
        }
        activeCount.setValue(active);
        filteredTodos.setValue(result);
    }
}
