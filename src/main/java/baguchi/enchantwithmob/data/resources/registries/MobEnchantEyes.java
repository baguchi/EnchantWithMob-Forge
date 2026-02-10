package baguchi.enchantwithmob.data.resources.registries;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantEye;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public class MobEnchantEyes {
    public static final ResourceKey<Registry<MobEnchantEye>> MOB_ENCHANT_EYE_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant_eye"));
    public static final ResourceKey<MobEnchantEye> BLAZE = createKey("blaze");
    public static final ResourceKey<MobEnchantEye> CREEPER = createKey("creeper");
    public static final ResourceKey<MobEnchantEye> EVOKER = createKey("evoker");
    public static final ResourceKey<MobEnchantEye> GUARDIAN = createKey("guardian");
    public static final ResourceKey<MobEnchantEye> PILLAGER = createKey("pillager");
    public static final ResourceKey<MobEnchantEye> SLIME = createKey("slime");
    public static final ResourceKey<MobEnchantEye> SPIDER = createKey("spider");
    public static final ResourceKey<MobEnchantEye> VINDICATOR = createKey("vindicator");
    public static final ResourceKey<MobEnchantEye> WITCH = createKey("witch");
    public static final ResourceKey<MobEnchantEye> ZOMBIE = createKey("zombie");

    public static final ResourceKey<MobEnchantEye> SKELETON = createKey("skeleton");
    public static final ResourceKey<MobEnchantEye> PARCHED = createKey("parched");

    private static ResourceKey<MobEnchantEye> createKey(String name) {
        return ResourceKey.create(MobEnchantEyes.MOB_ENCHANT_EYE_REGISTRY_KEY, Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, name));
    }

    public static void bootstrap(BootstrapContext<MobEnchantEye> context) {
        context.register(BLAZE, new MobEnchantEye(
                HolderSet.direct(EntityType.BLAZE.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_blaze_eyes.png")
        ));
        context.register(CREEPER, new MobEnchantEye(
                HolderSet.direct(EntityType.CREEPER.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_creeper_eyes.png")
        ));
        context.register(EVOKER, new MobEnchantEye(
                HolderSet.direct(EntityType.EVOKER.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_evoker_eyes.png")
        ));
        context.register(GUARDIAN, new MobEnchantEye(
                HolderSet.direct(EntityType.GUARDIAN.builtInRegistryHolder(), EntityType.ELDER_GUARDIAN.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_guardian_eyes.png")
        ));
        context.register(PILLAGER, new MobEnchantEye(
                HolderSet.direct(EntityType.PILLAGER.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_pillager_eyes.png")
        ));
        context.register(SLIME, new MobEnchantEye(
                HolderSet.direct(EntityType.SLIME.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_slime_eyes.png")
        ));
        context.register(SPIDER, new MobEnchantEye(
                HolderSet.direct(EntityType.SPIDER.builtInRegistryHolder(), EntityType.CAVE_SPIDER.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_spider_eyes.png")
        ));
        context.register(VINDICATOR, new MobEnchantEye(
                HolderSet.direct(EntityType.VINDICATOR.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_vindicator_eyes.png")
        ));
        context.register(WITCH, new MobEnchantEye(
                HolderSet.direct(EntityType.WITCH.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_witch_eyes.png")
        ));

        context.register(ZOMBIE, new MobEnchantEye(
                HolderSet.direct(EntityType.ZOMBIE.builtInRegistryHolder(), EntityType.HUSK.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_zombie_eyes.png")
        ));
        context.register(SKELETON, new MobEnchantEye(
                HolderSet.direct(EntityType.SKELETON.builtInRegistryHolder(), EntityType.STRAY.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_skeleton_eyes.png")
        ));
        context.register(PARCHED, new MobEnchantEye(
                HolderSet.direct(EntityType.PARCHED.builtInRegistryHolder()),
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_parched_eyes.png")
        ));
    }


    public static Optional<Holder.Reference<MobEnchantEye>> getEyeVariant(RegistryAccess p_332694_, EntityType<?> p_332773_) {
        Registry<MobEnchantEye> registry = p_332694_.lookupOrThrow(MOB_ENCHANT_EYE_REGISTRY_KEY);
        return registry.listElements()
                .filter(p_332674_ -> p_332674_.value().entityType().contains(p_332773_.builtInRegistryHolder()))
                .findFirst()
                .or(Optional::empty);
    }
}
