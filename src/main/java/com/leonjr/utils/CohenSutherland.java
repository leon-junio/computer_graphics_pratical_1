package com.leonjr.utils;

import javafx.scene.paint.Color;

public class CohenSutherland {

    /**
     * Clip a line from (x1, y1) to (x2, y2) using Cohen-Sutherland algorithm.
     * 
     * @param x1          The x-coordinate of the first point.
     * @param y1          The y-coordinate of the first point.
     * @param x2          The x-coordinate of the second point.
     * @param y2          The y-coordinate of the second point.
     * @param xMin        The minimum x-coordinate of the clipping window.
     * @param yMin        The minimum y-coordinate of the clipping window.
     * @param xMax        The maximum x-coordinate of the clipping window.
     * @param yMax        The maximum y-coordinate of the clipping window.
     * @param canvas      The canvas to draw the line on.
     * @param canvasColor The canvas to draw the line on with colors.
     * @param color       The color of the line.
     */
    public static void lineClip(double x1, double y1, double x2, double y2, double xMin, double yMin, double xMax,
            double yMax, boolean[][] canvas, Color[][] canvasColor, Color color) {
        int code1 = computeOutCode(x1, y1, xMin, yMin, xMax, yMax);
        int code2 = computeOutCode(x2, y2, xMin, yMin, xMax, yMax);
        boolean accept = false;

        while (true) {
            if ((code1 | code2) == 0) { // Totalmente dentro
                accept = true;
                break;
            } else if ((code1 & code2) != 0) { // Totalmente fora
                break;
            } else {
                double x, y;
                int outcodeOut = (code1 != 0) ? code1 : code2;

                if ((outcodeOut & 8) != 0) { // Acima
                    x = x1 + (x2 - x1) * (yMax - y1) / (y2 - y1);
                    y = yMax;
                } else if ((outcodeOut & 4) != 0) { // Abaixo
                    x = x1 + (x2 - x1) * (yMin - y1) / (y2 - y1);
                    y = yMin;
                } else if ((outcodeOut & 2) != 0) { // Direita
                    y = y1 + (y2 - y1) * (xMax - x1) / (x2 - x1);
                    x = xMax;
                } else { // Esquerda
                    y = y1 + (y2 - y1) * (xMin - x1) / (x2 - x1);
                    x = xMin;
                }

                if (outcodeOut == code1) {
                    x1 = x;
                    y1 = y;
                    code1 = computeOutCode(x1, y1, xMin, yMin, xMax, yMax);
                } else {
                    x2 = x;
                    y2 = y;
                    code2 = computeOutCode(x2, y2, xMin, yMin, xMax, yMax);
                }
            }
        }

        if (accept) {
            DDA.line((int) x1, (int) y1, (int) x2, (int) y2, canvas, canvasColor, color);
        }
    }

    /**
     * Compute the outcode of a point (x, y) based on the clipping window defined by
     * (xMin, yMin) and (xMax, yMax).
     * 
     * @param x    The x-coordinate of the point.
     * @param y    The y-coordinate of the point.
     * @param xMin The minimum x-coordinate of the clipping window.
     * @param yMin The minimum y-coordinate of the clipping window.
     * @param xMax The maximum x-coordinate of the clipping window.
     * @param yMax The maximum y-coordinate of the clipping window.
     * @return The outcode of the point.
     */
    private static int computeOutCode(double x, double y, double xMin, double yMin, double xMax, double yMax) {
        int code = 0;
        if (x < xMin) {
            code |= 1; // Esquerda
        } else if (x > xMax) {
            code |= 2; // Direita
        }
        if (y < yMin) {
            code |= 4; // Abaixo
        } else if (y > yMax) {
            code |= 8; // Acima
        }
        return code;
    }

}
