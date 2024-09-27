package com.leonjr.controllers;

import com.leonjr.confs.UserConfs;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Data;

@Data
public class PreferencesController {

    @FXML
    private BorderPane preferencesContainer;

    private ColorPicker canvasColorPicker;

    @FXML
    private void initialize() {
        preferencesContainer.setTop(createCanvasColorPicker());
        preferencesContainer.setCenter(createWidthAndHeightFields());
    }

    private VBox createWidthAndHeightFields() {
        VBox widthAndHeightFields = new VBox();
        widthAndHeightFields.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        widthAndHeightFields.setSpacing(10);
        widthAndHeightFields.setAlignment(javafx.geometry.Pos.CENTER);
        var widthField = new javafx.scene.control.Spinner<Integer>(100, 10000, UserConfs.CANVAS_WIDTH);
        widthField.setEditable(true);
        widthField.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                widthField.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                widthField.getEditor().setText(oldValue);
            }
        });
        var heightField = new javafx.scene.control.Spinner<Integer>(100, 10000, UserConfs.CANVAS_HEIGHT);
        heightField.setEditable(true);
        heightField.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                heightField.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                heightField.getEditor().setText(oldValue);
            }
        });
        var widthLabel = new Label("Definir largura do canvas:");
        var heightLabel = new Label("Definir altura do canvas:");
        widthField.valueProperty().addListener((obs, oldValue, newValue) -> {
            UserConfs.CANVAS_WIDTH = newValue;
        });
        heightField.valueProperty().addListener((obs, oldValue, newValue) -> {
            UserConfs.CANVAS_HEIGHT = newValue;
        });
        widthAndHeightFields.getChildren().addAll(widthLabel, widthField, heightLabel, heightField);
        return widthAndHeightFields;
    }

    private VBox createCanvasColorPicker() {
        canvasColorPicker = new ColorPicker();
        canvasColorPicker.setValue(UserConfs.CANVAS_COLOR);
        var label = new Label("Definir cor do fundo do quadro do canvas:");
        VBox canvasColorPickerContainer = new VBox();
        canvasColorPickerContainer.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        canvasColorPickerContainer.setSpacing(10);
        canvasColorPickerContainer.setAlignment(javafx.geometry.Pos.CENTER);
        canvasColorPickerContainer.getChildren().addAll(label, canvasColorPicker);
        canvasColorPicker.setOnAction(event -> {
            UserConfs.CANVAS_COLOR = canvasColorPicker.getValue();
        });
        return canvasColorPickerContainer;
    }
}
