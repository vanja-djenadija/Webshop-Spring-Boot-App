package com.example.webshop.controllers;

import com.example.webshop.models.dto.Message;
import com.example.webshop.models.requests.MessageRequest;
import com.example.webshop.services.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@RequestBody @Valid MessageRequest request, Authentication authentication) {
        return service.create(request, authentication);
    }
}