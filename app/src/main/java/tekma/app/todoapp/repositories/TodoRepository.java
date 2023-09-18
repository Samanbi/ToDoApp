package tekma.app.todoapp.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import tekma.app.todoapp.dao.TodoDao;
import tekma.app.todoapp.database.TodoDatabase;
import tekma.app.todoapp.entities.Todo;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodoList;

    public TodoRepository(Application application) {
        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao = database.todoDao();
        allTodoList = todoDao.getAllTodoList();
    }

    public void insert(Todo todo) {
        new InsertTodoAsyncTask(todoDao).execute(todo);
    }

    public void update(Todo todo) {
        new UpdateTodoAsyncTask(todoDao).execute(todo);
    }

    public void delete(Todo todo) {
        new DeleteTodoAsyncTask(todoDao).execute(todo);
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodoList;
    }

    private static class InsertTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        public InsertTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.insert(todos[0]);
            return null;
        }
    }

    private static class UpdateTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        public UpdateTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.update(todos[0]);
            return null;
        }
    }

    private static class DeleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao todoDao;

        public DeleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.delete(todos[0]);
            todoDao.delete(todos[0]);
            return null;
        }
    }


}
