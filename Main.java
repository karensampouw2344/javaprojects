package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Student;
import util.Connect;

public class Main extends Application {

    private VBox vbox;
    private TableView<Student> table;
    private TextField nameField, ageField, addressField, ipkField;
    private Button addButton, updateButton, deleteButton;

    private Connect connect = Connect.getInstance();
    private ArrayList<Student> students = new ArrayList<>();
    private Integer tempId = null;

    @Override
    public void start(Stage primaryStage) {
        initComponents();
        setupEventHandlers();
        refreshTable();

        Scene scene = new Scene(vbox, 480, 640);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Management");
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);

        // Table Initialization
        table = new TableView<>();

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Student, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Student, Double> ipkColumn = new TableColumn<>("IPK");
        ipkColumn.setCellValueFactory(new PropertyValueFactory<>("ipk"));

        table.getColumns().addAll(nameColumn, ageColumn, addressColumn, ipkColumn);

        // Input Fields
        nameField = new TextField();
        nameField.setPromptText("Name");

        ageField = new TextField();
        ageField.setPromptText("Age");

        addressField = new TextField();
        addressField.setPromptText("Address");

        ipkField = new TextField();
        ipkField.setPromptText("IPK");

        FlowPane inputPane = new FlowPane(10, 10, nameField, ageField, addressField, ipkField);
        inputPane.setAlignment(Pos.CENTER);

        // Buttons
        addButton = new Button("Add");
        updateButton = new Button("Update");
        deleteButton = new Button("Delete");

        FlowPane buttonPane = new FlowPane(10, 10, addButton, updateButton, deleteButton);
        buttonPane.setAlignment(Pos.CENTER);

        // Add components to VBox
        vbox.getChildren().addAll(table, inputPane, buttonPane);
    }

    private void setupEventHandlers() {
        addButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String address = addressField.getText();
                double ipk = Double.parseDouble(ipkField.getText());

                addData(name, age, address, ipk);
                refreshTable();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter valid values for Age and IPK.");
            }
        });

        table.setOnMouseClicked(event -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tempId = selected.getId();
                nameField.setText(selected.getName());
                ageField.setText(String.valueOf(selected.getAge()));
                addressField.setText(selected.getAddress());
                ipkField.setText(String.valueOf(selected.getIpk()));
            }
        });

        updateButton.setOnAction(event -> {
            if (tempId != null) {
                try {
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    String address = addressField.getText();
                    double ipk = Double.parseDouble(ipkField.getText());

                    updateData(tempId, name, age, address, ipk);
                    refreshTable();
                    clearFields();
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid values for Age and IPK.");
                }
            } else {
                showAlert("No Selection", "Please select a row to update.");
            }
        });

        deleteButton.setOnAction(event -> {
            if (tempId != null) {
                deleteData(tempId);
                refreshTable();
                clearFields();
            } else {
                showAlert("No Selection", "Please select a row to delete.");
            }
        });
    }

    private void addData(String name, int age, String address, double ipk) {
        String query = "INSERT INTO student (name, age, address, ipk) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, address);
            ps.setDouble(4, ipk);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateData(int id, String name, int age, String address, double ipk) {
        String query = "UPDATE student SET name = ?, age = ?, address = ?, ipk = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, address);
            ps.setDouble(4, ipk);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteData(int id) {
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        students.clear();
        String query = "SELECT * FROM student";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                int id = connect.rs.getInt("id");
                String name = connect.rs.getString("name");
                int age = connect.rs.getInt("age");
                String address = connect.rs.getString("address");
                double ipk = connect.rs.getDouble("ipk");
                students.add(new Student(id, name, age, address, ipk));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<Student> studentObs = FXCollections.observableArrayList(students);
        table.setItems(studentObs);
    }

    private void clearFields() {
        nameField.clear();
        ageField.clear();
        addressField.clear();
        ipkField.clear();
        tempId = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
