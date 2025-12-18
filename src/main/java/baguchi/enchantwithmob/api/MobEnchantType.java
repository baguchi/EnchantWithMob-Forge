package baguchi.enchantwithmob.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record MobEnchantType(Identifier texture, float expGainScale) {
    public static final Codec<MobEnchantType> DIRECT_CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(Identifier.CODEC.fieldOf("texture")
                                    .forGetter(MobEnchantType::texture),
                            Codec.FLOAT.fieldOf("exp_gain_scale")
                                    .forGetter(MobEnchantType::expGainScale))
                    .apply(instance, MobEnchantType::new));
}
