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

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    public void modifyDamageProtection(ServerLevel level, int p344605, LivingEntity entity, DamageSource damageSource, MutableFloat mutablefloat) {
    }

    public void modifyDamage(ServerLevel level, int p344526, Entity entity, DamageSource damageSource, MutableFloat mutablefloat) {
    }
}
