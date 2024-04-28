package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantCapability;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class HugeMobEnchant extends MobEnchant {
    public HugeMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

            if (attacker instanceof IEnchantCap cap) {
                if (cap.getEnchantCap().hasEnchant()) {
                    if (event.getAmount() > 0) {
                        event.setAmount(getDamageIncrease(event.getAmount(), cap.getEnchantCap()));
                    }
                }
            }
        }
    }

    public static float getDamageIncrease(float damage, MobEnchantCapability cap) {
        int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getMobEnchants(), MobEnchants.HUGE.get());
        if (level > 0) {
            damage *= 1.0F + level * 0.15F;
        }
        return damage;
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) || EnchantConfig.COMMON.bigYourSelf.get();
    }
}
