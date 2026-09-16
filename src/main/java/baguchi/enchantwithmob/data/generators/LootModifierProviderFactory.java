package baguchi.enchantwithmob.data.generators;

import baguchi.bagus_lib.loot.OneItemLootModifier;
import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class LootModifierProviderFactory {

    public GlobalLootModifierProvider create(GatherDataEvent.Client event, CompletableFuture<HolderLookup.Provider> provider) {
        return new GlobalLootModifierProvider(
                event.getGenerator().getPackOutput(),
                provider,
                EnchantWithMob.MODID
        ) {
            @Override
            protected void start() {
                addTable(BuiltInLootTables.ANCIENT_CITY, prefix("inject/mob_enchant_ancient"), 0.35F);
                addOneItemTable(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, prefix("inject/archaeology_enchant_normal"), 0.15F);
                addTable(BuiltInLootTables.DESERT_PYRAMID, prefix("inject/mob_enchant_normal"), 0.25F);
                addTable(BuiltInLootTables.WOODLAND_MANSION, prefix("inject/mob_enchant_mansion"), 0.2F);
                addTable(BuiltInLootTables.STRONGHOLD_LIBRARY, prefix("inject/mob_enchant_normal"), 0.35F);
            }

            private void addTable(ResourceKey<LootTable> table, ResourceKey<LootTable> rewriteTable, float chance) {
                add(EnchantWithMob.prefix(table)
                        , new AddTableLootModifier(Optional.of(Holder.direct(LootTableIdCondition.builder(table.identifier()).and(LootItemRandomChanceCondition.randomChance(chance)).build())), IGlobalLootModifier.DEFAULT_PRIORITY, rewriteTable));
            }

            private void addOneItemTable(ResourceKey<LootTable> table, ResourceKey<LootTable> rewriteTable, float chance) {
                add(EnchantWithMob.prefix(table)
                        , new OneItemLootModifier(Optional.of(Holder.direct(LootTableIdCondition.builder(table.identifier()).and(LootItemRandomChanceCondition.randomChance(chance)).build())), IGlobalLootModifier.DEFAULT_PRIORITY, rewriteTable));
            }

        };
    }

    public static ResourceKey<LootTable> prefix(String key) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, key));
    }
}