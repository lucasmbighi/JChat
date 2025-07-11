package com.lucasbighi.jchat.repository;

import com.lucasbighi.jchat.model.Chat;
import com.lucasbighi.jchat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByParticipants_Id(UUID userId);
    List<Chat> findAllByParticipantsContaining(User user);
}
