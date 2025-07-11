package com.lucasbighi.jchat.service;

import com.lucasbighi.jchat.model.Chat;
import com.lucasbighi.jchat.model.Message;
import com.lucasbighi.jchat.model.User;
import com.lucasbighi.jchat.repository.ChatRepository;
import com.lucasbighi.jchat.repository.MessageRepository;
import com.lucasbighi.jchat.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public List<Message> getMessages(UUID chatId) {
        return messageRepository.findAllByChat_IdOrderBySentAtAsc(chatId);
    }

    public Optional<Message> getMessageById(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    public Message sendMessage(UUID chatId, UUID senderId, String content) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        User sender = userRepository.findById(senderId).orElseThrow();

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);
        message.setSentAt(Instant.now());

        return messageRepository.save(message);
    }

    public void deleteMessage(UUID messageId) {
        messageRepository.deleteById(messageId);
    }
}
