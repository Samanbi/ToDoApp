package tekma.app.todoapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import tekma.app.todoapp.adapters.TodoAdapter;
import tekma.app.todoapp.entities.Todo;
import tekma.app.todoapp.viewmodels.TodoViewModel;

public class MainActivity extends AppCompatActivity {


    private TodoViewModel todoViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
    }

    private void init() {
        bindViews();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        TodoAdapter adapter = new TodoAdapter();
        recyclerView.setAdapter(adapter);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, adapter::setToDoList);

        ActivityResultLauncher<Intent> addTodoActivityResultLauncher = getAddToDoIntentActivityResultLauncher();

        fabAdd.setOnClickListener(v -> {
            Intent addIntent = new Intent(MainActivity.this, AddEditToDoActivity.class);
            addTodoActivityResultLauncher.launch(addIntent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                todoViewModel.delete(adapter.getTodo(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Todo deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        ActivityResultLauncher<Intent> editTodoActivityResultLauncher = getEditIntentActivityResultLauncher();

        adapter.setOnItemClickListener(toDo -> {
            Intent editIntent = new Intent(MainActivity.this, AddEditToDoActivity.class);
            editIntent.putExtra(AddEditToDoActivity.EXTRA_ID, toDo.getId());
            editIntent.putExtra(AddEditToDoActivity.EXTRA_TITLE, toDo.getTitle());
            editIntent.putExtra(AddEditToDoActivity.EXTRA_DESCRIPTION, toDo.getDescription());
            editTodoActivityResultLauncher.launch(editIntent);
        });

        adapter.setOnStateChangedListener((toDo, isChecked) -> {
            toDo.setDone(isChecked);
            todoViewModel.update(toDo);
            Toast.makeText(this, "Todo state changed", Toast.LENGTH_SHORT).show();
        });
    }

    private ActivityResultLauncher<Intent> getEditIntentActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) return;
                        int id = data.getIntExtra(AddEditToDoActivity.EXTRA_ID, -1);
                        if (id == -1) {
                            Toast.makeText(this, "Invalid data id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String title = data.getStringExtra(AddEditToDoActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(AddEditToDoActivity.EXTRA_DESCRIPTION);
                        String date = getDate();
                        Todo toDo = new Todo(title, description, date);
                        toDo.setId(id);
                        todoViewModel.update(toDo);
                        Toast.makeText(this, "todo updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ActivityResultLauncher<Intent> getAddToDoIntentActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) return;
                        String title = data.getStringExtra(AddEditToDoActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(AddEditToDoActivity.EXTRA_DESCRIPTION);
                        String date = getDate();
                        Todo toDo = new Todo(title, description, date);
                        todoViewModel.insert(toDo);
                        Toast.makeText(this, "new todo added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MM/dd HH:mm");
            return dateTime.format(formatter);
        }
        return "no date";
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab_add);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }
}