import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox mainPane = new VBox();
        ControlPanel controlPanel = new ControlPanel();
        mainPane.getChildren().add(controlPanel);
        mainPane.setPrefWidth(800);
        mainPane.setPrefHeight(800);

        mainPane.setPadding(new Insets(0));
        mainPane.setSpacing(0);
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        CanvasPanel canvasPanel = new CanvasPanel(mainPane.getWidth(), 800 - controlPanel.getHeight());
        canvasPanel.widthProperty().bind(controlPanel.widthProperty());
        canvasPanel.heightProperty().bind(mainPane.heightProperty().subtract(controlPanel.heightProperty()));
        controlPanel.setActionHandler(canvasPanel);
        mainPane.getChildren().add(canvasPanel);
    }
}
