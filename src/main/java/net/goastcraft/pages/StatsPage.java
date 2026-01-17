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

import java.util.Map;

public class StatsPage extends InteractiveCustomUIPage<StatsPage.StatsData> {

    public static class StatsData {
        public static final BuilderCodec<StatsData> CODEC =
                BuilderCodec.builder(StatsData.class, StatsData::new).build();
    }

    public StatsPage(@NonNullDecl PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, StatsData.CODEC);
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder cmd, @NonNullDecl UIEventBuilder event, @NonNullDecl Store<EntityStore> store) {
        cmd.append("Pages/StatsPage.ui");

        StatData.TypeData typeData = Main.getMain().getStatData().getTypeData(playerRef.getUuid().toString());

        int x = 0;
        int y = 0;
        for (Map.Entry<String, StatData.EntityData> entry : typeData.getEntityDataMap().entrySet()) { // Hardcode ENTITY type for now

            StatData.EntityData statData = entry.getValue();

            if (x == 0) {
                cmd.appendInline("#StatsGrid", "Group { LayoutMode: Left; }");
            }

            cmd.append("#StatsGrid[" + y + "]", "Pages/StatEntryPage.ui");
            cmd.set("#StatsGrid[" + y + "][" + x + "] #StatName.Text", Message.translation(entry.getKey()).getAnsiMessage());
            cmd.set("#StatsGrid[" + y + "][" + x + "] #StatValue.Text", String.valueOf(statData.getCount()));
            cmd.set("#StatsGrid[" + y + "][" + x + "] #Image.Background", "Pages/Memories/npcs/" + statData.getName() + ".png");

            x++;
            if (x >= 3) {
                x = 0;
                y++;
            }
        }

        if (typeData.getEntityDataMap().isEmpty()) {
            cmd.appendInline("#StatsGrid", "Label #StatName { Style: (FontSize: 25, TextColor: #ffffff); Padding: 10; Text: \"Nothing here yet\";}");
        }
    }

    @Override
    public void handleDataEvent(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store, @NonNullDecl StatsData data) {
    }
}
