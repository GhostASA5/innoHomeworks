package org.project.dto;

import lombok.Data;

@Data
public class Feature {

    private String type;

    private Properties properties;

    private Geometry geometry;
}
