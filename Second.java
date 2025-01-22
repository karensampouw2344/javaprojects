package second;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Second extends Application {

    private int colorIndex = 0;
    private final String[] colors = {"#ADD8E6", "#FFB6C1", "#E6E6FA"}; // Light Blue, Light Pink, Lavender

    @Override
    public void start(Stage primaryStage) {
        // Create a button
        Button button = new Button("Hover or Click Me");

        // Create a label to display the event information
        Label label = new Label("No event triggered yet");

        // Create a TextField
        TextField textField = new TextField();
        textField.setPromptText("Click and Release to Change Color");

        // Handle mouse released event for the TextField
        textField.setOnMouseReleased(event -> {
            // Change the background color of the TextField
            textField.setStyle("-fx-background-color: " + colors[colorIndex] + ";");

            // Cycle through the colors
            colorIndex = (colorIndex + 1) % colors.length;  // This will loop through the colors
        });

        // Handle mouse clicked event for the Button
        button.setOnMouseClicked(event -> {
            label.setText("Mouse clicked at X: " + event.getSceneX() + ", Y: " + event.getSceneY());
        });

        // Handle mouse pressed event for the Button
        button.setOnMousePressed(event -> {
            label.setText("Mouse pressed at X: " + event.getSceneX() + ", Y: " + event.getSceneY());
        });

        // Handle mouse released event for the Button
        button.setOnMouseReleased(event -> {
            label.setText("Mouse released at X: " + event.getSceneX() + ", Y: " + event.getSceneY());
        });

        // Handle mouse entered event for the Button
        button.setOnMouseEntered(event -> {
            label.setText("Mouse entered button");
        });

        // Handle mouse exited event for the Button
        button.setOnMouseExited(event -> {
            label.setText("Mouse exited button");
        });

        // Create a layout and add the button, label, and text field
        StackPane root = new StackPane();
        root.getChildren().addAll(button, label, textField);

        // Center the label at the bottom of the layout
        StackPane.setAlignment(label, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setAlignment(textField, javafx.geometry.Pos.TOP_CENTER);  // Position TextField at the top

        // Set up the scene
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("JavaFX Mouse Events and TextField Color Change");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
