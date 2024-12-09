package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.mobenchant.effects.AllOf;
import baguchi.enchantwithmob.mobenchant.effects.entity.ApplyMobEffect;
import baguchi.enchantwithmob.mobenchant.effects.entity.DamageEntity;
import baguchi.enchantwithmob.mobenchant.effects.entity.MobEnchantAttributeEffect;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.EnchantmentLevelProvider;

public class MobEnchants {
    public static final ResourceKey<MobEnchant> STRONG = key("strong");
    public static final ResourceKey<MobEnchant> PROTECTION = key("protection");
    public static final ResourceKey<MobEnchant> HEALTH_BOOST = key("health_boost");
    public static final ResourceKey<MobEnchant> TOUGH = key("tough");
    public static final ResourceKey<MobEnchant> MULTISHOT = key("multishot");
    public static final ResourceKey<MobEnchant> THORN = key("thorn");
    public static final ResourceKey<MobEnchant> WIND = key("wind");
    public static final ResourceKey<MobEnchant> SPEEDY = key("speedy");
    public static final ResourceKey<MobEnchant> DEFLECT = key("deflect");
    public static final ResourceKey<MobEnchant> POISON = key("poison");
    public static final ResourceKey<MobEnchant> POISON_CLOUD = key("poison_cloud");
    public static final ResourceKey<MobEnchant> SOUL_STEAL = key("soul_steal");

    private static ResourceKey<MobEnchant> key(String name) {
        return ResourceKey.create(ModRegistries.MOB_ENCHANT, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, name));
    }

    public static void bootstrap(BootstrapContext<MobEnchant> context) {
        HolderGetter<DamageType> holdergetter = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<MobEnchant> holdergetter1 = context.lookup(ModRegistries.MOB_ENCHANT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);
        HolderGetter<Block> holdergetter3 = context.lookup(Registries.BLOCK);
        HolderGetter<EntityType<?>> holdergetter4 = context.lookup(Registries.ENTITY_TYPE);
        register(
                context,
                PROTECTION,
                MobEnchant.enchantment(
                                MobEnchant.definition(
                                        10,
                                        4,
                                        MobEnchant.dynamicCost(1, 11),
                                        MobEnchant.dynamicCost(12, 11),
                                        1
                                )
                        )
                        .withEffect(
                                ModMobEnchantDataCompnents.DAMAGE_PROTECTION.get(),
                                new AddValue(LevelBasedValue.perLevel(1.0F)),
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType().tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                )
                        )
        );
        register(
                context,
                STRONG,
                MobEnchant.enchantment(
                                MobEnchant.definition(
                                        10,
                                        5,
                                        MobEnchant.dynamicCost(1, 11),
                                        MobEnchant.dynamicCost(21, 11),
                                        1
                                )
                        )
                        .withEffect(ModMobEnchantDataCompnents.DAMAGE.get(), new AddValue(LevelBasedValue.perLevel(1.0F, 0.5F)))
        );
        register(
                context,
                HEALTH_BOOST,
                MobEnchant.enchantment(
                                MobEnchant.definition(
                                        10,
                                        10,
                                        MobEnchant.dynamicCost(5, 5),
                                        MobEnchant.dynamicCost(22, 5),
                                        1
                                )
                        )
                        .withEffect(
                                ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                                new MobEnchantAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "health"),
                                        Attributes.MAX_HEALTH,
                                        LevelBasedValue.perLevel(2F),
                                        AttributeModifier.Operation.ADD_VALUE)
                        )
        );
        register(
                context,
                TOUGH,
                MobEnchant.enchantment(
                                MobEnchant.definition(
                                        5,
                                        5,
                                        MobEnchant.dynamicCost(5, 5),
                                        MobEnchant.dynamicCost(22, 5),
                                        2
                                )
                        )
                        .withEffect(
                                ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                                new MobEnchantAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "armor"),
                                        Attributes.ARMOR,
                                        LevelBasedValue.perLevel(2F),
                                        AttributeModifier.Operation.ADD_VALUE))
                        .withEffect(
                                ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                                new MobEnchantAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "tough"),
                                        Attributes.ARMOR_TOUGHNESS,
                                        LevelBasedValue.perLevel(0.5F),
                                        AttributeModifier.Operation.ADD_VALUE)));
        register(
                context,
                MULTISHOT,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                2,
                                1,
                                MobEnchant.dynamicCost(10, 5),
                                MobEnchant.constantCost(50),
                                3
                        )
                )
        );
        register(
                context,
                THORN,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                2,
                                2,
                                MobEnchant.dynamicCost(10, 20),
                                MobEnchant.dynamicCost(60, 20),
                                3
                        )
                ).withEffect(
                        ModMobEnchantDataCompnents.POST_ATTACK.get(),
                        EnchantmentTarget.VICTIM,
                        EnchantmentTarget.ATTACKER,
                        AllOf.entityEffects(
                                new DamageEntity(LevelBasedValue.constant(1.0F), LevelBasedValue.constant(5.0F), holdergetter.getOrThrow(DamageTypes.THORNS))
                        ),
                        LootItemRandomChanceCondition.randomChance(EnchantmentLevelProvider.forEnchantmentLevel(LevelBasedValue.perLevel(0.15F)))
                )
        );

        register(
                context,
                WIND,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                1,
                                1,
                                MobEnchant.dynamicCost(10, 5),
                                MobEnchant.constantCost(50),
                                4
                        )
                ).withEffect(
                        ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                        new MobEnchantAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "fall"),
                                Attributes.SAFE_FALL_DISTANCE,
                                LevelBasedValue.perLevel(4F),
                                AttributeModifier.Operation.ADD_VALUE))
        );
        register(
                context,
                SPEEDY,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                5,
                                2,
                                MobEnchant.dynamicCost(10, 5),
                                MobEnchant.dynamicCost(30, 5),
                                3
                        )
                ).withEffect(
                        ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                        new MobEnchantAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "speed"),
                                Attributes.MOVEMENT_SPEED,
                                LevelBasedValue.perLevel(0.05F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
        );
        register(
                context,
                DEFLECT,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                2,
                                1,
                                MobEnchant.dynamicCost(15, 5),
                                MobEnchant.constantCost(60),
                                3
                        )
                )
        );

        register(
                context,
                POISON,
                MobEnchant.enchantment(
                                MobEnchant.definition(
                                        1,
                                        2,
                                        MobEnchant.dynamicCost(15, 5),
                                        MobEnchant.dynamicCost(35, 5),
                                        3
                                )
                        ).exclusiveWith(holdergetter1.getOrThrow(ModTags.MobEnchantTags.POISON))
                        .withEffect(ModMobEnchantDataCompnents.POST_ATTACK.get(),
                                EnchantmentTarget.ATTACKER,
                                EnchantmentTarget.VICTIM,
                                new ApplyMobEffect(
                                        HolderSet.direct(MobEffects.POISON),
                                        LevelBasedValue.constant(1.5F),
                                        LevelBasedValue.perLevel(1.5F, 0.5F),
                                        LevelBasedValue.constant(3.0F),
                                        LevelBasedValue.constant(3.0F)
                                )
                        )
        );
        register(
                context,
                POISON_CLOUD,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                1,
                                2,
                                MobEnchant.dynamicCost(15, 5),
                                MobEnchant.dynamicCost(35, 5),
                                3
                        )
                ).exclusiveWith(holdergetter1.getOrThrow(ModTags.MobEnchantTags.POISON))
        );
        register(
                context,
                SOUL_STEAL,
                MobEnchant.enchantment(
                        MobEnchant.definition(
                                1,
                                2,
                                MobEnchant.dynamicCost(15, 5),
                                MobEnchant.dynamicCost(60, 5),
                                3
                        )
                )
        );
    }

    private static void register(BootstrapContext<MobEnchant> context, ResourceKey<MobEnchant> key, MobEnchant.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}