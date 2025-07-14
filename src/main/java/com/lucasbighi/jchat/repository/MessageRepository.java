package com.lucasbighi.jchat.repository;

import com.lucasbighi.jchat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByChat_IdOrderBySentAtAsc(UUID chatId);
}
