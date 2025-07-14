package com.lucasbighi.jchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private UUID chatId;
    private UUID senderId;
    private String content;
}
