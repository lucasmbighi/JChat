package com.lucasbighi.jchat.service;

import com.lucasbighi.jchat.model.Chat;
import com.lucasbighi.jchat.model.User;
import com.lucasbighi.jchat.repository.ChatRepository;
import com.lucasbighi.jchat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public List<Chat> getUserChats(UUID userId) {
        return chatRepository.findAllByParticipants_Id(userId);
    }

    public Optional<Chat> getChatById(UUID chatId) {
        return chatRepository.findById(chatId);
    }

    public Chat createChat(List<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        Chat chat = new Chat();
        chat.setParticipants(new HashSet<>(users));
        return chatRepository.save(chat);
    }

    public void deleteChat(UUID chatId) {
        chatRepository.deleteById(chatId);
    }
}
