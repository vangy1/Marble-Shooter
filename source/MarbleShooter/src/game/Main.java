package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MarbleShooterPane marbleShooterPane = new MarbleShooterPane();

        primaryStage.setTitle("Marble shooter");
        primaryStage.setScene(new Scene(marbleShooterPane, MarbleShooterPane.PANE_WIDTH, MarbleShooterPane.PANE_HEIGHT));
        primaryStage.setMaxWidth(MarbleShooterPane.PANE_WIDTH);
        primaryStage.setMinWidth(MarbleShooterPane.PANE_WIDTH);
        primaryStage.setMaxHeight(MarbleShooterPane.PANE_HEIGHT + 20);
        primaryStage.setMinHeight(MarbleShooterPane.PANE_HEIGHT + 20);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

