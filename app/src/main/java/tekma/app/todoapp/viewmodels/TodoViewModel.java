package tekma.app.todoapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import tekma.app.todoapp.entities.Todo;
import tekma.app.todoapp.repositories.TodoRepository;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository repository;
    private LiveData<List<Todo>> allTodoList;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodoList = repository.getAllTodos();
    }

    public void insert(Todo toDo) {
        repository.insert(toDo);
    }

    public void update(Todo toDo) {
        repository.update(toDo);
    }

    public void delete(Todo toDo) {
        if(toDo == null || toDo.getId() <= 0)
            return;
        repository.delete(toDo);
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodoList;
    }
    
}
