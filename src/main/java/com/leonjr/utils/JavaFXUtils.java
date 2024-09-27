package com.leonjr.utils;

import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class JavaFXUtils {

    /**
     * Show a modal dialog with the given node.
     * 
     * @param node      The node to show in the dialog.
     * @param width     The width of the dialog.
     * @param height    The height of the dialog.
     * @param modalPane The modal pane to show the dialog.
     * @param onClose   The action to run when the dialog is closed.
     */
    public static void showModal(Node node, double width, double height, ModalPane modalPane, Runnable onClose,
            boolean showDetachButton) {
        Card card = new Card();
        card.setPrefWidth(width);
        card.setPrefHeight(height);
        card.setMaxWidth(width);
        card.setMaxHeight(height);
        card.getStyleClass().add(Styles.ELEVATED_2);
        BorderPane borderPane = new BorderPane();
        Button closeButton = new Button();
        closeButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
        closeButton.setOnAction(event -> {
            modalPane.hide();
            if (onClose != null) {
                onClose.run();
            }
        });
        FontIcon closeIcon = new FontIcon("mdi2c-close");
        closeIcon.setIconSize(24);
        closeButton.setGraphic(closeIcon);
        Button detachButton = null;
        if (showDetachButton) {
            detachButton = new Button();
            detachButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
            detachButton.setOnAction(event -> {
                modalPane.hide();
                showDialog(node);
            });
            FontIcon detachIcon = new FontIcon("mdi2d-dock-window");
            detachIcon.setIconSize(24);
            detachButton.setGraphic(detachIcon);
            Tooltip tooltip = new Tooltip("Abrir em nova janela");
            tooltip.setHideDelay(Duration.seconds(1));
            detachButton.setTooltip(tooltip);
        }
        HBox buttonBox = new HBox();
        if (showDetachButton && detachButton != null) {
            buttonBox.getChildren().add(detachButton);
        }
        buttonBox.getChildren().add(closeButton);
        buttonBox.setSpacing(8);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        borderPane.setTop(buttonBox);
        borderPane.setCenter(node);
        card.setBody(borderPane);
        modalPane.show(card);
    }

    /**
     * Show a dialog with the given node.
     * 
     * @param node The node to show in the dialog.
     */
    public static void showDialog(Node node) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(node);
        dialog.setResizable(true);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
        dialog.show();
    }

    /**
     * Create a custom button with an icon.
     * 
     * @param text    The text of the button.
     * @param tooltip The tooltip of the button.
     * @param icon    The icon of the button.
     * @param action  The action to run when the button is clicked.
     * @return The custom button.
     */
    public static Button createCustomButtonIcon(String text, String tooltip, String icon, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().addAll(Styles.INTERACTIVE, Styles.BUTTON_ICON);
        button.setOnAction(event -> action.run());
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setIconSize(24);
        button.setGraphic(fontIcon);
        Tooltip toolTip = new Tooltip(tooltip);
        toolTip.setHideDelay(Duration.seconds(1));
        button.setTooltip(toolTip);
        return button;
    }

    /**
     * Show a notification with the given title and message.
     * 
     * @param title     The title of the notification.
     * @param message   The message of the notification.
     * @param accent    The accent of the notification.
     * @param container The container to show the notification.
     */
    public static void showNotification(String title, String message, String accent, Pane container) {
        var fontIcon = (accent == Styles.WARNING ? new FontIcon("mdi2a-alert-circle")
                : accent == Styles.SUCCESS ? new FontIcon("mdi2c-check") : accent == Styles.DANGER?new FontIcon("mdi2a-alert")  :  new FontIcon("mdi2i-information"));
        Notification msg = new Notification(title + "\n" + message, fontIcon);
        msg.getStyleClass().addAll(
                accent, Styles.ELEVATED_1);
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);
        alignNotification(msg, container);
        msg.setOnClose(e -> {
            var out = Animations.slideOutUp(msg, Duration.millis(250));
            out.setOnFinished(f -> container.getChildren().remove(msg));
            out.playFromStart();
        });
        var in = Animations.slideInDown(msg, Duration.millis(250));
        if (!container.getChildren().contains(msg)) {
            container.getChildren().add(msg);
        }
        in.playFromStart();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (container.getChildren().contains(msg))
                container.getChildren().remove(msg);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private static void alignNotification(Notification notification, Pane container) {
        if (container instanceof StackPane) {
            StackPane.setAlignment(notification, Pos.TOP_RIGHT);
            StackPane.setMargin(notification, new Insets(10, 10, 0, 0));
        } else if (container instanceof BorderPane) {
            BorderPane.setAlignment(notification, Pos.TOP_RIGHT);
            BorderPane.setMargin(notification, new Insets(10, 10, 0, 0));
        } else if (container instanceof HBox) {
            HBox.setMargin(notification, new Insets(10, 10, 0, 0));
        } else if (container instanceof VBox) {
            VBox.setMargin(notification, new Insets(10, 10, 0, 0));
        }
    }

}
