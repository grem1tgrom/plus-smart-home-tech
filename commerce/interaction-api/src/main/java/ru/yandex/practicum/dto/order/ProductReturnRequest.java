package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReturnRequest {
    @NotBlank
    private UUID orderId;

    @NotBlank
    private Map<UUID, Long> products;
}
