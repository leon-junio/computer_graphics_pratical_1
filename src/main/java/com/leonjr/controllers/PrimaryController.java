package com.leonjr.controllers;

import java.net.URI;
import java.util.Stack;

import com.leonjr.MainApp;
import com.leonjr.confs.UserConfs;
import com.leonjr.controllers.components.ActionButtonsBar;
import com.leonjr.controllers.components.LineRasterPicker;
import com.leonjr.controllers.components.MainColorPicker;
import com.leonjr.controllers.components.NumeralParamsBox;
import com.leonjr.controllers.components.ReflectionTypeSelector;
import com.leonjr.controllers.components.SelectRender;
import com.leonjr.controllers.types.ActionButtonsFunctions;
import com.leonjr.shapes.Circle;
import com.leonjr.shapes.Line;
import com.leonjr.shapes.Shape;
import com.leonjr.shapes.ShapeType;
import com.leonjr.utils.CanvasRenderer;
import com.leonjr.utils.CohenSutherland;
import com.leonjr.utils.JavaFXUtils;
import com.leonjr.utils.LiangBarsky;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class PrimaryController {

    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isMaximized = true;
    private double lastWidth = 0;
    private double lastHeight = 0;
    private double lastX = 0;
    private double lastY = 0;
    private double scaleFactor = 1.0;

    @FXML
    private BorderPane rootPane;
    @FXML
    private StackPane primaryContainer;
    @FXML
    private ToolBar topBar;
    @FXML
    private ToolBar leftBar;
    @FXML
    private ToolBar componentsBar;
    @FXML
    private BorderPane canvasWrapper;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button resetCenterPosition;
    @FXML
    private ToggleGroup renderModeGroup;
    @FXML
    private Button lineButton;
    @FXML
    private Button circleButton;
    @FXML
    private ToggleButton clipButton;

    private Canvas canvas;
    private GraphicsContext gc;
    private ColorPicker mainColorPicker;
    private Spinner<Integer> angleSpinner;
    private Spinner<Integer> scaleSpinner;
    private CanvasRenderer renderer;
    private boolean[][] rasterCanvas;
    private Color[][] canvasColors;
    private boolean isDrawing = false;
    private ShapeType shapeTypeSelected = null;
    private double[] lineStart = new double[2];
    private double[] lineEnd = new double[2];
    private double[] circleStart = new double[2];
    private double[] circleEnd = new double[2];
    private int[] clipAreaFirstClick = new int[2];
    private int[] clipAreaSecondClick = new int[2];

    private final ModalPane modalPane = new ModalPane();

    private Stack<Shape> shapes;

    /**
     * Functions to be called when the action buttons are clicked
     */
    private final ActionButtonsFunctions functions = new ActionButtonsFunctions(
            () -> {
                resetActionButton();
            },
            () -> {
                enableLeftBar(false);
                enableTopBar(false);
                MainApp.getScene().setCursor(Cursor.MOVE);
                canvas.setOnMouseReleased(e -> {
                    try {
                        if (shapes.peek() != null) {
                            var currentShape = shapes.peek();
                            double deltaX = e.getX() - currentShape.getPosition()[1];
                            double deltaY = e.getY() - currentShape.getPosition()[0];
                            currentShape.translate(deltaY, deltaX);
                            updateCanvas();
                        } else {
                            JavaFXUtils.showNotification("Nenhuma figura para mover", "Não há figuras para mover",
                                    Styles.WARNING, primaryContainer);
                        }
                    } catch (Exception ex) {
                        JavaFXUtils.showNotification("Falha ao transladar",
                                "Localização inválida, a figura transborda o canvas",
                                Styles.WARNING, primaryContainer);
                    }
                });
            },
            () -> {
                resetActionButton();
                try {
                    if (shapes.peek() != null) {
                        var currentShape = shapes.peek();
                        currentShape.rotate((int) angleSpinner.getValue());
                        updateCanvas();
                    } else {
                        JavaFXUtils.showNotification("Nenhuma figura para rotacionar", "Não há figuras para rotacionar",
                                Styles.WARNING, primaryContainer);
                    }
                } catch (Exception e) {
                    JavaFXUtils.showNotification("Falha ao rotacionar",
                            "Localização inválida, a figura transborda o canvas",
                            Styles.WARNING, primaryContainer);
                }
            },
            () -> {
                resetActionButton();
                try {
                    if (shapes.peek() != null) {
                        var currentShape = shapes.peek();
                        currentShape.scale((int) scaleSpinner.getValue(), (int) scaleSpinner.getValue());
                        updateCanvas();
                    } else {
                        JavaFXUtils.showNotification("Nenhuma figura para escalar", "Não há figuras para escalar",
                                Styles.WARNING, primaryContainer);
                    }
                } catch (Exception e) {
                    JavaFXUtils.showNotification("Falha ao escalar",
                            "Localização inválida, a figura transborda o canvas",
                            Styles.WARNING, primaryContainer);
                }
            },
            () -> {
                resetActionButton();
                try {
                    if (shapes.peek() != null) {
                        var currentShape = shapes.peek();
                        switch (UserConfs.REFLECTION_TYPE) {
                            case 1:
                                currentShape.reflectX();
                                break;
                            case 2:
                                currentShape.reflectY();
                                break;
                            case 3:
                                currentShape.reflectXY();
                                break;
                            default:
                                break;
                        }
                        updateCanvas();
                    } else {
                        JavaFXUtils.showNotification("Nenhuma figura para refletir", "Não há figuras para refletir",
                                Styles.WARNING, primaryContainer);
                    }
                } catch (Exception e) {
                    JavaFXUtils.showNotification("Falha ao refletir",
                            "Erro ao refletir figura no canvas",
                            Styles.WARNING, primaryContainer);
                }
            },
            () -> {
                resetActionButton();
                if (!shapes.isEmpty()) {
                    shapes.pop();
                    updateCanvas();
                } else {
                    JavaFXUtils.showNotification("Nenhuma figura para remover", "Não há figuras para remover",
                            Styles.WARNING, primaryContainer);
                }
            });

    private void resetActionButton() {
        MainApp.getScene().setCursor(Cursor.DEFAULT);
        canvas.setOnMouseReleased(e -> {
        });
        enableLeftBar(true);
        enableTopBar(true);
    }

    /**
     * The initialize method is called after all @FXML annotated fields are injected
     */
    @FXML
    private void initialize() {
        shapes = new Stack<>();
        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        startCustomComponents();
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> handleZoom(event, canvas));
        canvas = new Canvas(UserConfs.CANVAS_WIDTH, UserConfs.CANVAS_HEIGHT);
        renderer = new CanvasRenderer(canvas);
        rasterCanvas = new boolean[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
        canvasColors = new Color[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
        renderer.render(rasterCanvas, canvasColors);
        canvasWrapper.setCenter(canvas);
        canvasWrapper.setTop(componentsBar);
        BorderPane.setAlignment(componentsBar, javafx.geometry.Pos.CENTER);
        lineButton.setOnAction(event -> {
            startDrawMode(ShapeType.LINE);
        });
        circleButton.setOnAction(event -> {
            startDrawMode(ShapeType.CIRCLE);
        });
        defineCanvasMouseEvents();
    }

    private void enableLeftBar(boolean enable) {
        leftBar.setDisable(!enable);
    }

    private void enableTopBar(boolean enable) {
        topBar.setDisable(!enable);
    }

    private void enableComponentsBar(boolean enable) {
        componentsBar.setDisable(!enable);
    }

    /**
     * Start custom components (components that are not part of the FXML file)
     */
    private void startCustomComponents() {
        primaryContainer.getChildren().add(modalPane);
        componentsBar = new ToolBar();
        componentsBar.setBorder(
                new Border(
                        new BorderStroke(Color.DARKVIOLET, BorderStrokeStyle.SOLID,
                                new CornerRadii(10),
                                new BorderWidths(1))));
        componentsBar.setBackground(null);
        componentsBar.setPadding(new javafx.geometry.Insets(0, 0, 0, 20));
        mainColorPicker = new ColorPicker();
        MainColorPicker.startColorPicker(mainColorPicker);
        componentsBar.getItems().add(ActionButtonsBar.createActionButtonsBar(mainColorPicker, functions));
        componentsBar.setPrefWidth(490);
        componentsBar.setMinWidth(50);
        componentsBar.setMaxWidth(490);
        resetCenterPosition.setTooltip(new Tooltip("Centralizar a tela"));
        leftBar.getItems().addAll(SelectRender.createSelectRenderComponent());
        leftBar.getItems().addAll(LineRasterPicker.createLineRasterPicker());
        angleSpinner = new Spinner<Integer>(0, 360, 90);
        scaleSpinner = new Spinner<Integer>(0, 100, 1);
        leftBar.getItems().addAll(NumeralParamsBox.createNumeralParamsBox(angleSpinner, scaleSpinner));
        leftBar.getItems().addAll(ReflectionTypeSelector.createReflectionTypeSelector());
        leftBar.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
    }

    /**
     * Define main canvas mouse events to draw shapes
     */
    private void defineCanvasMouseEvents() {
        canvas.setOnMouseClicked(e -> {
            try {
                if (isDrawing && shapeTypeSelected == ShapeType.LINE) {
                    if (lineStart[0] == 0 && lineStart[1] == 0) {
                        lineStart[0] = e.getY();
                        lineStart[1] = e.getX();
                        rasterCanvas[(int) lineStart[0]][(int) lineStart[1]] = true;
                        canvasColors[(int) lineStart[0]][(int) lineStart[1]] = UserConfs.LINE_COLOR;
                        renderer.render(rasterCanvas, canvasColors);
                    } else {
                        try {
                            lineEnd[0] = e.getY();
                            lineEnd[1] = e.getX();
                            var line = new Line(lineStart, lineEnd, UserConfs.LINE_COLOR);
                            line.rasterize(rasterCanvas, canvasColors);
                            shapes.push(line);
                            renderer.render(rasterCanvas, canvasColors);
                            endDrawMode();
                        } catch (Exception ex) {
                            JavaFXUtils.showNotification("Posição inválida para a linha",
                                    "Erro ao desenhar a linha, a posição é inválida",
                                    Styles.WARNING, primaryContainer);
                        }
                    }
                } else if (isDrawing && shapeTypeSelected == ShapeType.CIRCLE) {
                    if (circleStart[0] == 0 && circleStart[1] == 0) {
                        circleStart[0] = e.getY();
                        circleStart[1] = e.getX();
                        rasterCanvas[(int) circleStart[0]][(int) circleStart[1]] = true;
                        canvasColors[(int) circleStart[0]][(int) circleStart[1]] = UserConfs.LINE_COLOR;
                        renderer.render(rasterCanvas, canvasColors);
                    } else {
                        try {
                            circleEnd[0] = e.getY();
                            circleEnd[1] = e.getX();
                            double radius = Math.sqrt(
                                    Math.pow(circleEnd[0] - circleStart[0], 2)
                                            + Math.pow(circleEnd[1] - circleStart[1], 2));
                            var circle = new Circle(circleStart, radius, UserConfs.LINE_COLOR);
                            circle.rasterize(rasterCanvas, canvasColors);
                            shapes.push(circle);
                            renderer.render(rasterCanvas, canvasColors);
                            endDrawMode();
                        } catch (Exception ex) {
                            JavaFXUtils.showNotification("O círculo não pode ser desenhado",
                                    "Erro ao desenhar o círculo, o raio do círculo é maior que o canvas",
                                    Styles.WARNING, primaryContainer);
                        }
                    }
                }
            } catch (Exception ex) {
                JavaFXUtils.showNotification("Erro ao desenhar", "Não foi possível desenhar a figura no canvas",
                        Styles.DANGER, primaryContainer);
            }
        });
    }

    /**
     * Start the draw mode for a specific shape
     * 
     * @param type The shape type
     */
    private void startDrawMode(ShapeType type) {
        isDrawing = true;
        shapeTypeSelected = type;
        resetClipButtonStatus();
        enableLeftBar(false);
        enableTopBar(false);
        enableComponentsBar(false);
        switch (type) {
            case LINE:
                lineStart = new double[] { 0, 0 };
                lineEnd = new double[] { 0, 0 };
                MainApp.getScene().setCursor(Cursor.CROSSHAIR);
                break;
            case CIRCLE:
                MainApp.getScene().setCursor(Cursor.CROSSHAIR);
                circleStart = new double[] { 0, 0 };
                circleEnd = new double[] { 0, 0 };
                break;
            default:
                isDrawing = false;
                break;
        }
    }

    /**
     * End draw mode and reset variables
     */
    private void endDrawMode() {
        isDrawing = false;
        circleStart = new double[] { 0, 0 };
        circleEnd = new double[] { 0, 0 };
        lineStart = new double[] { 0, 0 };
        lineEnd = new double[] { 0, 0 };
        MainApp.getScene().setCursor(Cursor.DEFAULT);
        enableComponentsBar(true);
        enableLeftBar(true);
        enableTopBar(true);
    }

    /**
     * Update the canvas render map with the current shapes
     */
    private void updateCanvasRenderMap() {
        try {
            canvas.setWidth(UserConfs.CANVAS_WIDTH);
            canvas.setHeight(UserConfs.CANVAS_HEIGHT);
            renderer.render(rasterCanvas, canvasColors);
            defineCanvasMouseEvents();
            clipButton.setSelected(false);
        } catch (Exception e) {
            JavaFXUtils.showNotification("Erro ao atualizar o canvas", "Não foi possível atualizar o canvas",
                    Styles.DANGER, primaryContainer);
        }
    }

    /**
     * Start the clip area mode (Draw a rectangle to clip the lines)
     */
    @FXML
    private void startClipArea() {
        if (clipButton.isSelected()) {
            enableLeftBar(false);
            enableComponentsBar(false);
            canvas.setOnMouseClicked((e) -> {
                gc = canvas.getGraphicsContext2D();
                if (clipAreaFirstClick[0] == 0 && clipAreaFirstClick[1] == 0) {
                    clipAreaFirstClick[0] = (int) e.getY();
                    clipAreaFirstClick[1] = (int) e.getX();
                } else {
                    clipAreaSecondClick[0] = (int) e.getY();
                    clipAreaSecondClick[1] = (int) e.getX();
                    var rasterCanvas2 = new boolean[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
                    var xMin = Math.min(clipAreaFirstClick[1], clipAreaSecondClick[1]);
                    var yMin = Math.min(clipAreaFirstClick[0], clipAreaSecondClick[0]);
                    var xMax = Math.max(clipAreaFirstClick[1], clipAreaSecondClick[1]);
                    var yMax = Math.max(clipAreaFirstClick[0], clipAreaSecondClick[0]);
                    enableLeftBar(true);
                    enableComponentsBar(true);
                    if (UserConfs.COHEN_SUTHERLAND) {
                        shapes.forEach(shape -> {
                            if (shape instanceof Line) {
                                var line = (Line) shape;
                                CohenSutherland.lineClip((int) line.getStart()[1], (int) line.getStart()[0],
                                        (int) line.getEnd()[1], (int) line.getEnd()[0], xMin, yMin, xMax, yMax,
                                        rasterCanvas2, canvasColors, line.getColor());
                            }
                        });
                    } else {
                        shapes.forEach(shape -> {
                            if (shape instanceof Line) {
                                var line = (Line) shape;
                                LiangBarsky.clipLiangBarsky((int) line.getStart()[1],
                                        (int) line.getStart()[0], (int) line.getEnd()[1], (int) line.getEnd()[0], xMin,
                                        yMin,
                                        xMax, yMax, rasterCanvas2, canvasColors, line.getColor());
                            }
                        });
                    }
                    renderer.render(rasterCanvas2, canvasColors);
                    gc.setStroke(Color.BLACK);
                    gc.strokeRect(xMin, yMin, xMax - xMin, yMax - yMin);
                    canvas.setOnMouseClicked(null);
                    defineCanvasMouseEvents();
                }
            });
        } else {
            resetClipButtonStatus();
        }
    }

    /**
     * Reset the clip button status
     */
    private void resetClipButtonStatus() {
        canvas.setOnMouseClicked(null);
        updateCanvasRenderMap();
        clipAreaFirstClick = new int[2];
        clipAreaSecondClick = new int[2];
    }

    /**
     * Update the canvas with the current shapes
     */
    private void updateCanvas() {
        rasterCanvas = new boolean[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
        canvasColors = new Color[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
        shapes.forEach(shape -> shape.rasterize(rasterCanvas, canvasColors));
        renderer.render(rasterCanvas, canvasColors);
    }

    /**
     * Handle the zoom at canvas event
     * 
     * @param event  The scroll event
     * @param canvas The canvas to be zoomed
     */
    private void handleZoom(ScrollEvent event, Canvas canvas) {
        double zoomFactor = 1.05;
        if (scaleFactor < 0.5)
            scaleFactor = 0.5;
        if (scaleFactor > 5.0)
            scaleFactor = 5.0;
        if (event.getDeltaY() > 0) {
            scaleFactor *= zoomFactor;
        } else {
            scaleFactor /= zoomFactor;
        }
        canvas.getTransforms().clear();
        Scale scale = new Scale(scaleFactor, scaleFactor, event.getX(), event.getY());
        canvas.getTransforms().add(scale);
        scrollPane.setHbarPolicy(
                scrollPane.getWidth() < canvas.getWidth() * scaleFactor ? ScrollPane.ScrollBarPolicy.ALWAYS
                        : ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(
                scrollPane.getHeight() < canvas.getHeight() * scaleFactor ? ScrollPane.ScrollBarPolicy.ALWAYS
                        : ScrollPane.ScrollBarPolicy.NEVER);
    }

    @FXML
    private void clear() {
        shapes.clear();
        rasterCanvas = new boolean[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
        canvasColors = new Color[UserConfs.CANVAS_HEIGHT][UserConfs.CANVAS_WIDTH];
        renderer.clear();
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(UserConfs.CANVAS_COLOR);
        updateCanvasRenderMap();
        resetCanvasZoom();
    }

    @FXML
    private void resetCanvasZoom() {
        scaleFactor = 1.0;
        canvas.getTransforms().clear();
    }

    @FXML
    private void minimizeWindow() {
        stage.setIconified(true);
    }

    @FXML
    private void maximizeWindow() {
        if (!isMaximized) {
            lastWidth = stage.getWidth();
            lastHeight = stage.getHeight();
            lastX = stage.getX();
            lastY = stage.getY();
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setMaxWidth(primaryScreenBounds.getWidth());
            stage.setMinWidth(primaryScreenBounds.getWidth());
            stage.setMaxHeight(primaryScreenBounds.getHeight());
            stage.setMinHeight(primaryScreenBounds.getHeight());
            isMaximized = true;
        } else {
            stage.setMaxWidth(lastWidth);
            stage.setMinWidth(lastWidth);
            stage.setMaxHeight(lastHeight);
            stage.setMinHeight(lastHeight);
            stage.setX(lastX);
            stage.setY(lastY);
            isMaximized = false;
        }
    }

    @FXML
    public void openAboutDialog() {
        var borderPane = new BorderPane();
        borderPane.setMinWidth(400);
        borderPane.setMaxWidth(400);
        borderPane.setMaxHeight(300);
        var header1 = new Tile(
                "Trabalho Prático 1 - Computação Gráfica",
                "Sistema de desenho de figuras geométricas");
        borderPane.setTop(header1);
        var text1 = new TextFlow(new Text("\n\n" +
                "Desenvolvido por: Leon Junio Martins Ferreira\n" +
                "Disciplina: Computação Gráfica\n" +
                "Professor: Rosilane \n" +
                "PUC Minas - Praça da Liberdade\n" +
                "2024/2"));
        text1.setMaxWidth(260);
        borderPane.setCenter(text1);
        JavaFXUtils.showModal(borderPane, 300, 300, modalPane, null, false);
    }

    @FXML
    public void openRepo() {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    desktop.browse(new URI("https://github.com/leon-junio/computer_graphics_pratical_1"));
                } else {
                    throw new UnsupportedOperationException("Desktop API is not supported on the current platform");
                }
            } else {
                throw new UnsupportedOperationException("Desktop API is not supported on the current platform");
            }
        } catch (Exception e) {
            System.out.println("Erro ao abrir o navegador: " + e.getMessage());
        }
    }

    @FXML
    private void saveCanvas() {
        try {
            var desktopFile = new java.io.File(System.getProperty("user.home") + "/Desktop");
            var fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Salvar imagem");
            fileChooser.setInitialDirectory(desktopFile);
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Imagens", "*.png"));
            var file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    javafx.embed.swing.SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null);
                    var image = canvas.snapshot(null, null);
                    if (javax.imageio.ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(image, null), "png",
                            file)) {
                        JavaFXUtils.showNotification("Imagem salva com sucesso", "A imagem foi salva com sucesso",
                                Styles.SUCCESS, primaryContainer);
                    } else {
                        throw new Exception("Erro ao salvar a imagem");
                    }
                } catch (Exception e) {
                    JavaFXUtils.showNotification("Erro ao salvar a imagem", "Não foi possível salvar a imagem",
                            Styles.DANGER, primaryContainer);
                }
            }
        } catch (Exception e) {
            JavaFXUtils.showNotification("Erro no processo de salvar a imagem", "Não foi possível salvar o canvas",
                    Styles.DANGER, primaryContainer);
        }
    }

    @FXML
    private void openPreferences() {
        try {
            var loader = MainApp.loadFXML("preferences");
            Node root = loader.load();
            Runnable reRenderCallback = () -> {
                renderer.clear();
                renderer.render(rasterCanvas, canvasColors);
                updateCanvasRenderMap();
            };
            JavaFXUtils.showModal(root, 400, 400, modalPane, reRenderCallback, false);
        } catch (Exception e) {
            JavaFXUtils.showNotification("Erro ao abrir as preferências", "Não foi possível abrir as preferências",
                    Styles.DANGER, primaryContainer);
        }
    }

    /**
     * Start the key bindings for the scene
     */
    public void startKeyBindings() {
        var scene = stage.getScene();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            try {
                if (event.getCode() == KeyCode.ESCAPE) {
                    resetActionButton();
                    resetClipButtonStatus();
                    endDrawMode();
                }
                if (event.isShiftDown()) {
                    if (!isDrawing && !clipButton.isSelected())
                        startDrawMode(ShapeType.LINE);
                    else
                        throw new RuntimeException("Não é possível desenhar um círculo e uma linha ao mesmo tempo");
                }
                if (event.isControlDown()) {
                    if (!isDrawing && !clipButton.isSelected())
                        startDrawMode(ShapeType.CIRCLE);
                    else
                        throw new RuntimeException("Não é possível desenhar um círculo e uma linha ao mesmo tempo");
                }
                if (event.isAltDown()) {
                    if (!isDrawing && !clipButton.isSelected())
                        clipButton.fire();
                    else
                        throw new RuntimeException("Não é possível recortar enquanto desenha uma figura");
                }
            } catch (Exception e) {
                JavaFXUtils.showNotification("Erro no evento de teclas", e.getMessage(), Styles.DANGER,
                        primaryContainer);
            }
        });
    }

    @FXML
    private void closeWindow() {
        if (shapes.isEmpty()) {
            stage.close();
        }
        var pane = new BorderPane();
        pane.setPrefWidth(220);
        pane.setPrefHeight(160);
        pane.setMaxWidth(220);
        pane.setMaxHeight(160);
        pane.setStyle("-fx-background-color: -color-bg-default");
        var text = new Text(
                "Você tem certeza que deseja sair do programa?\n Todas as alterações não salvas serão perdidas.");
        var button = new Button("Sair do programa");
        button.getStyleClass().addAll(Styles.DANGER, Styles.INTERACTIVE);
        button.setOnAction(event -> stage.close());
        pane.setCenter(text);
        pane.setBottom(button);
        BorderPane.setAlignment(button, javafx.geometry.Pos.CENTER);
        BorderPane.setAlignment(text, javafx.geometry.Pos.CENTER);
        JavaFXUtils.showModal(pane, 220, 160, modalPane, null, false);
    }
}
