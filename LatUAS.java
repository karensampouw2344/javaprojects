package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LatUAS extends Application {

    private ComboBox<String> categoryComboBox;
    private ComboBox<String> barangComboBox; // Added a ComboBox for Kd_Barang
    private TextField namaTextField;
    private TextField hargaLelangTextField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Data Input Form");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Initialize the ComboBoxes and TextFields
        categoryComboBox = new ComboBox<>();
        barangComboBox = new ComboBox<>();
        namaTextField = new TextField();
        hargaLelangTextField = new TextField();

        // Populate the ComboBoxes with some example data
        populateCategoryComboBox();
        populateBarangComboBox();

        // Add controls to the grid
        grid.add(new Label("Kategori:"), 0, 0);
        grid.add(categoryComboBox, 1, 0);
        grid.add(new Label("Kode Barang:"), 0, 1);
        grid.add(barangComboBox, 1, 1); // Use the new ComboBox for Kd_Barang
        grid.add(new Label("Nama:"), 0, 2);
        grid.add(namaTextField, 1, 2);
        grid.add(new Label("Harga Lelang:"), 0, 3);
        grid.add(hargaLelangTextField, 1, 3);

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        // Submit Button Action
        submitButton.setOnAction(event -> {
            // Check if ComboBoxes are selected and fields are not empty
            if (categoryComboBox.getValue() == null || barangComboBox.getValue() == null || 
                namaTextField.getText().isEmpty() || hargaLelangTextField.getText().isEmpty()) {
                System.out.println("Please fill in all fields.");
                return;
            }

            // Insert logic or data processing (you can replace this with actual logic)
            System.out.println("Data submitted:");
            System.out.println("Kategori: " + categoryComboBox.getValue());
            System.out.println("Kode Barang: " + barangComboBox.getValue());
            System.out.println("Nama: " + namaTextField.getText());
            System.out.println("Harga Lelang: " + hargaLelangTextField.getText());
        });

        // Back Button Action
        backButton.setOnAction(event -> primaryStage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(submitButton, backButton);
        grid.add(buttonBox, 1, 4);

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Method to populate categoryComboBox with sample data
    private void populateCategoryComboBox() {
        ObservableList<String> categoryList = FXCollections.observableArrayList("Category 1", "Category 2", "Category 3");
        categoryComboBox.setItems(categoryList);
    }

    // Method to populate barangComboBox with sample data
    private void populateBarangComboBox() {
        ObservableList<String> barangList = FXCollections.observableArrayList("Barang 1", "Barang 2", "Barang 3");
        barangComboBox.setItems(barangList);
    }
}

