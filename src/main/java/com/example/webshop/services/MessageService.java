package com.example.webshop.services;

import com.example.webshop.models.dto.Message;
import com.example.webshop.models.requests.MessageRequest;
import org.springframework.security.core.Authentication;

public interface MessageService {
    Message create(MessageRequest request, Authentication authentication);
}