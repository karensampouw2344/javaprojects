package main;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Student;

import java.sql.*;

public class Main extends Application {

    private TableView<Student> tableView;
    private TextField nameField, ageField, gradeField;
    private ObservableList<Student> studentList;
    private Connection connection;

    @Override
    public void start(Stage stage) {
        try {
            // Establishing the database connection
            connection = DriverManager.getConnection("jdbc:yourdatabaseurl", "username", "password");

            studentList = FXCollections.observableArrayList(getAllStudents());

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        stage.setTitle("Student Management");

        // Labels and TextFields
        Label nameLabel = new Label("Name");
        nameField = new TextField();
        Label ageLabel = new Label("Age");
        ageField = new TextField();
        Label gradeLabel = new Label("Grade");
        gradeField = new TextField();

        // Buttons
        Button addButton = new Button("Add Student");
        Button updateButton = new Button("Update Student");
        Button deleteButton = new Button("Delete Student");

        // Set actions for buttons
        addButton.setOnAction(e -> addStudent());
        updateButton.setOnAction(e -> updateStudent());
        deleteButton.setOnAction(e -> deleteStudent());

        // TableView setup
        tableView = new TableView<>();

        // Creating columns for TableView
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        TableColumn<Student, String> gradeColumn = new TableColumn<>("Grade");

        // Using PropertyValueFactory to bind columns with Student properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Adding columns to TableView
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(ageColumn);
        tableView.getColumns().add(gradeColumn);
        tableView.setItems(studentList);

        // Layout setup
        VBox formLayout = new VBox(10, nameLabel, nameField, ageLabel, ageField, gradeLabel, gradeField, addButton, updateButton, deleteButton);
        formLayout.setPadding(new Insets(20));

        HBox root = new HBox(20, formLayout, tableView);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void addStudent() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String grade = gradeField.getText();

        if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            String sql = "INSERT INTO students (name, age, grade) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, grade);
            stmt.executeUpdate();

            // Adding the new student to the list (retrieving fresh list from the DB)
            studentList.setAll(getAllStudents());
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully!");
        } catch (NumberFormatException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input or database error!");
        }
    }

    private void updateStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            String name = nameField.getText();
            String ageText = ageField.getText();
            String grade = gradeField.getText();

            if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                String sql = "UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setString(3, grade);
                stmt.setInt(4, selectedStudent.getId());
                stmt.executeUpdate();

                // Refresh the table data
                studentList.setAll(getAllStudents());
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully!");
            } catch (NumberFormatException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid input or database error!");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a student to update.");
        }
    }

    private void deleteStudent() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                String sql = "DELETE FROM students WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, selectedStudent.getId());
                stmt.executeUpdate();

                // Refresh the table data
                studentList.setAll(getAllStudents());
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Database error!");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a student to delete.");
        }
    }

    private ObservableList<Student> getAllStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM students";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("grade")));
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database error!");
        }
        return students;
    }

    private void clearFields() {
        nameField.clear();
        ageField.clear();
        gradeField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

