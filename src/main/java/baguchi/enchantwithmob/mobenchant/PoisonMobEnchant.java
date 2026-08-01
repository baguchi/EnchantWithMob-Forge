package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
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

    @SubscribeEvent
    public static void onEntityHurtPost(LivingDamageEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            if (attacker.level() instanceof ServerLevel serverLevel) {
                MobEnchantAttachment attachment = attacker.getData(ModAttachments.MOB_ENCHANTS);

                if (attachment.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(attachment.getMobEnchants(), MobEnchants.POISON.getKey())) {
                    int i = MobEnchantUtils.getMobEnchantLevelFromHandler(attachment.getMobEnchants(), MobEnchants.POISON.getKey());

                    if (attacker.getRandom().nextFloat() < i * 0.125F) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), attacker);
                    }
                }
            }
        }
    }


    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Bee) && !(livingEntity instanceof CaveSpider);
    }

    @Override
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && !anotherHolder.is(ModTags.MobEnchantTags.POST_ATTACK);
    }
}

