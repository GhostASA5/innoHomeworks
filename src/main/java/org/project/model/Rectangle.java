package org.project.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rectangle extends Figure {

    public Rectangle(double a, double b) {
        super(a, b);
    }

    @Override
    public double getPerimeter() {
        return 2 * (super.a + super.b);
    }
}
