package com.example.service;

import java.util.UUID;

public record PlayerMessage(UUID playerUuid, String text) {
}
