package com.leonjr.controllers.components;

import java.util.List;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Spinner;

public class NumeralParamsBox {
    public static List<Node> createNumeralParamsBox(
            final Spinner<Integer> angleSpinner,
            final Spinner<Integer> scaleSpinner) {
        var separator2 = new javafx.scene.control.Separator(Orientation.HORIZONTAL);
        var label2 = new javafx.scene.control.Label("Configurações");
        var labelAng = new javafx.scene.control.Label("Ângulo");
        angleSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                angleSpinner.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                angleSpinner.getEditor().setText(oldValue);
            }
        });
        angleSpinner.setEditable(true);
        var labelScale = new javafx.scene.control.Label("Escala");
        scaleSpinner.setEditable(true);
        scaleSpinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                scaleSpinner.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                scaleSpinner.getEditor().setText(oldValue);
            }
        });
        return List.of(label2, labelAng, angleSpinner, labelScale, scaleSpinner, separator2);
    }
}
