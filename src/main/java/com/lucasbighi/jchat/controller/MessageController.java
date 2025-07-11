package com.lucasbighi.jchat.controller;

import com.lucasbighi.jchat.model.Chat;
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

    public MessageController(
            MessageService messageService,
            UserRepository userRepository
    ) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(
            Authentication authentication,
            @RequestParam UUID chatId,
            @RequestParam String content
    ) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(messageService.sendMessage(chatId, user.getId(), content));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable UUID chatId) {
        return ResponseEntity.ok(messageService.getMessages(chatId));
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
