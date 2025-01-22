package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
public class Main extends Application {


	public Main() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label label = new Label("Click anywhere!");

		Circle circle = new Circle (50, Color.LIGHTBLUE);
		circle.setStroke(Color.DARKBLUE);

		circle.setOnMouseClicked(event -> {
			String message = String.format("Circle clicked at (%.2f, %.2f)", event.getX(), event.getY());
			label.setText(message);
		});



		StackPane root = new StackPane();
		root.getChildren().addAll(circle, label);

		root.setOnMouseClicked(event -> handleMouseClick(event, label));
		Scene scene = new Scene(root, 400,400);
		primaryStage.setTitle("JavaFX MouseEvent Demo");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void handleMouseClick(MouseEvent event, Label label) {
		double x = event.getSceneX();
		double y = event.getSceneY();

		String message = String.format("Mouse clicked at (%.2f, %.2f)", x, y);
		label.setText(message);
	}

	public static void main(String[] args) {
		launch(args);
	}
}


