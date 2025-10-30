package app.android.todomvc;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

    private FloatingActionButton fabFilter, fabFilterAll, fabFilterActive, fabFilterCompleted;
    private TextView tvLabelAll, tvLabelActive, tvLabelCompleted;
    private boolean isFabExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        etNewTodo = findViewById(R.id.etNewTodo);
        RecyclerView rvTodos = findViewById(R.id.rvTodos);
        footer = findViewById(R.id.footer);
        tvItemCount = findViewById(R.id.tvItemCount);
        fabClearCompleted = findViewById(R.id.fabClearCompleted);

        // NEW: Initialize FAB Menu Views
        fabFilter = findViewById(R.id.fabFilter);
        fabFilterAll = findViewById(R.id.fabFilterAll);
        fabFilterActive = findViewById(R.id.fabFilterActive);
        fabFilterCompleted = findViewById(R.id.fabFilterCompleted);
        tvLabelAll = findViewById(R.id.tvLabelAll);
        tvLabelActive = findViewById(R.id.tvLabelActive);
        tvLabelCompleted = findViewById(R.id.tvLabelCompleted);

        // Setup RecyclerView and ViewModel
        adapter = new TodoAdapter();
        rvTodos.setLayoutManager(new LinearLayoutManager(this));
        rvTodos.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        // Observe LiveData
        viewModel.getFilteredTodos().observe(this, todos -> {
            adapter.setTodos(todos);
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
            updateFabIcon(filter);
        });

        viewModel.getCompletedCount().observe(this, count -> {
            if (count > 0 && "Completed".equals(viewModel.getCurrentFilter().getValue())) {
                fabClearCompleted.show();
            } else {
                fabClearCompleted.hide();
            }
        });

        viewModel.getTotalCount().observe(this, count -> {
            if (count > 0) {
                fabFilter.show();
            } else {
                // Hide the FAB and ensure the menu is collapsed if it was open
                fabFilter.hide();
                if (isFabExpanded) {
                    collapseFabMenu();
                }
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


        // NEW: Main FAB listener to toggle menu
        fabFilter.setOnClickListener(v -> toggleFabMenu());

        // NEW: Mini FAB listeners
        fabFilterAll.setOnClickListener(v -> {
            viewModel.setFilter("All");
            collapseFabMenu();
        });
        fabFilterActive.setOnClickListener(v -> {
            viewModel.setFilter("Active");
            collapseFabMenu();
        });
        fabFilterCompleted.setOnClickListener(v -> {
            viewModel.setFilter("Completed");
            collapseFabMenu();
        });

        fabClearCompleted.setOnClickListener(v -> viewModel.clearCompleted());
    }

    private void toggleFabMenu() {
        if (isFabExpanded) {
            collapseFabMenu();
        } else {
            expandFabMenu();
        }
    }

    private void expandFabMenu() {
        isFabExpanded = true;
        // Animate FAB to rotate
        fabFilter.animate().rotation(45f).setDuration(200).start();

        // Animate mini FABs and labels to appear
        fabFilterAll.setVisibility(View.VISIBLE);
        tvLabelAll.setVisibility(View.VISIBLE);
        fabFilterAll.animate().translationY(- getResources().getDimension(R.dimen.fab_margin)).alpha(1f).setDuration(200).start();
        tvLabelAll.animate().translationY(- getResources().getDimension(R.dimen.fab_margin)).alpha(1f).setDuration(200).start();

        fabFilterActive.setVisibility(View.VISIBLE);
        tvLabelActive.setVisibility(View.VISIBLE);
        fabFilterActive.animate().translationY(- 2 * getResources().getDimension(R.dimen.fab_margin)).alpha(1f).setDuration(200).start();
        tvLabelActive.animate().translationY(- 2 * getResources().getDimension(R.dimen.fab_margin)).alpha(1f).setDuration(200).start();

        fabFilterCompleted.setVisibility(View.VISIBLE);
        tvLabelCompleted.setVisibility(View.VISIBLE);
        fabFilterCompleted.animate().translationY(- 3 * getResources().getDimension(R.dimen.fab_margin)).alpha(1f).setDuration(200).start();
        tvLabelCompleted.animate().translationY(- 3 * getResources().getDimension(R.dimen.fab_margin)).alpha(1f).setDuration(200).start();
    }

    private void collapseFabMenu() {
        isFabExpanded = false;
        // Animate FAB to rotate back
        fabFilter.animate().rotation(0f).setDuration(200).start();

        // Animate mini FABs and labels to disappear
        fabFilterAll.animate().translationY(0).alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabFilterAll.setVisibility(View.INVISIBLE);
                tvLabelAll.setVisibility(View.INVISIBLE);
            }
        }).start();
        tvLabelAll.animate().translationY(0).alpha(0f).setDuration(200).start();

        fabFilterActive.animate().translationY(0).alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabFilterActive.setVisibility(View.INVISIBLE);
                tvLabelActive.setVisibility(View.INVISIBLE);
            }
        }).start();
        tvLabelActive.animate().translationY(0).alpha(0f).setDuration(200).start();

        fabFilterCompleted.animate().translationY(0).alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fabFilterCompleted.setVisibility(View.INVISIBLE);
                tvLabelCompleted.setVisibility(View.INVISIBLE);
            }
        }).start();
        tvLabelCompleted.animate().translationY(0).alpha(0f).setDuration(200).start();
    }

    // NEW: Method to update the main FAB's icon based on the filter
    private void updateFabIcon(String filter) {
        switch (filter) {
            case "Active":
                fabFilter.setImageResource(R.drawable.ic_filter_active);
                break;
            case "Completed":
                fabFilter.setImageResource(R.drawable.ic_filter_completed);
                break;
            case "All":
            default:
                fabFilter.setImageResource(R.drawable.ic_filter_all);
                break;
        }
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
