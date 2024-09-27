package com.leonjr;

import java.io.IOException;

import com.leonjr.controllers.PrimaryController;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        FXMLLoader loader = loadFXML("primary");
        var screenSize = getComputerScreenSize();
        scene = new Scene(loader.load(), screenSize[0], screenSize[1]);
        scene.setFill(Color.TRANSPARENT);
        PrimaryController controller = loader.getController();
        controller.setStage(stage);
        controller.setLastWidth(screenSize[0] / 2);
        controller.setLastHeight(screenSize[1] / 2);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        controller.setLastX((primaryScreenBounds.getMaxX() / 2) - screenSize[0] / 4);
        controller.setLastY((primaryScreenBounds.getMaxY() / 2) - screenSize[1] / 4);
        stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Computer graphics pratical assignment - Leon Junio");
        stage.getIcons().add(loadImageAsset("logo.jpeg"));
        stage.setResizable(true);
        stage.show();
        controller.startKeyBindings();
    }

    /**
     * Get the size of the computer screen.
     * Minimum size is 800x600.
     * 
     * @return An array with the width and height of the screen.
     */
    static double[] getComputerScreenSize() {
        double[] screenSize = new double[2];
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenSize[0] = screenBounds.getWidth();
        screenSize[1] = screenBounds.getHeight();
        if (screenSize[0] < 800) {
            screenSize[0] = 800;
        }
        if (screenSize[1] < 600) {
            screenSize[1] = 600;
        }
        return screenSize;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml).load());
    }

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader;
    }

    public static Image loadImageAsset(String assetName) {
        return new Image(MainApp.class.getResourceAsStream("/assets/" + assetName));
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}
