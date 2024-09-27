package com.leonjr.controllers.components;

import java.util.List;

import com.leonjr.confs.UserConfs;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class LineRasterPicker {
    public static List<Node> createLineRasterPicker() {
        var group = new ToggleGroup();
        var radio1 = new RadioButton("DDA");
        radio1.setToggleGroup(group);
        radio1.setSelected(true);
        radio1.setBackground(null);
        var radio2 = new RadioButton("Bresenham");
        radio2.setToggleGroup(group);
        radio2.setBackground(null);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (radio1.isSelected()) {
                radio2.setSelected(false);
            } else {
                radio1.setSelected(false);
            }
        });
        var separator = new javafx.scene.control.Separator(Orientation.HORIZONTAL);
        var label = new javafx.scene.control.Label("Rasterização de linhas");
        radio1.setOnAction(event -> {
            if (radio1.isSelected()) {
                UserConfs.DDA_RASTER = true;
            }
        });
        radio2.setOnAction(event -> {
            if (radio2.isSelected()) {
                UserConfs.DDA_RASTER = false;
            }
        });
        return List.of(label, radio1, radio2, separator);
    }
}
