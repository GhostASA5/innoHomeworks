package org.project;

import lombok.*;

import java.text.MessageFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;

    private String description;

    private Integer price;

    private Integer quantity;

    @Override
    public String toString() {
        return MessageFormat
                .format("Product: id - {0}, description - {1}, price - {2}, quantity - {3}",
                        id,
                        description,
                        price,
                        quantity
                );
    }
}
