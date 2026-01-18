package net.goastcraft.listeners;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import net.goastcraft.Main;
import net.goastcraft.data.StatData;

import java.time.Instant;

public class PlayerReadyListener {
    public static void onPlayerReady(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        StatData.TypeData typeData = Main.getMain().getStatData().getTypeData(player.getUuid().toString());
        StatData.PlayerData playerData = typeData.getPlayerData();

        if (playerData == null) {
            playerData = new StatData.PlayerData(Instant.now());
            playerData.setLastJoinDate(Instant.now());
            typeData.setPlayerData(playerData);
        } else {
            playerData.setLastJoinDate(Instant.now());
        }
    }
}
