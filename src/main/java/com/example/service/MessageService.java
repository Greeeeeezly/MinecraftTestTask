package com.example.service;

import com.example.database.MessageRepository;

public class MessageService {
    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public void save(PlayerMessage message) {
        repository.save(message.playerUuid(), sanitize(message.text()));
    }

    private String sanitize(String text) {
        String trimmed = text.trim();
        if (trimmed.length() > 256) {
            return trimmed.substring(0, 256);
        }
        return trimmed;
    }
}
