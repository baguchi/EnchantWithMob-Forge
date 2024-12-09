package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import baguchi.enchantwithmob.mobenchant.effects.entity.MobEnchantAttributeEffect;
import baguchi.enchantwithmob.registry.ModMobEnchantDataCompnents;
import baguchi.enchantwithmob.registry.ModRegistries;
import baguchi.enchantwithmob.registry.ModTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public record MobEnchant(Component description, EnchantmentDefinition definition,
                         HolderSet<MobEnchant> exclusiveSet, DataComponentMap effects) {
    public static final int MAX_LEVEL = 255;
    public static final Codec<MobEnchant> DIRECT_CODEC = RecordCodecBuilder.create(
            p_344998_ -> p_344998_.group(
                            ComponentSerialization.CODEC.fieldOf("description").forGetter(MobEnchant::description),
                            EnchantmentDefinition.CODEC.forGetter(MobEnchant::definition),
                            RegistryCodecs.homogeneousList(ModRegistries.MOB_ENCHANT)
                                    .optionalFieldOf("exclusive_set", HolderSet.direct())
                                    .forGetter(MobEnchant::exclusiveSet),
                            ModMobEnchantDataCompnents.CODEC.optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(MobEnchant::effects)
                    )
                    .apply(p_344998_, MobEnchant::new)
    );
    public static final Codec<Holder<MobEnchant>> CODEC = RegistryFixedCodec.create(ModRegistries.MOB_ENCHANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MobEnchant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModRegistries.MOB_ENCHANT);

    public static Cost constantCost(int cost) {
        return new Cost(cost, 0);
    }

    public static Cost dynamicCost(int base, int perLevel) {
        return new Cost(base, perLevel);
    }


    public static EnchantmentDefinition definition(
            int weight,
            int maxLevel,
            Cost minCost,
            Cost maxCost,
            int anvilCost
    ) {
        return new EnchantmentDefinition(weight, maxLevel, minCost, maxCost, anvilCost);
    }

    public int getWeight() {
        return this.definition.weight();
    }

    public int getAnvilCost() {
        return this.definition.anvilCost();
    }

    public int getMinLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return this.definition.maxLevel();
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int level) {
        return this.definition.minCost().calculate(level);
    }

    public int getMaxCost(int level) {
        return this.definition.maxCost().calculate(level);
    }

    @Override
    public String toString() {
        return "Enchantment " + this.description.getString();
    }

    public static boolean areCompatible(Holder<MobEnchant> first, Holder<MobEnchant> second) {
        return !first.equals(second) && !first.value().exclusiveSet.contains(second) && !second.value().exclusiveSet.contains(first);
    }

    public static Component getFullname(Holder<MobEnchant> enchantment, int level) {
        MutableComponent mutablecomponent = enchantment.value().description.copy();
        if (enchantment.is(ModTags.MobEnchantTags.CURSE)) {
            ComponentUtils.mergeStyles(mutablecomponent, Style.EMPTY.withColor(ChatFormatting.RED));
        } else {
            ComponentUtils.mergeStyles(mutablecomponent, Style.EMPTY.withColor(ChatFormatting.GRAY));
        }

        if (level != 1 || enchantment.value().getMaxLevel() != 1) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level));
        }

        return mutablecomponent;
    }

    public <T> List<T> getEffects(DataComponentType<List<T>> component) {
        return this.effects.getOrDefault(component, List.of());
    }

    public boolean isImmuneToDamage(ServerLevel level, int enchantmentLevel, Entity entity, DamageSource damageSource) {
        LootContext lootcontext = damageContext(level, enchantmentLevel, entity, damageSource);

        for (ConditionalEffect<DamageImmunity> conditionaleffect : this.getEffects(ModMobEnchantDataCompnents.DAMAGE_IMMUNITY.get())) {
            if (conditionaleffect.matches(lootcontext)) {
                return true;
            }
        }

        return false;
    }

    public void modifyDamageProtection(
            ServerLevel level, int enchantmentLevel, Entity entity, DamageSource damageSource, MutableFloat damageProtection
    ) {
        LootContext lootcontext = damageContext(level, enchantmentLevel, entity, damageSource);

        for (ConditionalEffect<EnchantmentValueEffect> conditionaleffect : this.getEffects(ModMobEnchantDataCompnents.DAMAGE_PROTECTION.get())) {
            if (conditionaleffect.matches(lootcontext)) {
                damageProtection.setValue(conditionaleffect.effect().process(enchantmentLevel, entity.getRandom(), damageProtection.floatValue()));
            }
        }
    }

    public void modifyDamage(ServerLevel level, int enchantmentLevel, Entity entity, DamageSource damageSource, MutableFloat damage) {
        this.modifyDamageFilteredValue(ModMobEnchantDataCompnents.DAMAGE.get(), level, enchantmentLevel, entity, damageSource, damage);
    }

    public void modifyKnockback(ServerLevel level, int enchantmentLevel, Entity entity, DamageSource damageSource, MutableFloat knockback) {
        this.modifyDamageFilteredValue(ModMobEnchantDataCompnents.KNOCKBACK.value(), level, enchantmentLevel, entity, damageSource, knockback);
    }

    public void modifyArmorEffectivness(
            ServerLevel level, int enchantmentLevel, Entity entity, DamageSource damageSource, MutableFloat armorEffectiveness
    ) {
        this.modifyDamageFilteredValue(ModMobEnchantDataCompnents.ARMOR_EFFECTIVENESS.get(), level, enchantmentLevel, entity, damageSource, armorEffectiveness);
    }

    public void doPostAttack(
            ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, EnchantmentTarget target, Entity entity, DamageSource damageSource
    ) {
        for (TargetedConditionalEffect<MobEnchantEntityEffect> targetedconditionaleffect : this.getEffects(ModMobEnchantDataCompnents.POST_ATTACK.get())) {
            if (target == targetedconditionaleffect.enchanted()) {
                doPostAttack(targetedconditionaleffect, level, enchantmentLevel, owner, entity, damageSource);
            }
        }
    }

    public static void doPostAttack(
            TargetedConditionalEffect<MobEnchantEntityEffect> effect,
            ServerLevel level,
            int enchantmentLevel,
            @Nullable LivingEntity owner,
            Entity p_entity,
            DamageSource damageSource
    ) {
        if (effect.matches(damageContext(level, enchantmentLevel, p_entity, damageSource))) {
            Entity entity = switch (effect.affected()) {
                case ATTACKER -> damageSource.getEntity();
                case DAMAGING_ENTITY -> damageSource.getDirectEntity();
                case VICTIM -> p_entity;
            };
            if (entity != null) {
                effect.effect().apply(level, enchantmentLevel, owner, entity, entity.position());
            }
        }
    }

    public void modifyUnfilteredValue(DataComponentType<EnchantmentValueEffect> componentType, RandomSource random, int enchantmentLevel, MutableFloat value) {
        EnchantmentValueEffect enchantmentvalueeffect = this.effects.get(componentType);
        if (enchantmentvalueeffect != null) {
            value.setValue(enchantmentvalueeffect.process(enchantmentLevel, random, value.floatValue()));
        }
    }

    public void tick(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity) {
        applyEffects(
                this.getEffects(ModMobEnchantDataCompnents.TICK.get()),
                entityContext(level, enchantmentLevel, entity, entity.position()),
                p_345592_ -> p_345592_.apply(level, enchantmentLevel, owner, entity, entity.position())
        );
    }

    public void modifyItemFilteredCount(
            DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType,
            ServerLevel level,
            int enchantmentLevel,
            MutableFloat value
    ) {
        applyEffects(
                this.getEffects(componentType),
                ownerContext(level, enchantmentLevel),
                p_379237_ -> value.setValue(p_379237_.process(enchantmentLevel, level.getRandom(), value.getValue()))
        );
    }

    public void modifyEntityFilteredValue(
            DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType,
            ServerLevel level,
            int enchantmentLevel,
            ItemStack tool,
            Entity entity,
            MutableFloat value
    ) {
        applyEffects(
                this.getEffects(componentType),
                entityContext(level, enchantmentLevel, entity, entity.position()),
                p_347312_ -> value.setValue(p_347312_.process(enchantmentLevel, entity.getRandom(), value.floatValue()))
        );
    }

    public void modifyDamageFilteredValue(
            DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType,
            ServerLevel level,
            int enchantmentLevel,
            Entity entity,
            DamageSource damageSource,
            MutableFloat value
    ) {
        applyEffects(
                this.getEffects(componentType),
                damageContext(level, enchantmentLevel, entity, damageSource),
                p_347304_ -> value.setValue(p_347304_.process(enchantmentLevel, entity.getRandom(), value.floatValue()))
        );
    }

    public static LootContext damageContext(ServerLevel level, int enchantmentLevel, Entity entity, DamageSource damageSource) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
                .create(LootContextParamSets.ENCHANTED_DAMAGE);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext ownerContext(ServerLevel level, int enchantmentLevel) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
                .create(LootContextParamSets.ENCHANTED_ITEM);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext locationContext(ServerLevel level, int enchantmentLevel, Entity entity, boolean enchantmentActive) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.ENCHANTMENT_ACTIVE, enchantmentActive)
                .create(LootContextParamSets.ENCHANTED_LOCATION);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext entityContext(ServerLevel level, int enchantmentLevel, Entity entity, Vec3 origin) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
                .withParameter(LootContextParams.ORIGIN, origin)
                .create(LootContextParamSets.ENCHANTED_ENTITY);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static LootContext blockHitContext(ServerLevel level, int enchantmentLevel, Entity entity, Vec3 origin, BlockState state) {
        LootParams lootparams = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ENCHANTMENT_LEVEL, enchantmentLevel)
                .withParameter(LootContextParams.ORIGIN, origin)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.HIT_BLOCK);
        return new LootContext.Builder(lootparams).create(Optional.empty());
    }

    public static <T> void applyEffects(List<ConditionalEffect<T>> effects, LootContext context, Consumer<T> applier) {
        for (ConditionalEffect<T> conditionaleffect : effects) {
            if (conditionaleffect.matches(context)) {
                applier.accept(conditionaleffect.effect());
            }
        }
    }

    public void runLocationChangedEffects(MobEnchant changeMobEnchant, ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, LivingEntity entity) {
        for (MobEnchantAttributeEffect conditionaleffect : changeMobEnchant.getEffects(ModMobEnchantDataCompnents.ATTRIBUTES.get())) {
            conditionaleffect.onChangedBlock(level, enchantmentLevel, owner, entity, entity.position(), true);
        }

    }

    public void stopLocationBasedEffects(MobEnchant removedMobEnchant, int enchantmentLevel, @Nullable LivingEntity owner, LivingEntity entity) {
        for (MobEnchantAttributeEffect conditionaleffect : removedMobEnchant.getEffects(ModMobEnchantDataCompnents.ATTRIBUTES.get())) {
            conditionaleffect.onDeactivated(owner, entity, entity.position(), enchantmentLevel);
        }

    }

    public static Builder enchantment(EnchantmentDefinition definition) {
        return new Builder(definition);
    }

//    TODO: Reimplement. Not sure if we want to patch EnchantmentDefinition or hack this in as an EnchantmentEffectComponent.
//    /**
//     * Is this enchantment allowed to be enchanted on books via Enchantment Table
//     * @return false to disable the vanilla feature
//     */
//    public boolean isAllowedOnBooks() {
//        return true;
//    }

    public static class Builder {
        private final EnchantmentDefinition definition;
        private HolderSet<MobEnchant> exclusiveSet = HolderSet.direct();
        private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();

        /**
         * Neo: Allow customizing or changing the {@link Component} created by the enchantment builder.
         */
        protected java.util.function.UnaryOperator<MutableComponent> nameFactory = java.util.function.UnaryOperator.identity();

        public Builder(EnchantmentDefinition definition) {
            this.definition = definition;
        }

        public Builder exclusiveWith(HolderSet<MobEnchant> exclusiveSet) {
            this.exclusiveSet = exclusiveSet;
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> componentType, E effect, LootItemCondition.Builder requirements) {
            this.getEffectsList(componentType).add(new ConditionalEffect<>(effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> componentType, E effect) {
            this.getEffectsList(componentType).add(new ConditionalEffect<>(effect, Optional.empty()));
            return this;
        }

        public <E> Builder withEffect(
                DataComponentType<List<TargetedConditionalEffect<E>>> componentType,
                EnchantmentTarget enchanted,
                EnchantmentTarget affected,
                E effect,
                LootItemCondition.Builder requirements
        ) {
            this.getEffectsList(componentType).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> Builder withEffect(
                DataComponentType<List<TargetedConditionalEffect<E>>> componentType, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect
        ) {
            this.getEffectsList(componentType).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty()));
            return this;
        }

        public Builder withEffect(DataComponentType<List<MobEnchantAttributeEffect>> componentType, MobEnchantAttributeEffect effect) {
            this.getEffectsList(componentType).add(effect);
            return this;
        }

        public <E> Builder withSpecialEffect(DataComponentType<E> component, E value) {
            this.effectMapBuilder.set(component, value);
            return this;
        }

        public Builder withEffect(DataComponentType<Unit> componentType) {
            this.effectMapBuilder.set(componentType, Unit.INSTANCE);
            return this;
        }

        /**
         * Allows specifying an operator that can customize the default {@link Component} created by {@link #build(ResourceLocation)}.
         *
         * @return this
         */
        public Builder withCustomName(java.util.function.UnaryOperator<MutableComponent> nameFactory) {
            this.nameFactory = nameFactory;
            return this;
        }

        private <E> List<E> getEffectsList(DataComponentType<List<E>> componentType) {
            return (List<E>) this.effectLists.computeIfAbsent(componentType, p_346247_ -> {
                ArrayList<E> arraylist = new ArrayList<>();
                this.effectMapBuilder.set(componentType, arraylist);
                return arraylist;
            });
        }

        public MobEnchant build(ResourceLocation location) {
            return new MobEnchant(
                    // Neo: permit custom name components instead of a single hardcoded translatable component.
                    this.nameFactory.apply(Component.translatable(Util.makeDescriptionId("mob_enchant", location))),
                    this.definition, this.exclusiveSet, this.effectMapBuilder.build()
            );
        }
    }

    public static record Cost(int base, int perLevelAboveFirst) {
        public static final Codec<Cost> CODEC = RecordCodecBuilder.create(
                p_345979_ -> p_345979_.group(
                                Codec.INT.fieldOf("base").forGetter(Cost::base),
                                Codec.INT.fieldOf("per_level_above_first").forGetter(Cost::perLevelAboveFirst)
                        )
                        .apply(p_345979_, Cost::new)
        );

        public int calculate(int level) {
            return this.base + this.perLevelAboveFirst * (level - 1);
        }
    }

    public static record EnchantmentDefinition(
            int weight,
            int maxLevel,
            Cost minCost,
            Cost maxCost,
            int anvilCost
    ) {
        public static final MapCodec<EnchantmentDefinition> CODEC = RecordCodecBuilder.mapCodec(
                p_344890_ -> p_344890_.group(
                                ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(EnchantmentDefinition::weight),
                                ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(EnchantmentDefinition::maxLevel),
                                Cost.CODEC.fieldOf("min_cost").forGetter(EnchantmentDefinition::minCost),
                                Cost.CODEC.fieldOf("max_cost").forGetter(EnchantmentDefinition::maxCost),
                                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anvil_cost").forGetter(EnchantmentDefinition::anvilCost)
                        )
                        .apply(p_344890_, EnchantmentDefinition::new)
        );
    }
}