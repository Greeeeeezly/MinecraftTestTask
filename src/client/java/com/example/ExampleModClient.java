package com.example;

import com.example.client.gui.MessageScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
	private static final String KEY_CATEGORY = "category.modid.controls";
	private static final String KEY_OPEN_SCREEN = "key.modid.open_message_screen";

	private KeyBinding openScreenKey;

	@Override
	public void onInitializeClient() {
		openScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			KEY_OPEN_SCREEN,
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_M,
			KEY_CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(this::handleKeyBinding);
	}

	private void handleKeyBinding(MinecraftClient client) {
		while (openScreenKey.wasPressed()) {
			if (client.currentScreen == null) {
				client.setScreen(new MessageScreen());
			}
		}
	}
}
