package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DimensionDto {

    @DecimalMin(value = "1.0")
    private double width;

    @DecimalMin(value = "1.0")
    private double height;

    @DecimalMin(value = "1.0")
    private double depth;
}