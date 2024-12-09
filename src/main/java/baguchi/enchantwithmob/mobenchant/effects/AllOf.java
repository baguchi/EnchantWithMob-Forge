package baguchi.enchantwithmob.mobenchant.effects;

import baguchi.enchantwithmob.mobenchant.effects.location.MobEnchantLocationBasedEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public interface AllOf {
    static <T, A extends T> MapCodec<A> codec(Codec<T> codec, Function<List<T>, A> getter, Function<A, List<T>> factory) {
        return RecordCodecBuilder.mapCodec(p_345790_ -> p_345790_.group(codec.listOf().fieldOf("effects").forGetter(factory)).apply(p_345790_, getter));
    }

    static AllOf.EntityEffects entityEffects(MobEnchantEntityEffect... effects) {
        return new AllOf.EntityEffects(List.of(effects));
    }

    static AllOf.LocationBasedEffects locationBasedEffects(MobEnchantLocationBasedEffect... effects) {
        return new AllOf.LocationBasedEffects(List.of(effects));
    }

    static AllOf.ValueEffects valueEffects(EnchantmentValueEffect... effects) {
        return new AllOf.ValueEffects(List.of(effects));
    }

    public static record EntityEffects(List<MobEnchantEntityEffect> effects) implements MobEnchantEntityEffect {
        public static final MapCodec<AllOf.EntityEffects> CODEC = AllOf.codec(
                MobEnchantEntityEffect.CODEC, AllOf.EntityEffects::new, AllOf.EntityEffects::effects
        );

        @Override
        public void apply(ServerLevel p_346093_, int p_345940_, @Nullable LivingEntity owner, Entity p_345319_, Vec3 p_345200_) {
            for (MobEnchantEntityEffect enchantmententityeffect : this.effects) {
                enchantmententityeffect.apply(p_346093_, p_345940_, owner, p_345319_, p_345200_);
            }
        }

        @Override
        public MapCodec<AllOf.EntityEffects> codec() {
            return CODEC;
        }
    }

    public static record LocationBasedEffects(
            List<MobEnchantLocationBasedEffect> effects) implements MobEnchantLocationBasedEffect {
        public static final MapCodec<AllOf.LocationBasedEffects> CODEC = AllOf.codec(
                MobEnchantLocationBasedEffect.CODEC, AllOf.LocationBasedEffects::new, AllOf.LocationBasedEffects::effects
        );

        @Override
        public void onChangedBlock(ServerLevel p_345329_, int p_345154_, @Nullable LivingEntity owner, Entity p_345671_, Vec3 p_344781_, boolean p_345113_) {
            for (MobEnchantLocationBasedEffect enchantmentlocationbasedeffect : this.effects) {
                enchantmentlocationbasedeffect.onChangedBlock(p_345329_, p_345154_, owner, p_345671_, p_344781_, p_345113_);
            }
        }

        @Override
        public void onDeactivated(@Nullable LivingEntity owner, Entity p_346234_, Vec3 p_346036_, int p_345698_) {
            for (MobEnchantLocationBasedEffect enchantmentlocationbasedeffect : this.effects) {
                enchantmentlocationbasedeffect.onDeactivated(owner, p_346234_, p_346036_, p_345698_);
            }
        }

        @Override
        public MapCodec<AllOf.LocationBasedEffects> codec() {
            return CODEC;
        }
    }

    public static record ValueEffects(List<EnchantmentValueEffect> effects) implements EnchantmentValueEffect {
        public static final MapCodec<AllOf.ValueEffects> CODEC = AllOf.codec(EnchantmentValueEffect.CODEC, AllOf.ValueEffects::new, AllOf.ValueEffects::effects);

        @Override
        public float process(int p_345324_, RandomSource p_345137_, float p_344866_) {
            for (EnchantmentValueEffect enchantmentvalueeffect : this.effects) {
                p_344866_ = enchantmentvalueeffect.process(p_345324_, p_345137_, p_344866_);
            }

            return p_344866_;
        }

        @Override
        public MapCodec<AllOf.ValueEffects> codec() {
            return CODEC;
        }
    }
}
