package com.example.network.payload;

import com.example.ExampleMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public final class MessagePayload implements CustomPayload {
    private static final int MAX_BYTES = 1024;

    public static final CustomPayload.Id<MessagePayload> ID = new CustomPayload.Id<>(Identifier.of(ExampleMod.MOD_ID, "message"));
    public static final PacketCodec<PacketByteBuf, MessagePayload> CODEC = PacketCodec.of(MessagePayload::encode, MessagePayload::decode);

    private final byte[] data;

    public MessagePayload(byte[] data) {
        this.data = data;
    }

    private static void encode(MessagePayload payload, PacketByteBuf buf) {
        buf.writeByteArray(payload.data);
    }

    private static MessagePayload decode(PacketByteBuf buf) {
        return new MessagePayload(buf.readByteArray(MAX_BYTES));
    }

    public byte[] data() {
        return data;
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
