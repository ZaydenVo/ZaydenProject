package com.zayden.chat_service.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationRequest {
    String type;

    @Size(min = 1)
    @NonNull
    List<String> participantIds;
}
