package com.example;

import com.example.database.DatabaseManager;
import com.example.network.MessageNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	public static final String MOD_ID = "modid";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MessageNetworking.initialize();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> DatabaseManager.initialize());
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> DatabaseManager.shutdown());

		LOGGER.info("Hello Fabric world!");
	}
}
