package baguchi.enchantwithmob.mobenchant;


import baguchi.enchantwithmob.registry.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;

public class ThornEnchant extends MobEnchant {
    public ThornEnchant(Properties properties) {
        super(properties);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Guardian);
    }

    @Override
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && anotherHolder.is(ModTags.MobEnchantTags.AFFECT_SELF_REFLECT);
    }
}
