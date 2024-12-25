package org.project.model;

import lombok.Getter;
import lombok.Setter;
import org.project.MoveFigure;

@Getter
@Setter
public class Circle extends Ellipse implements MoveFigure {

    int x;

    int y;

    public Circle(double radius, int x, int y) {
        super(radius, radius);
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
