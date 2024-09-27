package com.leonjr.utils;

import javafx.scene.paint.Color;

public class DDA {

    /**
     * Draw a line from (x1, y1) to (x2, y2) using DDA algorithm.
     * 
     * @param x1           The x-coordinate of the first point.
     * @param y1           The y-coordinate of the first point.
     * @param x2           The x-coordinate of the second point.
     * @param y2           The y-coordinate of the second point.
     * @param canvas       The canvas to draw the line on.
     * @param canvasColors The canvas to draw the line on with colors.
     * @param color        The color of the line.
     */
    public static void line(int x1, int y1, int x2, int y2, boolean[][] canvas, Color[][] canvasColors, Color color) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double steps = Math.max(Math.abs(dx), Math.abs(dy));

        double xIncrement = dx / steps;
        double yIncrement = dy / steps;

        double x = x1;
        double y = y1;

        for (int i = 0; i <= steps; i++) {
            canvas[(int) Math.round(y)][(int) Math.round(x)] = true;
            canvasColors[(int) Math.round(y)][(int) Math.round(x)] = color;
            x += xIncrement;
            y += yIncrement;
        }
    }
}
