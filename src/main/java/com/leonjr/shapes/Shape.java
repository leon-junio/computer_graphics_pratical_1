package com.leonjr.shapes;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Shape {
    
    /**
     * Position can be used to store the center of a circle, the middle of a line,
     * etc.
     */
    protected double[] position;
    /**
     * Color of the shape.
     */
    protected Color color;

    /**
     * Rasterize the shape on the canvas.
     * 
     * @param canvas       The canvas to rasterize the shape on based on boolean
     *                     values.
     * @param canvasColors The canvas to rasterize the shape on based on colors.
     */
    public abstract void rasterize(boolean[][] canvas, Color[][] canvasColors);

    /**
     * Translate the shape by tx and ty.
     * 
     * @param tx TX value is the translation in the x-axis.
     * @param ty TY value is the translation in the y-axis.
     */
    public abstract void translate(double tx, double ty);

    /**
     * Rotate the shape by an angle.
     * 
     * @param angle The angle to rotate the shape.
     */
    public abstract void rotate(double angle);

    /**
     * Scale the shape by sx and sy.
     * 
     * @param sx SX value is the scale in the x-axis.
     * @param sy SY value is the scale in the y-axis.
     */
    public abstract void scale(double sx, double sy);

    /**
     * Reflect the shape in the x-axis.
     */
    public abstract void reflectX();

    /**
     * Reflect the shape in the y-axis.
     */
    public abstract void reflectY();

    /**
     * Reflect the shape in the x-axis and y-axis.
     */
    public abstract void reflectXY();
}
