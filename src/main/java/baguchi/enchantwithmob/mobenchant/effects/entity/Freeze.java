package baguchi.enchantwithmob.mobenchant.effects.entity;

import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record Freeze(LevelBasedValue duration) implements MobEnchantEntityEffect {
    public static final MapCodec<Freeze> CODEC = RecordCodecBuilder.mapCodec(
            p_345641_ -> p_345641_.group(LevelBasedValue.CODEC.fieldOf("duration").forGetter(p_345622_ -> p_345622_.duration)).apply(p_345641_, Freeze::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity, Vec3 origin) {
        entity.setTicksFrozen((int) (this.duration.calculate(enchantmentLevel) * 20));
    }

    @Override
    public MapCodec<Freeze> codec() {
        return CODEC;
    }
}
