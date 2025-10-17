package com.example.network.client;

import com.example.ExampleMod;
import com.example.network.payload.MessagePayload;
import com.example.proto.MessageProtos;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public final class MessageClientNetworking {
    private MessageClientNetworking() {
    }

    public static void sendToServer(String text) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            ExampleMod.LOGGER.warn("Cannot send message: client or player not available");
            return;
        }

        MessageProtos.Message message = MessageProtos.Message.newBuilder()
            .setText(text)
            .build();

        MessagePayload payload = new MessagePayload(message.toByteArray());
        ClientPlayNetworking.send(payload);

        ExampleMod.LOGGER.info("Client [{}] sent message: {}", client.player.getGameProfile().getName(), text);
    }
}
