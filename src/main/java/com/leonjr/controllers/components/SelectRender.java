package com.leonjr.controllers.components;

import java.util.List;

import com.leonjr.confs.UserConfs;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class SelectRender {
    public static List<RadioButton> createSelectRenderComponent() {
        var group = new ToggleGroup();
        var radio1 = new RadioButton("Liang-barsky");
        radio1.setToggleGroup(group);
        radio1.setSelected(true);
        radio1.setBackground(null);
        var radio2 = new RadioButton("Cohen-Sutherland");
        radio2.setToggleGroup(group);
        radio2.setBackground(null);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (radio1.isSelected()) {
                radio2.setSelected(false);
            } else {
                radio1.setSelected(false);
            }
        });
        radio1.setOnAction(event -> {
            if (radio1.isSelected()) {
                UserConfs.COHEN_SUTHERLAND = false;
            }
        });
        radio2.setOnAction(event -> {
            if (radio2.isSelected()) {
                UserConfs.COHEN_SUTHERLAND = true;
            }
        });
        return List.of(radio1, radio2);
    }
}
