package org.ferreteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {

    private Long id;

    @Size(min = 3,max = 40, message = "{entities.error.name.size}")
    @NotBlank(message = "{entities.error.name.blank}")
    private String name;

    @NotBlank(message = "{entities.error.phone.blank}")
    @Pattern(regexp = "\\d{10}",message = "{entities.error.phone.pattern}")
    private String phoneNumber;

    @NotBlank(message = "{address.error.blank}")
    @Size(min = 5,max = 100, message = "{address.error.size}")
    private String address;
}
