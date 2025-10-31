package app.android.todomvc;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private ArrayList<Todo> todos = new ArrayList<>();
    private OnTodoClickListener listener;

    public interface OnTodoClickListener {
        void onToggleClick(String id);
        void onEditClick(String id, String currentTitle);
        void onDeleteClick(String id);
    }

    public void setOnTodoClickListener(OnTodoClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTodos(ArrayList<Todo> todos) {
        this.todos = todos;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.tvTitle.setText(todo.getTitle());
        holder.cbCompleted.setChecked(todo.isCompleted());

        // Apply strikethrough for completed items
        if (todo.isCompleted()) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setTextColor(0xFF777777);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTitle.setTextColor(0xFF4d4d4d);
        }

        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onToggleClick(todo.getId());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(todo.getId(), todo.getTitle());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(todo.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbCompleted;
        TextView tvTitle;
        ImageButton btnDelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
