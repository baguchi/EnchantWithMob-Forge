package baguchi.enchantwithmob.mobenchant;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableFloat;

public class StrongMobEnchant extends MobEnchant {
    public StrongMobEnchant(Properties properties) {
        super(properties);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @Override
    public void modifyDamageProtection(ServerLevel level, int p344605, LivingEntity entity, DamageSource damageSource, MutableFloat mutablefloat) {
    }

    @Override
    public void modifyDamage(ServerLevel level, int p344526, Entity entity, DamageSource damageSource, MutableFloat mutablefloat) {
        mutablefloat.setValue(mutablefloat.getValue() + (p344526 - 1) * 0.5F + 1);
    }
}
