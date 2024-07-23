package com.team5.hospital_here.chatRoom.entity;

import com.team5.hospital_here.chatMessage.entity.ChatMessage;
import com.team5.hospital_here.chatRoom.enums.ChatRoomStatus;
import com.team5.hospital_here.chatRoom.enums.ChatRoomType;
import com.team5.hospital_here.common.baseEntity.BaseEntity;
import com.team5.hospital_here.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @OneToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;

    @OneToOne
    @JoinColumn(name = "leave_user")
    private User leaveUser;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @Builder
    public ChatRoom(User user1, User user2, ChatRoomType type, ChatRoomStatus status,
        List<ChatMessage> chatMessages) {
        this.user1 = user1;
        this.user2 = user2;
        this.type = type;
        this.status = status;
        this.chatMessages = chatMessages;
    }

    public void addLeaveUser(User user) {
        leaveUser = user;
    }

    public void updateStatus(ChatRoomStatus chatRoomStatus) {
        status = chatRoomStatus;
    }

    public boolean isChatRoomMember(Long userId) {
        try {
            return Objects.equals(user1.getUserId(), userId) || Objects.equals(user2.getUserId(),
                userId);
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    public void addChatRoomMember(User user) {
        if (user1 == null) {
            user1 = user;
            return;
        }

        user2 = user;
    }
}
