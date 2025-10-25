package com.zayden_project.addFriend_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.zayden_project.addFriend_service.enums.FriendStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String user1;

    @Column(nullable = false)
    String user2;

    @Column(nullable = false)
    String senderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    FriendStatus status;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column
    LocalDateTime updatedAt;
}
