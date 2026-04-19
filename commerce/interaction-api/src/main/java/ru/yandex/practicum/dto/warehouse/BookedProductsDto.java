package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedProductsDto {
    @NotNull
    private double deliveryWeight;

    @NotNull
    private double deliveryVolume;

    @NotNull
    private boolean fragile;
}
