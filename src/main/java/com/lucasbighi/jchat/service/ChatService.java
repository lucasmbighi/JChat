package com.lucasbighi.jchat.service;

import com.lucasbighi.jchat.model.Chat;
import com.lucasbighi.jchat.model.Message;
import com.lucasbighi.jchat.model.User;
import com.lucasbighi.jchat.repository.ChatRepository;
import com.lucasbighi.jchat.repository.MessageRepository;
import com.lucasbighi.jchat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
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

    public Message sendMessage(UUID chatId, UUID senderId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);
        message.setSentAt(Instant.now());

        return messageRepository.save(message);
    }

    public void deleteChat(UUID chatId) {
        chatRepository.deleteById(chatId);
    }
}
