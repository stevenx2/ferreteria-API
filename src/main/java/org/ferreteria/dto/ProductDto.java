package org.ferreteria.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Clase que realmente se expone al cliente en lugar de la entidad Product de la base de datos, de esta manera
 * solo muestro los campos que yo quiero.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @Null
    private Long id;

    @NotBlank(message = "{product.name.required}")
    @Size(min = 3, max = 30, message = "{product.name.size}")
    private String name;

    @Digits(integer = 8, fraction = 2, message = "{product.price.digits}")
    @DecimalMin(value = "200.00", message = "{product.price.min}")
    private double price;

    @PositiveOrZero(message = "{product.stock.positive}")
    private int stock;

    @NotNull
    private Long supplierId;


}
