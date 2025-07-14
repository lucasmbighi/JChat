package com.lucasbighi.jchat.dto;

import java.util.List;
import java.util.UUID;

public record CreateChatRequest(List<UUID> participantIds) {
}
