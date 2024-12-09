package baguchi.enchantwithmob.mobenchant.effects.entity;

import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public record ApplyMobEffect(
        HolderSet<MobEffect> toApply, LevelBasedValue minDuration, LevelBasedValue maxDuration,
        LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier
) implements MobEnchantEntityEffect {
    public static final MapCodec<ApplyMobEffect> CODEC = RecordCodecBuilder.mapCodec(
            p_346379_ -> p_346379_.group(
                            RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffect::toApply),
                            LevelBasedValue.CODEC.fieldOf("min_duration").forGetter(ApplyMobEffect::minDuration),
                            LevelBasedValue.CODEC.fieldOf("max_duration").forGetter(ApplyMobEffect::maxDuration),
                            LevelBasedValue.CODEC.fieldOf("min_amplifier").forGetter(ApplyMobEffect::minAmplifier),
                            LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxAmplifier)
                    )
                    .apply(p_346379_, ApplyMobEffect::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity, Vec3 origin) {
        if (entity instanceof LivingEntity livingentity) {
            RandomSource randomsource = livingentity.getRandom();
            Optional<Holder<MobEffect>> optional = this.toApply.getRandomElement(randomsource);
            if (optional.isPresent()) {
                int i = Math.round(Mth.randomBetween(randomsource, this.minDuration.calculate(enchantmentLevel), this.maxDuration.calculate(enchantmentLevel)) * 20.0F);
                int j = Math.max(0, Math.round(Mth.randomBetween(randomsource, this.minAmplifier.calculate(enchantmentLevel), this.maxAmplifier.calculate(enchantmentLevel))));
                livingentity.addEffect(new MobEffectInstance(optional.get(), i, j), owner);
            }
        }
    }

    @Override
    public MapCodec<ApplyMobEffect> codec() {
        return CODEC;
    }
}
