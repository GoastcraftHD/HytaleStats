package net.goastcraft.listeners;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Arrays;

public class CraftRecipeListener extends EntityEventSystem<EntityStore, CraftRecipeEvent.Pre> {
    public CraftRecipeListener() {
        super(CraftRecipeEvent.Pre.class);
    }

    @Override
    public void handle(int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl CraftRecipeEvent.Pre event) {
        System.out.println("1");

        CraftingRecipe recipe = event.getCraftedRecipe();

        System.out.println("%s | %s | %s".formatted(recipe.getId(), Arrays.toString(recipe.getOutputs()), String.valueOf(recipe.getPrimaryOutput())));
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
