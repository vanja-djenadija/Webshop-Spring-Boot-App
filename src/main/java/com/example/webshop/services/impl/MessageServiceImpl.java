package com.example.webshop.services.impl;

import com.example.webshop.models.dto.JWTUser;
import com.example.webshop.models.dto.Message;
import com.example.webshop.models.entities.MessageEntity;
import com.example.webshop.models.requests.MessageRequest;
import com.example.webshop.repositories.MessageEntityRepository;
import com.example.webshop.services.MessageService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    private final ModelMapper modelMapper;
    private final MessageEntityRepository repository;

    public MessageServiceImpl(ModelMapper modelMapper, MessageEntityRepository repository) {
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    @Override
    public Message create(MessageRequest request, Authentication authentication) {
        MessageEntity messageEntity = modelMapper.map(request, MessageEntity.class);
        messageEntity.setId(null);
        messageEntity.setIsRead(false);
        JWTUser user = (JWTUser) authentication.getPrincipal();
        Logger.getLogger(getClass()).info("MESSAGE CREATED by user(id) " + user.getId());
        return modelMapper.map(repository.saveAndFlush(messageEntity), Message.class);
    }
}