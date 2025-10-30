package app.android.todomvc;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TodoViewModel viewModel;
    private TodoAdapter adapter;
    private EditText etNewTodo;
    private LinearLayout footer;
    private TextView tvItemCount, tvFilterAll, tvFilterActive, tvFilterCompleted;
    private FloatingActionButton fabClearCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        etNewTodo = findViewById(R.id.etNewTodo);
        RecyclerView rvTodos = findViewById(R.id.rvTodos);
        footer = findViewById(R.id.footer);
        tvItemCount = findViewById(R.id.tvItemCount);
        tvFilterAll = findViewById(R.id.tvFilterAll);
        tvFilterActive = findViewById(R.id.tvFilterActive);
        tvFilterCompleted = findViewById(R.id.tvFilterCompleted);
        fabClearCompleted = findViewById(R.id.fabClearCompleted);

        // Setup RecyclerView
        adapter = new TodoAdapter();
        rvTodos.setLayoutManager(new LinearLayoutManager(this));
        rvTodos.setAdapter(adapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        // Observe LiveData
        viewModel.getFilteredTodos().observe(this, todos -> {
            adapter.setTodos(todos);
            // Show/hide footer based on list content
            if (viewModel.getActiveCount().getValue() > 0 || todos.size() > 0) {
                footer.setVisibility(View.VISIBLE);
            } else {
                footer.setVisibility(View.GONE);
            }
        });

        viewModel.getActiveCount().observe(this, count -> {
            String text = getResources().getQuantityString(R.plurals.item_count, count, count);
            tvItemCount.setText(text);
        });

        viewModel.getCurrentFilter().observe(this, filter -> {
            updateFilterStyles(filter);
        });

        viewModel.getCompletedCount().observe(this, count -> {
            if (count > 0) {
                fabClearCompleted.show();
            } else {
                fabClearCompleted.hide();
            }
        });

        // Set Listeners
        setupListeners();
    }

    private void setupListeners() {
        etNewTodo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String todoText = etNewTodo.getText().toString();
                if (!todoText.trim().isEmpty()) {
                    viewModel.addTodo(todoText);
                    etNewTodo.setText("");
                }
                return true; // Indicate we've handled the action
            }
            return false; // Let the system handle the action
        });

        // Adapter click listeners
        adapter.setOnTodoClickListener(new TodoAdapter.OnTodoClickListener() {
            @Override
            public void onToggleClick(String id) {
                viewModel.toggleTodo(id);
            }

            @Override
            public void onEditClick(String id, String currentTitle) {
                showEditDialog(id, currentTitle);
            }

            @Override
            public void onDeleteClick(String id) {
                viewModel.deleteTodo(id);
            }
        });

        // Footer listeners
        tvFilterAll.setOnClickListener(v -> viewModel.setFilter("All"));
        tvFilterActive.setOnClickListener(v -> viewModel.setFilter("Active"));
        tvFilterCompleted.setOnClickListener(v -> viewModel.setFilter("Completed"));
        fabClearCompleted.setOnClickListener(v -> viewModel.clearCompleted());
    }

    private void showEditDialog(String id, String currentTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setText(currentTitle);
        input.setSelection(input.getText().length()); // Move cursor to end
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newTitle = input.getText().toString();
            viewModel.updateTodo(id, newTitle);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateFilterStyles(String activeFilter) {
        tvFilterAll.setTextColor(activeFilter.equals("All") ? 0xFFe77f11 : 0xFF777777);
        tvFilterActive.setTextColor(activeFilter.equals("Active") ? 0xFFe77f11 : 0xFF777777);
        tvFilterCompleted.setTextColor(activeFilter.equals("Completed") ? 0xFFe77f11 : 0xFF777777);
    }
}
