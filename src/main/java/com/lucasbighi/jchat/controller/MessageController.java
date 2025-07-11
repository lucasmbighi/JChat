package com.lucasbighi.jchat.controller;

import com.lucasbighi.jchat.dto.ChatMapper;
import com.lucasbighi.jchat.dto.MessageResponse;
import com.lucasbighi.jchat.model.Message;
import com.lucasbighi.jchat.model.User;
import com.lucasbighi.jchat.repository.UserRepository;
import com.lucasbighi.jchat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    public MessageController(
            MessageService messageService,
            UserRepository userRepository,
            ChatMapper chatMapper
    ) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.chatMapper = chatMapper;
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<?> sendMessage(
            Authentication authentication,
            @PathVariable UUID chatId,
            @RequestBody String content
            ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Message message = messageService.sendMessage(chatId, user.getId(), content);
        return ResponseEntity.ok(chatMapper.toMessageResponse(message));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable UUID chatId) {
        List<Message> messages = messageService.getMessages(chatId);
        List<MessageResponse> responseList = messages.stream()
                .map(chatMapper::toMessageResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable UUID messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message
                .map(chatMapper::toMessageResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
