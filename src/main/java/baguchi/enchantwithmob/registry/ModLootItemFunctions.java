package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.loot.MobEnchantRandomlyFunction;
import baguchi.enchantwithmob.loot.MobEnchantWithLevelsFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootItemFunctions {
	public static final DeferredRegister<LootItemFunctionType<?>> LOOT_REGISTRY = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE, EnchantWithMob.MODID);

	public static final Supplier<LootItemFunctionType> MOB_ENCHANT_WITH_LEVELS = LOOT_REGISTRY.register("mob_enchant_with_levels", () -> new LootItemFunctionType(MobEnchantWithLevelsFunction.CODEC));
	public static final Supplier<LootItemFunctionType> MOB_ENCHANT_RANDOMLY_FUNCTION = LOOT_REGISTRY.register("mob_enchant_randomly_function", () -> new LootItemFunctionType(MobEnchantRandomlyFunction.CODEC));
}
