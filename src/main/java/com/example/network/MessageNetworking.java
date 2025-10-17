package com.example.network;

import com.example.ExampleMod;
import com.example.network.payload.MessagePayload;
import com.example.proto.MessageProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class MessageNetworking {
    private MessageNetworking() {
    }

    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(MessagePayload.ID, MessagePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(MessagePayload.ID, (payload, context) -> {
            MinecraftServer server = context.server();
            ServerPlayerEntity player = context.player();
            server.execute(() -> processIncomingMessage(player, payload));
        });
    }

    private static void processIncomingMessage(ServerPlayerEntity player, MessagePayload payload) {
        MessageProtos.Message message;
        try {
            message = MessageProtos.Message.parseFrom(payload.data());
        } catch (InvalidProtocolBufferException e) {
            ExampleMod.LOGGER.error("Failed to parse protobuf message from {}: {}", player.getGameProfile().getName(), e.getMessage());
            return;
        }

        String text = message.getText();
        if (text.isBlank()) {
            ExampleMod.LOGGER.warn("Received blank message from {}", player.getGameProfile().getName());
            return;
        }

        if (text.length() > 256) {
            ExampleMod.LOGGER.warn("Received message exceeding 256 characters from {}", player.getGameProfile().getName());
            return;
        }

        ExampleMod.LOGGER.info("Received message from {} ({}): {}", player.getGameProfile().getName(), player.getUuid(), text);
    }
}
