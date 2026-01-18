package net.goastcraft.listeners;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.goastcraft.Main;
import net.goastcraft.data.StatData;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Map;

public class UseBlockListener extends EntityEventSystem<EntityStore, UseBlockEvent.Pre> {
    public UseBlockListener() {
        super(UseBlockEvent.Pre.class);
    }

    @Override
    public void handle(int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl UseBlockEvent.Pre event) {
        Item item = event.getBlockType().getItem();

        Ref<EntityStore> playerRef = archetypeChunk.getReferenceTo(index);
        Player player = store.getComponent(playerRef, Player.getComponentType());
        if (player == null) return;

        Map<String, StatData.BlockData> saveData = Main.getMain().getStatData().getTypeData(player.getUuid().toString()).getBlockDataMap();

        if (saveData.containsKey(item.getBlockId())) {
            StatData.BlockData statData = saveData.get(item.getBlockId());
            statData.incrementUseCount();
        } else {
            StatData.BlockData statData = new StatData.BlockData(item.getTranslationKey(), item.getIcon());
            statData.incrementUseCount();
            saveData.put(item.getBlockId(), statData);
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
