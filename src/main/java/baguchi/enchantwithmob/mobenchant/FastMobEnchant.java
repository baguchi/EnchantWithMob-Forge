package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.registry.MobEnchants;
import net.minecraft.world.entity.LivingEntity;

public class FastMobEnchant extends MobEnchant {
    public FastMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 15;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 60;
    }


    public boolean isCursedEnchant() {
        return false;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) || EnchantConfig.COMMON.bigYourSelf.get();
    }

    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != MobEnchants.SPEEDY.get();
    }
}