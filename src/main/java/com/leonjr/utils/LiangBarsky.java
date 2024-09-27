package com.leonjr.utils;

import javafx.scene.paint.Color;

public class LiangBarsky {
    
    /**
     * Clip a line from (x1, y1) to (x2, y2) using Liang-Barsky algorithm.
     * 
     * @param x1           The x-coordinate of the first point.
     * @param y1           The y-coordinate of the first point.
     * @param x2           The x-coordinate of the second point.
     * @param y2           The y-coordinate of the second point.
     * @param xMin         The minimum x-coordinate of the clipping window.
     * @param yMin         The minimum y-coordinate of the clipping window.
     * @param xMax         The maximum x-coordinate of the clipping window.
     * @param yMax         The maximum y-coordinate of the clipping window.
     * @param canvas       The canvas to draw the line on.
     * @param canvasColors The canvas to draw the line on with colors.
     * @param color        The color of the line.
     */
    public static void clipLiangBarsky(double x1, double y1, double x2, double y2, double xMin, double yMin, double xMax,
            double yMax, boolean[][] canvas, Color[][] canvasColors, Color color) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double p[] = { -dx, dx, -dy, dy };
        double q[] = { x1 - xMin, xMax - x1, y1 - yMin, yMax - y1 };
        double u1 = 0.0, u2 = 1.0;
        for (int i = 0; i < 4; i++) {
            if (p[i] == 0 && q[i] < 0) {
                return;
            }
            double r = q[i] / p[i];
            if (p[i] < 0) {
                u1 = Math.max(u1, r);
            } else if (p[i] > 0) {
                u2 = Math.min(u2, r);
            }
        }
        if (u1 > u2)
            return;
        double xClip1 = x1 + u1 * dx;
        double yClip1 = y1 + u1 * dy;
        double xClip2 = x1 + u2 * dx;
        double yClip2 = y1 + u2 * dy;
        DDA.line((int) xClip1, (int) yClip1, (int) xClip2, (int) yClip2, canvas, canvasColors, color);
    }
}
