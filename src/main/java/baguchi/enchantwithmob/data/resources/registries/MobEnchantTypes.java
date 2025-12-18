package baguchi.enchantwithmob.data.resources.registries;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.client.ModParticles;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public class MobEnchantTypes {
    public static final ResourceKey<Registry<MobEnchantType>> MOB_ENCHANT_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant_type"));
    public static final ResourceKey<MobEnchantType> NORMAL = createKey("normal");

    public static final ResourceKey<MobEnchantType> ANCIENT = createKey("ancient");

    private static ResourceKey<MobEnchantType> createKey(String name) {
        return ResourceKey.create(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY, Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, name));
    }

    public static void bootstrap(BootstrapContext<MobEnchantType> context) {
        context.register(NORMAL, new MobEnchantType(
                ItemRenderer.ENCHANTED_GLINT_ARMOR,
                1F,
                Optional.of(ModParticles.ENCHANT.get())
        ));
        context.register(ANCIENT, new MobEnchantType(
                Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/ancient_glint.png"),
                3F,
                Optional.of(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER)
        ));
    }
}
