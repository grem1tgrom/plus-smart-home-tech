package ru.yandex.practicum.dto.payment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private UUID paymentId;

    private BigDecimal totalPayment;

    private BigDecimal deliveryTotal;

    private BigDecimal feeTotal;
}
