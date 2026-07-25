package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class HugeMobEnchant extends MobEnchant {
    public HugeMobEnchant(Properties properties) {
        super(properties);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + (enchantmentLevel - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingIncomingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            MobEnchantAttachment attachmentAttacker = attacker.getData(ModAttachments.MOB_ENCHANTS);

            if (attachmentAttacker.hasEnchant()) {
                if (event.getAmount() > 0) {
                    event.setAmount(getDamageIncrease(event.getAmount(), attachmentAttacker));
                }

            }
        }
    }

    public static float getDamageIncrease(float damage, MobEnchantAttachment cap) {
        int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.HUGE.getKey());
        if (level > 0) {
            damage *= 1.0F + level * 0.15F;
        }
        return damage;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) || EnchantConfig.COMMON.bigYourSelf.get();
    }

    @Override
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && anotherHolder.value() != MobEnchants.SMALL.get();
    }
}
