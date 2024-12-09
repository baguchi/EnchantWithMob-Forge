package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.effects.AllOf;
import baguchi.enchantwithmob.mobenchant.effects.entity.*;
import baguchi.enchantwithmob.mobenchant.effects.location.MobEnchantLocationBasedEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobEnchantLocationBasedEffects {
    public static final DeferredRegister<MapCodec<? extends MobEnchantLocationBasedEffect>> LOCATION_BASED_EFFECT = DeferredRegister.create(ModRegistries.MOB_ENCHANT_LOCATION_BASED_EFFECT_REGISTRY_KEY, EnchantWithMob.MODID);

    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<AllOf.LocationBasedEffects>> LOCATION_BASED = LOCATION_BASED_EFFECT.register("all_of", () -> AllOf.LocationBasedEffects.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<ApplyMobEffect>> APPLY_MOB_EFFECT = LOCATION_BASED_EFFECT.register("apply_mob_effect", () -> ApplyMobEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<MobEnchantAttributeEffect>> ATTRIBUTE = LOCATION_BASED_EFFECT.register("attribute", () -> MobEnchantAttributeEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<DamageEntity>> DAMAGE = LOCATION_BASED_EFFECT.register("damage", () -> DamageEntity.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<ExplodeEffect>> EXPLODE = LOCATION_BASED_EFFECT.register("explode", () -> ExplodeEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<Ignite>> IGNITE = LOCATION_BASED_EFFECT.register("ignite", () -> Ignite.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<Freeze>> FREEZE = LOCATION_BASED_EFFECT.register("freeze", () -> Freeze.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<PlaySoundEffect>> PLAY_SOUND = LOCATION_BASED_EFFECT.register("play_sound", () -> PlaySoundEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<RunFunction>> RUN_FUNCTION = LOCATION_BASED_EFFECT.register("run_function", () -> RunFunction.CODEC);
    public static final DeferredHolder<MapCodec<? extends MobEnchantLocationBasedEffect>, MapCodec<SpawnParticlesEffect>> SPAWN_PARTICLES = LOCATION_BASED_EFFECT.register("spawn_particles", () -> SpawnParticlesEffect.CODEC);

    private static Registry<MapCodec<? extends MobEnchantLocationBasedEffect>> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(ModRegistries.MOB_ENCHANT_LOCATION_BASED_EFFECT_REGISTRY_KEY).sync(true));
    }

    public static Registry<MapCodec<? extends MobEnchantLocationBasedEffect>> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}