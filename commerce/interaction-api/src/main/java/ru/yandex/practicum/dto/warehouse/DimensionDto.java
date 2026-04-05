package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DimensionDto {
    @DecimalMin("1.0")
    @NotNull
    private Double width;

    @DecimalMin("1.0")
    @NotNull
    private Double height;

    @DecimalMin("1.0")
    @NotNull
    private Double depth;
}