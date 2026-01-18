package net.goastcraft.listeners;


import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.goastcraft.Main;
import net.goastcraft.data.StatData;

public class PlayerDisconnectListener {
    public static void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef player = event.getPlayerRef();
        StatData.PlayerData playerData = Main.getMain().getStatData().getTypeData(player.getUuid().toString()).getPlayerData();

        if (playerData == null) return;

        playerData.updateTimePlayed();
    }
}
