package org.project;


import org.project.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        List<Figure> figures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("resources/figures.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                switch (type) {
                    case "Circle":
                        figures.add(new Circle(
                                Double.parseDouble(parts[1]),
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3])
                        ));
                        break;
                    case "Square":
                        figures.add(new Square(
                                Double.parseDouble(parts[1]),
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3])
                        ));
                        break;
                    case "Rectangle":
                        figures.add(new Rectangle(Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
                        break;
                    case "Ellipse":
                        figures.add(new Ellipse(Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/output.txt"))) {
            for (Figure figure : figures) {
                writer.write(figure.getClass() + " perimeter: " + figure.getPerimeter() + "\n");
            }
            System.out.println("Запись прошла успешно.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}