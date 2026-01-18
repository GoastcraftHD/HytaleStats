package net.goastcraft;

import com.google.gson.Gson;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.goastcraft.commands.ShowStats;
import net.goastcraft.data.StatData;
import net.goastcraft.listeners.*;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Main main;
    private StatData statData = new StatData();

    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
        main = this;
    }

    @Override
    protected void setup() {
        super.setup();

        Path dataPath = getDataDirectory().resolve("Data.json");
        if (Files.exists(dataPath)) {

            try {
                Gson gson = new Gson();
                statData = gson.fromJson(Files.readString(dataPath), StatData.class);
            } catch (IOException e) {
                getLogger().at(Level.WARNING).log("Could not load stats file (This can happen when the plugin is newly added)");
            }
        }

        this.getCommandRegistry().registerCommand(new ShowStats("Stats", "Shows all stats", false));

        this.getEntityStoreRegistry().registerSystem(new EntityKillListener());
        this.getEntityStoreRegistry().registerSystem(new BlockBreakListener());
        this.getEntityStoreRegistry().registerSystem(new BlockPlaceListener());
        this.getEntityStoreRegistry().registerSystem(new CraftRecipeListener());
        this.getEntityStoreRegistry().registerSystem(new DropItemListener());
        this.getEntityStoreRegistry().registerSystem(new UseBlockListener());

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerReadyListener::onPlayerReady);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, PlayerDisconnectListener::onPlayerDisconnect);
    }

    @Override
    protected void shutdown() {
        super.shutdown();

        Gson gson = new Gson();
        try {
            Path dataDir = getDataDirectory();

            Files.createDirectories(dataDir);
            Files.writeString(dataDir.resolve("Data.json"), gson.toJson(statData));
        } catch (IOException e) {
            getLogger().at(Level.SEVERE).log("Failed to save stats file!");
        }
    }


    public static Main getMain() {
        return main;
    }

    public StatData getStatData() {
        return statData;
    }
}
