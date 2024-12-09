package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;

public class MobEnchants {
    public static final ResourceKey<MobEnchant> STRONG = key("strong");
    public static final ResourceKey<MobEnchant> PROTECTION = key("protection");
    public static final ResourceKey<MobEnchant> HEALTH_BOOST = key("health_boost");
    public static final ResourceKey<MobEnchant> TOUGH = key("tough");

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
                                        MobEnchant.dynamicCost(12, 5),
                                        1
                                )
                        )
                        .withEffect(
                                ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                                new EnchantmentAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "health"),
                                        Attributes.MAX_HEALTH,
                                        LevelBasedValue.perLevel(2F),
                                        AttributeModifier.Operation.ADD_VALUE))
        );
        register(
                context,
                TOUGH,
                MobEnchant.enchantment(
                                MobEnchant.definition(
                                        5,
                                        5,
                                        MobEnchant.dynamicCost(5, 5),
                                        MobEnchant.dynamicCost(12, 5),
                                        2
                                )
                        )
                        .withEffect(
                                ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                                new EnchantmentAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "armor"),
                                        Attributes.ARMOR,
                                        LevelBasedValue.perLevel(2F),
                                        AttributeModifier.Operation.ADD_VALUE))
                        .withEffect(
                                ModMobEnchantDataCompnents.ATTRIBUTES.get(),
                                new EnchantmentAttributeEffect(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "tough"),
                                        Attributes.ARMOR_TOUGHNESS,
                                        LevelBasedValue.perLevel(0.5F),
                                        AttributeModifier.Operation.ADD_VALUE))
        );
    }

    private static void register(BootstrapContext<MobEnchant> context, ResourceKey<MobEnchant> key, MobEnchant.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}