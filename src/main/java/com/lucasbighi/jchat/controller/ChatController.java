package com.lucasbighi.jchat.controller;

import com.lucasbighi.jchat.dto.ChatMapper;
import com.lucasbighi.jchat.dto.ChatResponse;
import com.lucasbighi.jchat.dto.CreateChatRequest;
import com.lucasbighi.jchat.model.Chat;
import com.lucasbighi.jchat.model.Message;
import com.lucasbighi.jchat.model.User;
import com.lucasbighi.jchat.repository.ChatRepository;
import com.lucasbighi.jchat.repository.UserRepository;
import com.lucasbighi.jchat.service.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    public ChatController(
            ChatService chatService,
            ChatRepository chatRepository,
            UserRepository userRepository,
            ChatMapper chatMapper
    ) {
        this.chatService = chatService;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatMapper = chatMapper;
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody CreateChatRequest request) {
        List<User> users = userRepository.findAllById(request.participantIds());

        if (users.size() != request.participantIds().size()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "One or more user IDs are invalid"));
        }

        Chat chat = new Chat();
        chat.setParticipants(new HashSet<>(users));
        chat.setCreatedAt(new Date());

        Chat saved = chatRepository.save(chat);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(chatMapper.toChatResponse(saved));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatResponse> getChat(@PathVariable UUID chatId) {
        return chatService.getChatById(chatId)
                .map(chatMapper::toChatResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatResponse>> getUserChats(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Chat> chats = chatRepository.findAllByParticipantsContaining(user);
        List<ChatResponse> responseList = chats.stream()
                .map(chatMapper::toChatResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }
}
