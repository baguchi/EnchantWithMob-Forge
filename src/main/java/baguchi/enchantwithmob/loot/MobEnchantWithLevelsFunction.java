package baguchi.enchantwithmob.loot;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Optional;

public class MobEnchantWithLevelsFunction extends LootItemConditionalFunction {
	public static final MapCodec<MobEnchantWithLevelsFunction> CODEC = RecordCodecBuilder.mapCodec(
			p_344692_ -> commonFields(p_344692_)
					.and(
							p_344692_.group(
									ContextIntProviders.CODEC.fieldOf("levels").forGetter(p_298844_ -> p_298844_.levels),
									RegistryCodecs.holderSet(MobEnchants.MOB_ENCHANT_REGISTRY).optionalFieldOf("options").forGetter(p_344691_ -> p_344691_.options)
							)
					)
					.apply(p_344692_, MobEnchantWithLevelsFunction::new)
	);
	private final Holder<ContextIntProvider> levels;
	private final Optional<HolderSet<MobEnchant>> options;

	MobEnchantWithLevelsFunction(Optional<Holder<LootItemCondition>> condtions, Holder<ContextIntProvider> levels, Optional<HolderSet<MobEnchant>> options) {
		super(condtions);
		this.levels = levels;
		this.options = options;
	}


	@Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
	}

	/**
	 * Called to perform the actual action of this function, after conditions have been checked.
	 */
	@Override
	public ItemStack run(ItemStack stack, LootContext context) {
		RandomSource randomsource = context.getRandom();
		RegistryAccess registryaccess = context.getLevel().registryAccess();
		return MobEnchantUtils.addRandomEnchantmentToItemStack(randomsource, registryaccess, stack, this.levels.value().getInt(context), this.options.map(HolderSet::stream)
				.orElseGet(() -> context.getLevel().registryAccess().lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).listElements().map(p_344499_ -> (Holder<MobEnchant>) p_344499_)));
	}

	public static MobEnchantWithLevelsFunction.Builder enchantWithLevels(HolderLookup.Provider registries, Holder<ContextIntProvider> levels) {
		return new MobEnchantWithLevelsFunction.Builder(levels)
				.fromOptions(registries.lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).getOrThrow(ModTags.MobEnchantTags.RANDOM_LOOT));
	}

	public static class Builder extends LootItemConditionalFunction.Builder<MobEnchantWithLevelsFunction.Builder> {
		private final Holder<ContextIntProvider> levels;
		private Optional<HolderSet<MobEnchant>> options = Optional.empty();

		public Builder(Holder<ContextIntProvider> levels) {
			this.levels = levels;
		}

		protected MobEnchantWithLevelsFunction.Builder getThis() {
			return this;
		}

		public MobEnchantWithLevelsFunction.Builder fromOptions(HolderSet<MobEnchant> options) {
			this.options = Optional.of(options);
			return this;
		}

		@Override
		public LootItemFunction build() {
			return new MobEnchantWithLevelsFunction(this.getCondition(), this.levels, this.options);
		}
	}
}