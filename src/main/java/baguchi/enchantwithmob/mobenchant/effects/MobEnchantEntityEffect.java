package baguchi.enchantwithmob.mobenchant.effects;

import baguchi.enchantwithmob.mobenchant.effects.location.MobEnchantLocationBasedEffect;
import baguchi.enchantwithmob.registry.MobEnchantEntityEffects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface MobEnchantEntityEffect extends MobEnchantLocationBasedEffect {
    Codec<MobEnchantEntityEffect> CODEC = MobEnchantEntityEffects.getRegistry()
            .byNameCodec()
            .dispatch(MobEnchantEntityEffect::codec, Function.identity());

    void apply(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity, Vec3 origin);

    @Override
    default void onChangedBlock(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        this.apply(level, enchantmentLevel, owner, entity, pos);
    }

    @Override
    MapCodec<? extends MobEnchantEntityEffect> codec();
}
