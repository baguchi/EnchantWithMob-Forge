package baguchi.enchantwithmob.mobenchant.effects.entity;

import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record PlaySoundEffect(Holder<SoundEvent> soundEvent, FloatProvider volume,
                              FloatProvider pitch) implements MobEnchantEntityEffect {
    public static final MapCodec<PlaySoundEffect> CODEC = RecordCodecBuilder.mapCodec(
            p_345234_ -> p_345234_.group(
                            SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundEffect::soundEvent),
                            FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundEffect::volume),
                            FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundEffect::pitch)
                    )
                    .apply(p_345234_, PlaySoundEffect::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity item, Entity entity, Vec3 origin) {
        RandomSource randomsource = entity.getRandom();
        if (!entity.isSilent()) {
            level.playSound(
                    null,
                    origin.x(),
                    origin.y(),
                    origin.z(),
                    this.soundEvent,
                    entity.getSoundSource(),
                    this.volume.sample(randomsource),
                    this.pitch.sample(randomsource)
            );
        }
    }

    @Override
    public MapCodec<PlaySoundEffect> codec() {
        return CODEC;
    }
}
