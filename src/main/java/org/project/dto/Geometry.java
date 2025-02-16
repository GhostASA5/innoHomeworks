package org.project.dto;

import lombok.Data;

import java.util.List;

@Data
class Geometry {

    private String type;

    private List<Double> coordinates;
}
