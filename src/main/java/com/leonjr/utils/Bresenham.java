package com.leonjr.utils;

import javafx.scene.paint.Color;

public class Bresenham {

    /**
     * Draw a line from (x1, y1) to (x2, y2) using Bresenham's line algorithm.
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
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        while (true) {
            // draw pixel and color
            canvas[y1][x1] = true;
            canvasColors[y1][x1] = color;
            if (x1 == x2 && y1 == y2)
                break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    /**
     * Draw a circle centered at (xc, yc) with radius r using Bresenham's circle
     * algorithm.
     * 
     * @param xc           The x-coordinate of the center of the circle.
     * @param yc           The y-coordinate of the center of the circle.
     * @param r            The radius of the circle.
     * @param canvas       The canvas to draw the circle on.
     * @param canvasColors The canvas to draw the circle on with colors.
     * @param color        The color of the circle.
     */
    public static void circle(int xc, int yc, int r, boolean[][] canvas, Color[][] canvasColors, Color color) {
        int x = 0;
        int y = r;
        int d = 3 - 2 * r;
        drawCirclePoints(xc, yc, x, y, canvas, canvasColors, color);

        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
            drawCirclePoints(xc, yc, x, y, canvas, canvasColors, color);
        }
    }

    /**
     * Draw the points of the circle centered at (xc, yc) with radius r. This method
     * uses the octants of the circle to draw the points.
     * 
     * @param xc           The x-coordinate of the center of the circle.
     * @param yc           The y-coordinate of the center of the circle.
     * @param x            The x-coordinate of the point to draw.
     * @param y            The y-coordinate of the point to draw.
     * @param canvas       The canvas to draw the circle on.
     * @param canvasColors The canvas to draw the circle on with colors.
     * @param color        The color of the circle.
     */
    private static void drawCirclePoints(int xc, int yc, int x, int y, boolean[][] canvas, Color[][] canvasColors,
            Color color) {
        canvas[xc + x][yc + y] = true;
        canvasColors[xc + x][yc + y] = color;

        canvas[xc - x][yc + y] = true;
        canvasColors[xc - x][yc + y] = color;

        canvas[xc + x][yc - y] = true;
        canvasColors[xc + x][yc - y] = color;

        canvas[xc - x][yc - y] = true;
        canvasColors[xc - x][yc - y] = color;

        canvas[xc + y][yc + x] = true;
        canvasColors[xc + y][yc + x] = color;

        canvas[xc - y][yc + x] = true;
        canvasColors[xc - y][yc + x] = color;

        canvas[xc + y][yc - x] = true;
        canvasColors[xc + y][yc - x] = color;

        canvas[xc - y][yc - x] = true;
        canvasColors[xc - y][yc - x] = color;
    }

}
