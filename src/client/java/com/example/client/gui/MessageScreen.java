package com.example.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MessageScreen extends Screen {
    private static final int FIELD_WIDTH = 220;
    private static final int FIELD_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MAX_TEXT_LENGTH = 256;

    private TextFieldWidget inputField;
    private ButtonWidget sendButton;

    public MessageScreen() {
        super(Text.translatable("screen.modid.message.title"));
    }

    @Override
    protected void init() {
        if (client == null) {
            return;
        }

        int centerX = width / 2;
        int centerY = height / 2;

        inputField = new TextFieldWidget(
            textRenderer,
            centerX - FIELD_WIDTH / 2,
            centerY - FIELD_HEIGHT - 4,
            FIELD_WIDTH,
            FIELD_HEIGHT,
            Text.translatable("screen.modid.message.input")
        );
        inputField.setMaxLength(MAX_TEXT_LENGTH);
        inputField.setDrawsBackground(true);
        inputField.setFocused(true);
        inputField.setChangedListener(text -> updateButtonState());

        sendButton = ButtonWidget.builder(
            Text.literal("Send"),
            button -> sendMessage()
        ).dimensions(
            centerX - BUTTON_WIDTH / 2,
            centerY + 6,
            BUTTON_WIDTH,
            BUTTON_HEIGHT
        ).build();

        addDrawableChild(inputField);
        addDrawableChild(sendButton);
        setInitialFocus(inputField);
        updateButtonState();
    }

    private void sendMessage() {
        if (client == null) {
            return;
        }

        client.setScreen(null);

    }

    private void updateButtonState() {
        if (sendButton != null && inputField != null) {
            sendButton.active = !inputField.getText().isBlank();
        }
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (inputField != null && inputField.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (inputField != null && inputField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            if (sendButton != null && sendButton.active) {
                sendMessage();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
            textRenderer,
            title,
            width / 2,
            height / 2 - FIELD_HEIGHT - 20,
            0xFFFFFF
        );
    }
}
