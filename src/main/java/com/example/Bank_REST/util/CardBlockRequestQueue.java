package com.example.Bank_REST.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.example.Bank_REST.util.request.CardBlockRequest;

/**
 * An in-memory queue implementation for storing card block requests, 
 * providing methods for adding, retrieving, and removing requests.
 * 
 * This class serves as a cache for card block requests, allowing for efficient storage and retrieval of requests.
 */
@Component
public class CardBlockRequestQueue {

    private final Map<Long, CardBlockRequest> queue = new ConcurrentHashMap<>();

    private final AtomicLong requestId = new AtomicLong(0L);

    /**
     * Adds a new card block request to the queue.
     *
     * @param cardId    the unique identifier of the card to be blocked
     * @param userId    the unique identifier of the user requesting the block
     * @return          the newly added card block request
     */
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

    /**
     * Retrieves all card block requests from the queue.
     *
     * @return  a list of all card block requests in the queue
     */
    public List<CardBlockRequest> getAll() {
        return List.copyOf(queue.values());
    }

    /**
     * Retrieves a card block request by its unique identifier.
     *
     * @param id the unique identifier of the card block request
     * @return  the card block request associated with the given id, or an empty Optional if not found
     */
    public Optional<CardBlockRequest> get(Long id) {
        return Optional.ofNullable(queue.get(id));
    }

    /**
     * Removes a card block request from the queue by its unique identifier.
     *
     * @param id the unique identifier of the card block request to be removed
     */
    public void remove(Long id) {
        queue.remove(id);
    }
}
