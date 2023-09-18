package tekma.app.todoapp.database;

import android.content.Context;

import androidx.room.Database;

import tekma.app.todoapp.dao.TodoDao;
import tekma.app.todoapp.entities.Todo;

import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Todo.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase instance;
    public abstract TodoDao todoDao();

    public static synchronized TodoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TodoDatabase.class,
                            "todo_db")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }


}
