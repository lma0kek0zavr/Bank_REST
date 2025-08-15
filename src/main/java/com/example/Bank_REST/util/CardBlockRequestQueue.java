package com.example.Bank_REST.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.example.Bank_REST.util.request.CardBlockRequest;

@Component
public class CardBlockRequestQueue {

    private final Map<Long, CardBlockRequest> queue = new ConcurrentHashMap<>();

    private final AtomicLong requestId = new AtomicLong(0L);

    public CardBlockRequest add(Long cardId, Long userId) {
        CardBlockRequest req = CardBlockRequest.builder()
            .id(requestId.incrementAndGet())
            .cardId(cardId)
            .userId(userId)
            .createdAt(LocalDateTime.now())
            .status("PENDING")
            .build();

        queue.put(req.getId(), req);

        return req;
    }

    public List<CardBlockRequest> getAll() {
        return List.copyOf(queue.values());
    }

    public Optional<CardBlockRequest> get(Long id) {
        return Optional.ofNullable(queue.get(id));
    }

    public void remove(Long id) {
        queue.remove(id);
    }
}
