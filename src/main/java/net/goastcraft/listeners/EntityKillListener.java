package net.goastcraft.listeners;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import net.goastcraft.Main;
import net.goastcraft.data.StatData;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.HashMap;
import java.util.Map;

public class EntityKillListener extends EntityEventSystem<EntityStore, Damage> {
    public EntityKillListener() {
        super(Damage.class);
    }

    @Override
    public void handle(int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl Damage event) {
        EntityStatMap entityStatMapComponent = archetypeChunk.getComponent(index, EntityStatMap.getComponentType());
        if (entityStatMapComponent == null) return;

        EntityStatValue healthStat = entityStatMapComponent.get(DefaultEntityStatTypes.getHealth());
        if (healthStat == null) return;
        if (healthStat.get() != 0) return;

        Ref<EntityStore> entityStoreRef = archetypeChunk.getReferenceTo(index);
        NPCEntity npc = commandBuffer.getComponent(entityStoreRef, NPCEntity.getComponentType());
        if (npc != null) {
            handleNPC(npc, event, store);
        }

        Player player = commandBuffer.getComponent(entityStoreRef, Player.getComponentType());
        if (player != null) {
            handlePlayer(player, event, store);
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

    private void handleNPC(NPCEntity npc, Damage event, Store<EntityStore> store) {
        Role role = npc.getRole();

        if (event.getSource() instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> sourceRef = entitySource.getRef();
            if (!sourceRef.isValid()) return;

            Player player = store.getComponent(sourceRef, Player.getComponentType());
            if (player == null) return;

            String playerUUID = player.getUuid().toString(); // TODO: Use newer method to obtain uuid
            Map<String, StatData.EntityData> saveData = Main.getMain().getStatData().getTypeData(playerUUID).getEntityDataMap();

            if (saveData.containsKey(role.getNameTranslationKey())) {
                StatData.EntityData statCount = saveData.get(role.getNameTranslationKey());
                statCount.incrementKillCount();
                saveData.put(role.getNameTranslationKey(), statCount);
            } else {
                StatData.EntityData statCount = new StatData.EntityData(role.getRoleName());
                statCount.incrementKillCount();
                saveData.put(role.getNameTranslationKey(), statCount);
            }
        }
    }

    private void handlePlayer(Player player, Damage event, Store<EntityStore> store) {
        if (event.getSource() instanceof Damage.EntitySource entitySource) {
            Ref<EntityStore> sourceRef = entitySource.getRef();
            if (!sourceRef.isValid()) return;

            NPCEntity npc = store.getComponent(sourceRef, NPCEntity.getComponentType());
            if (npc == null) return;

            Role role = npc.getRole();

            String playerUUID = player.getUuid().toString();
            Map<String, StatData.EntityData> saveData = Main.getMain().getStatData().getTypeData(playerUUID).getEntityDataMap();

            if (saveData.containsKey(role.getNameTranslationKey())) {
                StatData.EntityData statCount = saveData.get(role.getNameTranslationKey());
                statCount.incrementDeathCount();
                saveData.put(role.getNameTranslationKey(), statCount);
            } else {
                StatData.EntityData statCount = new StatData.EntityData(role.getRoleName());
                statCount.incrementDeathCount();
                saveData.put(role.getNameTranslationKey(), statCount);
            }
        }
    }
}
