package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.effects.AllOf;
import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import baguchi.enchantwithmob.mobenchant.effects.entity.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobEnchantEntityEffects {
    public static final DeferredRegister<MapCodec<? extends MobEnchantEntityEffect>> ENTITY_EFFECT = DeferredRegister.create(ModRegistries.MOB_ENCHANT_ENTITY_EFFECT_REGISTRY_KEY, EnchantWithMob.MODID);

    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<AllOf.EntityEffects>> LOCATION_BASED = ENTITY_EFFECT.register("all_of", () -> AllOf.EntityEffects.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<ApplyMobEffect>> APPLY_MOB_EFFECT = ENTITY_EFFECT.register("apply_mob_effect", () -> ApplyMobEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<DamageEntity>> DAMAGE = ENTITY_EFFECT.register("damage", () -> DamageEntity.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<ExplodeEffect>> EXPLODE = ENTITY_EFFECT.register("explode", () -> ExplodeEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<Ignite>> IGNITE = ENTITY_EFFECT.register("ignite", () -> Ignite.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<Freeze>> FREEZE = ENTITY_EFFECT.register("freeze", () -> Freeze.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<PlaySoundEffect>> PLAY_SOUND = ENTITY_EFFECT.register("play_sound", () -> PlaySoundEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<RunFunction>> RUN_FUNCTION = ENTITY_EFFECT.register("run_function", () -> RunFunction.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantEntityEffect>, MapCodec<SpawnParticlesEffect>> SPAWN_PARTICLES = ENTITY_EFFECT.register("spawn_particles", () -> SpawnParticlesEffect.CODEC);

    private static Registry<MapCodec<? extends MobEnchantEntityEffect>> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(ModRegistries.MOB_ENCHANT_ENTITY_EFFECT_REGISTRY_KEY).sync(true));
    }

    public static Registry<MapCodec<? extends MobEnchantEntityEffect>> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}