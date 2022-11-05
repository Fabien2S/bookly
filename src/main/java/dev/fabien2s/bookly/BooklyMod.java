package dev.fabien2s.bookly;

import dev.fabien2s.bookly.command.HornCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooklyMod implements ModInitializer {

    public static final String ID = "bookly";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            HornCommand.register(dispatcher);
        });
    }

}
