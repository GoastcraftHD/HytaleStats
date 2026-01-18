package net.goastcraft.pages;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.goastcraft.Main;
import net.goastcraft.data.StatData;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class StatsPage extends InteractiveCustomUIPage<StatsPage.StatsData> {

    @FunctionalInterface
    interface GridAction<T> {
        void accept(UICommandBuilder cmd, int x, int y, String key, T value);
    }

    public static class StatsData {
        public static final BuilderCodec<StatsData> CODEC =
                BuilderCodec.builder(StatsData.class, StatsData::new).build();
    }

    public StatsPage(@NonNullDecl PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, StatsData.CODEC);
    }

    private <T> void addStatsGrid(UICommandBuilder cmd, String gridName, Map<String, T> statMap, GridAction<T> action) {
        int x = 0;
        int y = 0;
        for (Map.Entry<String, T> entry : statMap.entrySet()) {

            if (x == 0) {
                cmd.appendInline(gridName, "Group { LayoutMode: Left; }");
            }

            cmd.append(gridName + "[" + y + "]", "Pages/StatEntryPage.ui");
            action.accept(cmd, x, y,  entry.getKey(), entry.getValue());

            x++;
            if (x >= 3) {
                x = 0;
                y++;
            }
        }

        if (statMap.isEmpty()) {
            cmd.appendInline(gridName, "Label #StatName { Style: (FontSize: 25, TextColor: #ffffff); Padding: 10; Text: \"Nothing here yet\";}");
        }
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder cmd, @NonNullDecl UIEventBuilder event, @NonNullDecl Store<EntityStore> store) {
        cmd.append("Pages/StatsPage.ui");

        StatData.TypeData typeData = Main.getMain().getStatData().getTypeData(playerRef.getUuid().toString());

        addStatsGrid(cmd, "#EntityStatsGrid", typeData.getEntityDataMap(), (builder, x, y, key, value) -> {
            cmd.set("#EntityStatsGrid[" + y + "][" + x + "] #StatName.Text", Message.translation(key).getAnsiMessage());
            cmd.set("#EntityStatsGrid[" + y + "][" + x + "] #Image.Background", "Pages/Memories/npcs/" + value.getName() + ".png");

            cmd.set("#EntityStatsGrid[" + y + "][" + x + "] #StatContainer.TooltipText",
                    ("Killed: %d\n" +
                     "Died to: %d").formatted(value.getKillCount(), value.getDeathCount()));
        });

        addStatsGrid(cmd, "#BlockStatsGrid", typeData.getBlockDataMap(), (builder, x, y, key, value) -> {
            cmd.set("#BlockStatsGrid[" + y + "][" + x + "] #StatName.Text", Message.translation(value.getName()).getAnsiMessage());
            cmd.set("#BlockStatsGrid[" + y + "][" + x + "] #Image.Background", value.getIcon());

            cmd.set("#BlockStatsGrid[" + y + "][" + x + "] #StatContainer.TooltipText",
                    ("Placed: %d\n" +
                     "Broken: %d\n" +
                     "Dropped: %d\n" +
                     "Used: %d").formatted(value.getPlaceCount(), value.getBreakCount(), value.getDropCount(), value.getUseCount()));
        });

        StatData.PlayerData playerData = typeData.getPlayerData();
        double timePlayed = playerData.getTimePlayed() + Duration.between(playerData.getLastJoinDate(), Instant.now()).toSeconds();
        String timePlayedTimeEnd = "s";

        // Convert to minutes
        if (timePlayed >= 60d) {
            timePlayed = timePlayed / 60d;
            timePlayedTimeEnd = "m";
        }

        // Convert to hours
        if (timePlayed >= 60d) {
            timePlayed = timePlayed / 60d;
            timePlayedTimeEnd = "h";
        }

        cmd.set("#TimePlayed.Text", "Time played: %.2f%s".formatted(timePlayed, timePlayedTimeEnd));
    }

    @Override
    public void handleDataEvent(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store, @NonNullDecl StatsData data) {
    }
}
