package baguchi.enchantwithmob.loot;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.ModItems;
import baguchi.enchantwithmob.registry.ModLootItemFunctions;
import baguchi.enchantwithmob.registry.ModRegistries;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class MobEnchantRandomlyFunction extends LootItemConditionalFunction {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<MobEnchantRandomlyFunction> CODEC = RecordCodecBuilder.mapCodec(
            p_344688_ -> commonFields(p_344688_)
                    .and(
                            p_344688_.group(
                                    RegistryCodecs.homogeneousList(ModRegistries.MOB_ENCHANT).optionalFieldOf("options").forGetter(p_344687_ -> p_344687_.options),
                                    Codec.BOOL.optionalFieldOf("only_compatible", Boolean.valueOf(true)).forGetter(p_344689_ -> p_344689_.onlyCompatible)
                            )
                    )
                    .apply(p_344688_, MobEnchantRandomlyFunction::new)
    );
    private final Optional<HolderSet<MobEnchant>> options;
    private final boolean onlyCompatible;

    public MobEnchantRandomlyFunction(List<LootItemCondition> conditons, Optional<HolderSet<MobEnchant>> options, boolean onlyCompatible) {
        super(conditons);
        this.options = options;
        this.onlyCompatible = onlyCompatible;
    }

    @Override
    public LootItemFunctionType<MobEnchantRandomlyFunction> getType() {
        return ModLootItemFunctions.MOB_ENCHANT_RANDOMLY_FUNCTION.get();
    }

    /**
     * Called to perform the actual action of this function, after conditions have been checked.
     */
    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        RandomSource randomsource = context.getRandom();
        boolean flag = stack.is(ModItems.MOB_ENCHANT_BOOK.get());
        boolean flag1 = !flag && this.onlyCompatible;
        Stream<Holder<MobEnchant>> stream = this.options
                .map(HolderSet::stream)
                .orElseGet(() -> context.getLevel().registryAccess().lookupOrThrow(ModRegistries.MOB_ENCHANT).listElements().map(Function.identity()))
                .filter(p_344686_ -> !flag1); // Neo: Respect IItemExtension#supportsMobEnchant
        List<Holder<MobEnchant>> list = stream.toList();
        Optional<Holder<MobEnchant>> optional = Util.getRandomSafe(list, randomsource);
        if (optional.isEmpty()) {
            LOGGER.warn("Couldn't find a compatible enchantment for {}", stack);
            return stack;
        } else {
            return enchantItem(stack, optional.get(), randomsource);
        }
    }

    private static ItemStack enchantItem(ItemStack stack, Holder<MobEnchant> enchantment, RandomSource random) {
        int i = Mth.nextInt(random, enchantment.value().getMinLevel(), enchantment.value().getMaxLevel());
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(ModItems.MOB_ENCHANT_BOOK.get());
        }

        MobEnchantUtils.enchant(enchantment, stack, i);
        return stack;
    }

    public static Builder randomMobEnchant() {
        return new Builder();
    }

    public static Builder randomApplicableMobEnchant(HolderLookup.Provider registries) {
        return randomMobEnchant().withOneOf(registries.lookupOrThrow(ModRegistries.MOB_ENCHANT).getOrThrow(ModTags.MobEnchantTags.RANDOM_LOOT));
    }

    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private Optional<HolderSet<MobEnchant>> options = Optional.empty();
        private boolean onlyCompatible = true;

        protected Builder getThis() {
            return this;
        }

        public Builder withMobEnchant(Holder<MobEnchant> enchantment) {
            this.options = Optional.of(HolderSet.direct(enchantment));
            return this;
        }

        public Builder withOneOf(HolderSet<MobEnchant> enchantments) {
            this.options = Optional.of(enchantments);
            return this;
        }

        public Builder allowingIncompatibleMobEnchants() {
            this.onlyCompatible = false;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new MobEnchantRandomlyFunction(this.getConditions(), this.options, this.onlyCompatible);
        }
    }
}
