package com.example.webshop.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseRequest {
    @NotNull
    private Integer customerId;
}