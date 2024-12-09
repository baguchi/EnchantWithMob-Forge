package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.client.particle.TrialSpawnerDetectionParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, EnchantWithMob.MODID);

    public static final Supplier<SimpleParticleType> ENCHANT = PARTICLE_TYPES.register("enchant", () -> new SimpleParticleType(false));

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ENCHANT.get(), TrialSpawnerDetectionParticle.Provider::new);
    }
}
