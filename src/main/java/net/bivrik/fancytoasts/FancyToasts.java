package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.config.FancyToastsConfig;
import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FancyToasts implements ClientModInitializer {
	public static final String MOD_ID = "fancytoasts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static FancyToastsConfig CONFIG;

	@Override
	public void onInitializeClient() {
		CONFIG = FancyToastsConfig.load();

		LOGGER.info(MOD_ID + " client is initialized.");
	}
}
