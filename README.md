# Native Android Todo App

This project is a faithful native Android implementation of the classic [TodoMVC](https://todomvc.com/) application. It showcases modern Android development practices by replicating the full functionality and UI/UX of the original web example using only native UI components.

## 📸 Screenshots

*(Note: Actual screenshots would be placed here)*

*   **Initial State:** The app starts with a clean interface, showing only the title and the input field.
*   **Populated List:** A list of todos is displayed, with the footer showing the item count and filter options.
*   **Expandable Filter Menu:** The main FAB is expanded to reveal the "All", "Active", and "Completed" filter options.
*   **Completed View:** The "Completed" filter is active, showing only finished tasks and revealing the "Clear Completed" FAB.

## ✨ Features

This application includes all the core functionalities defined by the TodoMVC specification:

### ✅ Todo Management (CRUD)
*   **Create:** Add new todos with the main input field.
*   **Read:** View the full list of todos.
*   **Update:**
    *   Toggle the completion status of any todo with a checkbox.
    *   Edit the text of an existing todo by tapping on it.
*   **Delete:**
    *   Remove a single todo item.
    *   Bulk-delete all completed todos at once.

### 🔍 Filtering & Viewing
*   **Filter by Status:** View todos by "All", "Active", or "Completed" status.
*   **Active Filter Indicator:** The currently selected filter is visually highlighted.

### 💾 Local Persistence
*   **Automatic Saving:** All todos are automatically saved to the device's local storage.
*   **State Restoration:** The todo list persists across app restarts.

### 📊 Summarization
*   **Item Counter:** A dynamic counter in the footer displays the number of active (incomplete) items.

## 🏗️ Architecture & Implementation

The app is built using modern Android development practices to ensure a robust, maintainable, and lifecycle-aware codebase.

### **MVVM (Model-View-ViewModel)**
The project follows the MVVM architectural pattern to separate concerns:
*   **Model:** Represents the data and business logic (`Todo.java`).
*   **View:** Observes the ViewModel and renders the UI (`MainActivity.java`, `TodoAdapter.java`).
*   **ViewModel:** Holds and processes UI-related data, survives configuration changes, and exposes data streams via `LiveData` (`TodoViewModel.java`).

### **Key Components**
*   **`MainActivity.java`:** The single activity that hosts the entire UI. It observes `LiveData` from the `ViewModel` and updates the views accordingly.
*   **`TodoViewModel.java`:** The core logic hub. It manages the list of todos, handles all CRUD operations, applies filters, and interacts with `SharedPreferences` for data persistence.
*   **`TodoAdapter.java`:** A `RecyclerView.Adapter` that efficiently binds the list of `Todo` objects to their corresponding views in the scrollable list.
*   **Data Persistence:** Uses `SharedPreferences` to store the todo list as a JSON string, leveraging the `Gson` library for serialization.

## 🎨 UI/UX Features

The UI is a pixel-perfect recreation of the original TodoMVC design, enhanced with Material Design components:

*   **Expandable Floating Action Button (FAB):** The filter options ("All", "Active", "Completed") are contained within a modern, animated expandable FAB menu.
*   **Conditional FAB Visibility:**
    *   The main filter FAB only appears when at least one todo item exists.
    *   The "Clear Completed" FAB only appears when the "Completed" filter is active and there is at least one completed item.
*   **Material Design Aesthetics:** Uses standard Material Components like `FloatingActionButton`, `RecyclerView`, and `EditText` with appropriate elevation, typography, and color schemes.
*   **Smooth Animations:** Includes subtle animations for FAB expansion/collapse and view state changes.

## 🚀 How to Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    ```
2.  **Open in Android Studio:** Open the project folder in Android Studio.
3.  **Sync Gradle:** Let Android Studio sync the project and download the required dependencies.
4.  **Run:** Build and run the application on an emulator or a physical device.

## 🙏 Acknowledgments
This project is inspired by and aims to replicate the functionality of the original [TodoMVC](https://todomvc.com/) project, which helps developers showcase JavaScript frameworks by implementing the same todo application.
