package com.example.webshop.security;

import lombok.Data;

import java.util.List;

@Data
public class AuthorizationRules {
    private List<Rule> rules;
}