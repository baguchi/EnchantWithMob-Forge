package baguchi.enchantwithmob.mobenchant.effects.location;

import baguchi.enchantwithmob.registry.MobEnchantLocationBasedEffects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface MobEnchantLocationBasedEffect {

    Codec<MobEnchantLocationBasedEffect> CODEC = MobEnchantLocationBasedEffects.getRegistry().byNameCodec()
            .dispatch(MobEnchantLocationBasedEffect::codec, Function.identity());

    default void onDeactivated(@Nullable LivingEntity owner, Entity entity, Vec3 pos, int enchantmentLevel) {
    }

    void onChangedBlock(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity, Vec3 pos, boolean applyTransientEffects);


    MapCodec<? extends MobEnchantLocationBasedEffect> codec();
}
