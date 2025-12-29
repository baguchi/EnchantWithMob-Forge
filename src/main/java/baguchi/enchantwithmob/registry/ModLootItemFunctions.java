package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.loot.MobEnchantRandomlyFunction;
import baguchi.enchantwithmob.loot.MobEnchantWithLevelsFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootItemFunctions {
    public static final DeferredRegister<MapCodec<? extends LootItemFunction>> LOOT_REGISTRY = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE, EnchantWithMob.MODID);

    public static final Supplier<MapCodec<? extends LootItemFunction>> MOB_ENCHANT_WITH_LEVELS = LOOT_REGISTRY.register("mob_enchant_with_levels", () -> MobEnchantWithLevelsFunction.CODEC);
    public static final Supplier<MapCodec<? extends LootItemFunction>> MOB_ENCHANT_RANDOMLY_FUNCTION = LOOT_REGISTRY.register("mob_enchant_randomly_function", () -> MobEnchantRandomlyFunction.CODEC);
}
