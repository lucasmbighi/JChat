package com.lucasbighi.jchat.dto;

import com.lucasbighi.jchat.model.Chat;
import com.lucasbighi.jchat.model.Message;
import com.lucasbighi.jchat.model.User;
import org.springframework.stereotype.Component;

// ChatMapper.java
@Component
public class ChatMapper {

    public UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public MessageResponse toMessageResponse(Message msg) {
        MessageResponse response = new MessageResponse();
        response.setId(msg.getId());
        response.setSenderId(msg.getSender().getId());
        response.setContent(msg.getContent());
        response.setSentAt(msg.getSentAt());
        return response;
    }

    public ChatResponse toChatResponse(Chat chat) {
        ChatResponse res = new ChatResponse();
        res.setId(chat.getId());
        res.setParticipants(chat.getParticipants().stream().map(this::toUserDTO).toList());
        res.setMessages(chat.getMessages().stream().map(this::toMessageResponse).toList());
        return res;
    }
}
