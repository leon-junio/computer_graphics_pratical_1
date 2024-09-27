package com.leonjr.controllers.components;

import com.leonjr.controllers.types.ActionButtonsFunctions;
import com.leonjr.utils.JavaFXUtils;

import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;

public class ActionButtonsBar {
    public static HBox createActionButtonsBar(ColorPicker mainColorPicker, ActionButtonsFunctions functions) {
        HBox actionButtonsBar = new HBox();
        actionButtonsBar.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        actionButtonsBar.setSpacing(10);
        actionButtonsBar.setAlignment(javafx.geometry.Pos.CENTER);
        var selectButton = JavaFXUtils.createCustomButtonIcon("", "Seleção", "mdi2c-cursor-default",
                functions.select());
        var translateButton = JavaFXUtils.createCustomButtonIcon("", "Translação", "mdi2a-arrow-all",
                functions.translate());
        var rotateButton = JavaFXUtils.createCustomButtonIcon("", "Rotação", "mdi2f-format-rotate-90",
                functions.rotate());
        var scaleButton = JavaFXUtils.createCustomButtonIcon("", "Escala", "mdi2r-ruler", functions.scale());
        var reflectButton = JavaFXUtils.createCustomButtonIcon("", "Reflexão", "mdi2m-mirror", functions.reflect());
        var deleteButton = JavaFXUtils.createCustomButtonIcon("", "Deletar", "mdi2d-delete", functions.delete());
        actionButtonsBar.setDisable(false);
        actionButtonsBar.getChildren().addAll(mainColorPicker, selectButton, translateButton, rotateButton, scaleButton,
                reflectButton, deleteButton);
        return actionButtonsBar;
    }
}
