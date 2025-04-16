package baguchi.enchantwithmob.loot;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModLootItemFunctions;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MobEnchantWithLevelsFunction extends LootItemConditionalFunction {
    public static final MapCodec<MobEnchantWithLevelsFunction> CODEC = RecordCodecBuilder.mapCodec(
            p_344692_ -> commonFields(p_344692_)
                    .and(
                            p_344692_.group(
                                    NumberProviders.CODEC.fieldOf("levels").forGetter(p_298844_ -> p_298844_.levels),
                                    RegistryCodecs.homogeneousList(MobEnchants.MOB_ENCHANT_REGISTRY).optionalFieldOf("options").forGetter(p_344691_ -> p_344691_.options)
                            )
                    )
                    .apply(p_344692_, MobEnchantWithLevelsFunction::new)
    );
    private final NumberProvider levels;
    private final Optional<HolderSet<MobEnchant>> options;

    MobEnchantWithLevelsFunction(List<LootItemCondition> condtions, NumberProvider levels, Optional<HolderSet<MobEnchant>> options) {
        super(condtions);
        this.levels = levels;
        this.options = options;
    }

    @Override
    public LootItemFunctionType<MobEnchantWithLevelsFunction> getType() {
        return ModLootItemFunctions.MOB_ENCHANT_WITH_LEVELS.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.levels.getReferencedContextParams();
    }

    /**
     * Called to perform the actual action of this function, after conditions have been checked.
     */
    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        RandomSource randomsource = context.getRandom();
        RegistryAccess registryaccess = context.getLevel().registryAccess();
        return MobEnchantUtils.addRandomEnchantmentToItemStack(randomsource, registryaccess, stack, this.levels.getInt(context), this.options.map(HolderSet::stream)
                .orElseGet(() -> context.getLevel().registryAccess().lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).listElements().map(p_344499_ -> (Holder<MobEnchant>) p_344499_)));
    }

    public static Builder enchantWithLevels(HolderLookup.Provider registries, NumberProvider levels) {
        return new Builder(levels)
                .fromOptions(registries.lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).getOrThrow(ModTags.MobEnchantTags.RANDOM_LOOT));
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final NumberProvider levels;
        private Optional<HolderSet<MobEnchant>> options = Optional.empty();

        public Builder(NumberProvider levels) {
            this.levels = levels;
        }

        protected Builder getThis() {
            return this;
        }

        public Builder fromOptions(HolderSet<MobEnchant> options) {
            this.options = Optional.of(options);
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new MobEnchantWithLevelsFunction(this.getConditions(), this.levels, this.options);
        }
    }
}