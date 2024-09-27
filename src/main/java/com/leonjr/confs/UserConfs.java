package com.leonjr.confs;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class UserConfs implements Serializable {

    public static final long serialVersionUID = 1L;
    
    public static Color CANVAS_COLOR;
    public static int CANVAS_WIDTH = 800;
    public static int CANVAS_HEIGHT = 600;
    public static Color LINE_COLOR;

    // renders and rasters
    public static boolean DDA_RASTER = true;

    // clipping
    public static boolean COHEN_SUTHERLAND = false;

    // Reflections
    public static byte REFLECTION_TYPE = 0;

}