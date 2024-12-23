package org.project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ellipse extends Figure {

    public Ellipse(double a, double b) {
        super(a, b);
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2)) / 2);
    }
}
