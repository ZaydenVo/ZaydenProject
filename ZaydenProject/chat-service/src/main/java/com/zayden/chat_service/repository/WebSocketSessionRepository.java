package com.zayden.chat_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zayden.chat_service.entity.WebSocketSession;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {
    void deleteBySocketSessionId(String socketId);

    List<WebSocketSession> findAllByUserIdIn(List<String> userIds);
}
