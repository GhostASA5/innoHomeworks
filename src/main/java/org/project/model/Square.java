package org.project.model;

import lombok.Getter;
import lombok.Setter;
import org.project.MoveFigure;

@Getter
@Setter
public class Square extends Rectangle implements MoveFigure {

    int x;

    int y;

    public Square(double a, int x, int y) {
        super(a, a);
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
