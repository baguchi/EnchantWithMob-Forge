package baguchi.enchantwithmob.mobenchant;

import net.minecraft.world.entity.LivingEntity;

public class HealthBoostMobEnchant extends MobEnchant {
    public HealthBoostMobEnchant(Properties properties) {
        super(properties);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public void afterEnchanted(LivingEntity entity, int level) {
        entity.setHealth(Math.clamp(entity.getHealth() + level * 2F, 0, entity.getMaxHealth()));
    }
}
