package org.project.model;

public abstract class Figure {

    protected double a;

    protected double b;

    public Figure(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getPerimeter() {
        return 0;
    }
}
