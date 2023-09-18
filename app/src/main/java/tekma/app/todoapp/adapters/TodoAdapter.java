package tekma.app.todoapp.adapters;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tekma.app.todoapp.R;
import tekma.app.todoapp.entities.Todo;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    private List<Todo> todoList = new ArrayList<>();
    private OnItemClickListener listener;
    private OnDoneStateChangedListener changedListener;

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.checkBox.setChecked(todo.isDone());
        holder.textView.setText(todo.getTitle());
        holder.textDate.setText(todo.getDate());

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setToDoList(List<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Todo getTodo(int position) {
        return todoList.get(position);
    }


    public class TodoHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView textDate;
        private CheckBox checkBox;
        public TodoHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txt_todo);
            textDate = itemView.findViewById(R.id.txt_date);
            checkBox = itemView.findViewById(R.id.chx_todo);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(todoList.get(getAdapterPosition()));
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (changedListener != null) {
                    changedListener.onStateChanged(todoList.get(getAdapterPosition()), isChecked);
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Todo todo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnDoneStateChangedListener {
        void onStateChanged(Todo todo, boolean isChecked);
    }

    public void setOnStateChangedListener(OnDoneStateChangedListener listener) {
        this.changedListener = listener;
    }
    
}
