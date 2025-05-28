package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.registry.MobEnchants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SlowMobEnchant extends MobEnchant {
    public SlowMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 15;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 60;
    }
    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    public boolean isCursedEnchant() {
        return true;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Player) || EnchantConfig.COMMON.bigYourSelf.get();
    }

    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != MobEnchants.SPEEDY.get() && ench != MobEnchants.FAST.get();
    }
}