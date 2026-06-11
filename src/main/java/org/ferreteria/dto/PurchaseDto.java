package org.ferreteria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {

    @Null
    private Long id;

    @NotNull(message = "{purchase.supplierId.notnull}")
    private Long supplierId;


    @NotNull(message = "{purchase.localDate.notnull}")
    private LocalDate date;
}
