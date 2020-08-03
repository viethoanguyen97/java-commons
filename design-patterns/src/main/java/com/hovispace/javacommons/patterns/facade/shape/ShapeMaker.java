package com.hovispace.javacommons.patterns.facade.shape;

/**
 * ShapeMaker class uses the concrete classes to delegate user calls to Shape descendants.
 */
public class ShapeMaker {

    private final Shape _circle;
    private final Shape _rectangle;
    private final Shape _square;

    public ShapeMaker() {
        _circle = new Circle();
        _rectangle = new Rectangle();
        _square = new Square();
    }

    public void drawCircle(){
        _circle.draw();
    }

    public void drawRectangle(){
        _rectangle.draw();
    }

    public void drawSquare(){
        _square.draw();
    }
}
