package tekma.app.todoapp.entities;

import android.icu.text.SimpleDateFormat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Locale;

@Entity(tableName = "tbl_todo")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String description;

    private boolean done;

    private String date;



    public Todo(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDate() {
        return date;
    }

}
