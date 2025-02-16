package org.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeoJsonResponse {

    private String type;

    private List<Feature> features;
}
