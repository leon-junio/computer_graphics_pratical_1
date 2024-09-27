package com.leonjr.controllers.components;

import java.util.List;

import com.leonjr.confs.UserConfs;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ReflectionTypeSelector {
    public static List<Node> createReflectionTypeSelector() {
        var separator3 = new javafx.scene.control.Separator(Orientation.HORIZONTAL);
        var group2 = new ToggleGroup();
        var labelReflect = new javafx.scene.control.Label("Reflexão");
        var reflectionx = new RadioButton("X");
        reflectionx.setToggleGroup(group2);
        reflectionx.setSelected(true);
        reflectionx.setBackground(null);
        var reflectiony = new RadioButton("Y");
        reflectiony.setToggleGroup(group2);
        reflectiony.setBackground(null);
        var reflectionxy = new RadioButton("XY");
        reflectionxy.setToggleGroup(group2);
        reflectionxy.setBackground(null);
        group2.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (reflectionx.isSelected()) {
                reflectiony.setSelected(false);
                reflectionxy.setSelected(false);
            } else if (reflectiony.isSelected()) {
                reflectionx.setSelected(false);
                reflectionxy.setSelected(false);
            } else {
                reflectionx.setSelected(false);
                reflectiony.setSelected(false);
            }
        });
        reflectionx.setOnAction(event -> {
            UserConfs.REFLECTION_TYPE = 1;
        });
        reflectiony.setOnAction(event -> {
            UserConfs.REFLECTION_TYPE = 2;
        });
        reflectionxy.setOnAction(event -> {
            UserConfs.REFLECTION_TYPE = 3;
        });
        return List.of(labelReflect, reflectionx, reflectiony, reflectionxy, separator3);
    }
}
