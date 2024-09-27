package com.leonjr.controllers.components;

import com.leonjr.confs.UserConfs;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class MainColorPicker {
    public static void startColorPicker(final ColorPicker mainColorPicker) {
        mainColorPicker.setValue(Color.RED);
        UserConfs.LINE_COLOR = mainColorPicker.getValue();
        Tooltip tooltip = new Tooltip("Cor da linha");
        mainColorPicker.setTooltip(tooltip);
        mainColorPicker.setOnAction(event -> {
            UserConfs.LINE_COLOR = mainColorPicker.getValue();
        });
        UserConfs.CANVAS_COLOR = Color.WHITE;
    }
}
