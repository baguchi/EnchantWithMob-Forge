package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.registry.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.monster.spider.CaveSpider;

public class PoisonMobEnchant extends MobEnchant {
    public PoisonMobEnchant(Properties properties) {
        super(properties);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    @Override
    public void tick(LivingEntity entity, int level) {
        super.tick(entity, level);

        if (entity.level().isClientSide() && !EnchantConfig.CLIENT.disablePoisonParticle.get()) {
            entity.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.4F, 0.8F, 0.4F), entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 0.0,
                    0.0,
                    0.0);
        }
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Bee) && !(livingEntity instanceof CaveSpider);
    }

    @Override
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && anotherHolder.is(ModTags.MobEnchantTags.POISON);
    }
}

