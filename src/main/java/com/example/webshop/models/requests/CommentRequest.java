package com.example.webshop.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentRequest {
    @NotBlank
    private String content;
    @NotNull
    private Timestamp dateTime;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer productId;
}