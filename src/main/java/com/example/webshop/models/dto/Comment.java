package com.example.webshop.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    private Integer id;
    private String content;
    private Timestamp dateTime;
    private User user;
}