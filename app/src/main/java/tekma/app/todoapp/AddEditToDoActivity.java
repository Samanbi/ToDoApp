package tekma.app.todoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class AddEditToDoActivity extends AppCompatActivity {

    public static String EXTRA_ID = "saman.app.todoapp.todo_id";
    public static String EXTRA_TITLE = "saman.app.todoapp.todo_title";
    public static String EXTRA_DESCRIPTION = "saman.app.todoapp.todo_description";

    private EditText txtTitle, txtDescription;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
    }

    private void init() {
        bindViews();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit ToDo");
            txtTitle.setText(Objects.requireNonNull(intent.getExtras()).getString(EXTRA_TITLE));
            txtDescription.setText(intent.getExtras().getString(EXTRA_DESCRIPTION));
        } else {
            setTitle("Add ToDo");
        }

        btnSave.setOnClickListener(v -> {
            String title = txtTitle.getText().toString();
            String description = txtDescription.getText().toString();

            if (checkFormValidation(title)) return;

            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);

            if (intent.hasExtra(EXTRA_ID)) {
                int id = Objects.requireNonNull(getIntent().getExtras()).getInt(EXTRA_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ID, id);
                }
            }
            setResult(RESULT_OK, data);
            finish();
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // بررسی آیتم منو
        if (item.getItemId() == android.R.id.home) {
            // اجرای عملکرد بازگشت به عقب
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private boolean checkFormValidation(String title) {
        if (title == null || title.equals("")) {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void bindViews() {
        txtTitle = findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_description);
        btnSave = findViewById(R.id.btn_save);
    }
}