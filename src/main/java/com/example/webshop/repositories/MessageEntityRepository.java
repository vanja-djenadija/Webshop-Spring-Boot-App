package com.example.webshop.repositories;

import com.example.webshop.models.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Integer> {
}