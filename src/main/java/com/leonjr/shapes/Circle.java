package com.leonjr.shapes;

import com.leonjr.utils.Bresenham;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Circle extends Shape {
    private double[] center;
    private double radius;
    private Color color;

    public Circle(double[] center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.position = new double[] { center[0], center[1] };
    }

    @Override
    public void rasterize(boolean[][] canvas, Color[][] canvasColors) {
        Bresenham.circle((int) center[0], (int) center[1], (int) radius, canvas, canvasColors, color);
    }

    @Override
    public void translate(double tx, double ty) {
        this.center[0] += tx;
        this.center[1] += ty;
        this.position[0] += tx;
        this.position[1] += ty;
    }

    @Override
    public void rotate(double angle) {
        // A rotação de um círculo em torno de si mesmo não altera sua forma, então não há nada a fazer aqui.
    }

    @Override
    public void scale(double sx, double sy) {
        this.radius *= Math.min(sx, sy);
    }

    @Override
    public void reflectX() {
        this.center[1] = 2 * position[1] - center[1];
    }

    @Override
    public void reflectY() {
        this.center[0] = 2 * position[0] - center[0];
    }

    @Override
    public void reflectXY() {
        reflectX();
        reflectY();
    }

    public double[] getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }
}