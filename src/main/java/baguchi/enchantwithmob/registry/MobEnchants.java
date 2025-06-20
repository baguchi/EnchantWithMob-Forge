package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MobEnchants {
    public static final ResourceKey<Registry<MobEnchant>> MOB_ENCHANT_REGISTRY = createRegistryKey(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant"));

    public static final DeferredRegister<MobEnchant> MOB_ENCHANT = DeferredRegister.create(MOB_ENCHANT_REGISTRY, EnchantWithMob.MODID);
    public static final DeferredHolder<MobEnchant, MobEnchant> PROTECTION = MOB_ENCHANT.register("protection", () -> new ProtectionMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4, 1)));
    public static final DeferredHolder<MobEnchant, MobEnchant> TOUGH = MOB_ENCHANT.register("tough", () -> new ToughMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2, 4)).addAttributesModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.tough.armor"), (double) 2.0F, AttributeModifier.Operation.ADD_VALUE).addAttributesModifier(Attributes.ARMOR_TOUGHNESS, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.tough.toughness"), (double) 0.5F, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEnchant, MobEnchant> SPEEDY = MOB_ENCHANT.register("speedy", () -> new SpeedyMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.UNCOMMON, 2, 2)).addAttributesModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.speedy"), (double) 0.08F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEnchant, MobEnchant> STRONG = MOB_ENCHANT.register("strong", () -> new StrongMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3, 1)));
    public static final DeferredHolder<MobEnchant, MobEnchant> THORN = MOB_ENCHANT.register("thorn", () -> new ThornEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 3, 4)));
    public static final DeferredHolder<MobEnchant, MobEnchant> HEALTH_BOOST = MOB_ENCHANT.register("health_boost", () -> new HealthBoostMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 5, 5)).addAttributesModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.health_boost"), 2.0D, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEnchant, MobEnchant> POISON = MOB_ENCHANT.register("poison", () -> new PoisonMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 3, 3)));
    public static final DeferredHolder<MobEnchant, MobEnchant> POISON_CLOUD = MOB_ENCHANT.register("poison_cloud", () -> new PoisonCloudMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 2, 3)));
    public static final DeferredHolder<MobEnchant, MobEnchant> MULTISHOT = MOB_ENCHANT.register("multishot", () -> new MultiShotMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 1, 3)));
    public static final DeferredHolder<MobEnchant, MobEnchant> WIND = MOB_ENCHANT.register("wind", () -> new WindMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 1, 4)));

    public static final DeferredHolder<MobEnchant, MobEnchant> SOUL_STEAL = MOB_ENCHANT.register("soul_steal", () -> new SoulStealMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2, 4)));
    public static final DeferredHolder<MobEnchant, MobEnchant> DEFLECT = MOB_ENCHANT.register("deflect", () -> new DeflectMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 1, 4)));
    public static final DeferredHolder<MobEnchant, MobEnchant> SMALL = MOB_ENCHANT.register("small", () -> new SmallMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2, 4)).addAttributesModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.small.health"), -0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributesModifier(Attributes.SCALE, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.small.scale"), -0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEnchant, MobEnchant> HUGE = MOB_ENCHANT.register("huge", () -> new HugeMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2, 4)).addAttributesModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.huge.health"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL).addAttributesModifier(Attributes.SCALE, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant.huge.scale"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    private static Registry<MobEnchant> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(MOB_ENCHANT_REGISTRY).sync(true));
    }

    public static Registry<MobEnchant> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}