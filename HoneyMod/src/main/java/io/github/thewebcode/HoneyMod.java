package io.github.thewebcode;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoneyMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("honeymod");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}