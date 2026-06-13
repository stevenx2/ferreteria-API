package org.ferreteria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {

    @Null
    private Long id;

    @NotNull(message = "{purchase.localDate.notnull}")
    private LocalDate date;

    @NotNull(message = "{client.id.required}")
    private Long clientId;
}
