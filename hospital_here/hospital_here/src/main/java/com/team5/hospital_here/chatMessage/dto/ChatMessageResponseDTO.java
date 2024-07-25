package com.team5.hospital_here.chatMessage.dto;

import com.team5.hospital_here.user.entity.user.UserDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponseDTO {
    private Long chatRoomId;
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
