package org.ferreteria.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailDto {

    @Null
    private Long id;

    @NotNull(message = "{sale.id.required}")
    private Long saleId;

    @NotNull(message = "{NotNull.purchaseDetailDto.productId}")
    private Long productId;

    @Min(value = 1,message = "{Min.purchaseDetailDto.quantity}")
    @NotNull(message = "{NotNull.purchaseDetailDto.quantity}")
    private int quantity;
}
