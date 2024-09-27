package com.leonjr.shapes;

import com.leonjr.confs.UserConfs;
import com.leonjr.utils.Bresenham;
import com.leonjr.utils.DDA;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Line extends Shape {
    private double[] start;
    private double[] end;
    private Color color;

    public Line(double[] start, double[] end, Color color) {
        this.start = start;
        this.end = end;
        this.position = new double[] { (start[0] + end[0]) / 2, (start[1] + end[1]) / 2 };
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void rasterize(boolean[][] canvas, Color[][] canvasColors) {
        if (UserConfs.DDA_RASTER)
            DDA.line((int) start[1], (int) start[0], (int) end[1], (int) end[0], canvas, canvasColors, color);
        else
            Bresenham.line((int) start[1], (int) start[0], (int) end[1], (int) end[0], canvas, canvasColors, color);
    }

    @Override
    public void translate(double tx, double ty) {
        this.start[0] += tx;
        this.start[1] += ty;
        this.end[0] += tx;
        this.end[1] += ty;
        this.position[0] += tx;
        this.position[1] += ty;
    }

    @Override
    public void rotate(double angle) {
        // Rotacionar em torno da posição central
        double rad = Math.toRadians(angle);
        double cosTheta = Math.cos(rad);
        double sinTheta = Math.sin(rad);

        // Rotaciona o ponto inicial
        double x1 = start[0] - position[0];
        double y1 = start[1] - position[1];
        this.start[0] = position[0] + (x1 * cosTheta - y1 * sinTheta);
        this.start[1] = position[1] + (x1 * sinTheta + y1 * cosTheta);

        // Rotaciona o ponto final
        double x2 = end[0] - position[0];
        double y2 = end[1] - position[1];
        this.end[0] = position[0] + (x2 * cosTheta - y2 * sinTheta);
        this.end[1] = position[1] + (x2 * sinTheta + y2 * cosTheta);
    }

    @Override
    public void scale(double sx, double sy) {
        this.start[1] = position[1] + (start[1] - position[1]) * sx;
        this.start[0] = position[0] + (start[0] - position[0]) * sy;
        this.end[1] = position[1] + (end[1] - position[1]) * sx;
        this.end[0] = position[0] + (end[0] - position[0]) * sy;
    }

    @Override
    public void reflectX() {
        this.start[1] = 2 * position[1] - start[1];
        this.end[1] = 2 * position[1] - end[1];
    }

    @Override
    public void reflectY() {
        this.start[0] = 2 * position[0] - start[0];
        this.end[0] = 2 * position[0] - end[0];
    }

    @Override
    public void reflectXY() {
        reflectX();
        reflectY();
    }
}