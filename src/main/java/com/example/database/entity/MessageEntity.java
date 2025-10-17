package com.example.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, columnDefinition = "uuid")
    private UUID playerUuid;

    @Column(name = "text", nullable = false, length = 256)
    private String text;

    protected MessageEntity() {
    }

    public MessageEntity(UUID playerUuid, String text) {
        this.playerUuid = playerUuid;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getText() {
        return text;
    }
}
