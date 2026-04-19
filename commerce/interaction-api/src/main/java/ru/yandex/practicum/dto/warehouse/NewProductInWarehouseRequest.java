package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewProductInWarehouseRequest {
    @NotNull
    private UUID productId;

    private boolean fragile;

    @NotNull
    private DimensionDto dimension;

    @NotNull
    @DecimalMin(value = "1.0")
    private Double weight;
}
