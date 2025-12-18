package baguchi.enchantwithmob.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record MobEnchantType(Identifier texture, float expGainScale, Optional<ParticleOptions> particle) {
    public static final Codec<MobEnchantType> DIRECT_CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(Identifier.CODEC.fieldOf("texture")
                                    .forGetter(MobEnchantType::texture),
                            Codec.FLOAT.fieldOf("exp_gain_scale")
                                    .forGetter(MobEnchantType::expGainScale),
                            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(MobEnchantType::particle))
                    .apply(instance, MobEnchantType::new));
}
