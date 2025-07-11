package com.lucasbighi.jchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private UUID id;
    private List<UserDTO> participants;
    private List<MessageResponse> messages;
}
