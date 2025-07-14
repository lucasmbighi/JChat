package com.lucasbighi.jchat.controller;

import com.lucasbighi.jchat.dto.ChatMessage;
import com.lucasbighi.jchat.model.Message;
import com.lucasbighi.jchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        Message saved = chatService.sendMessage(
                chatMessage.getChatId(),
                chatMessage.getSenderId(),
                chatMessage.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/chats/" + chatMessage.getChatId(), saved
        );
    }
}
