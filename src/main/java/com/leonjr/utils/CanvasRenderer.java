package com.leonjr.utils;

import com.leonjr.confs.UserConfs;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasRenderer {
    private Canvas fxCanvas;
    private GraphicsContext gc;

    public CanvasRenderer(Canvas canvas) {
        this.fxCanvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * Render the rasterCanvas on the JavaFX Canvas.
     * 
     * @param rasterCanvas       Representation of the canvas as a 2D boolean array.
     * @param rasterCanvasColors Representation of the canvas as a 2D Color array.
     */
    public void render(boolean[][] rasterCanvas, Color[][] rasterCanvasColors) {
        clear();
        for (int y = 0; y < rasterCanvas.length; y++) {
            for (int x = 0; x < rasterCanvas[y].length; x++) {
                if (rasterCanvas[y][x]) {
                    gc.setFill(rasterCanvasColors[y][x] == null ? UserConfs.CANVAS_COLOR : rasterCanvasColors[y][x]);
                    gc.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    /**
     * Clear the JavaFX canvas.
     */
    public void clear() {
        gc.setFill(UserConfs.CANVAS_COLOR);
        gc.fillRect(0, 0, fxCanvas.getWidth(), fxCanvas.getHeight());
    }
}
