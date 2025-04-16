package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.message.SoulParticleMessage;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class SoulStealMobEnchant extends MobEnchant {
    public SoulStealMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    @SubscribeEvent
    public static void onLivingDeathAndStealEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (damageSource != null && damageSource.getDirectEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) damageSource.getDirectEntity();
            if (attacker instanceof IEnchantCap cap) {
                int enchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SOUL_STEAL.getKey());
                if (cap.getEnchantCap().hasEnchant() && enchantLevel > 0 && !attacker.hasEffect(MobEffects.ABSORPTION)) {
                    if (attacker.getAbsorptionAmount() < 6) {
                        attacker.setAbsorptionAmount(Mth.clamp(attacker.getAbsorptionAmount() + enchantLevel, 0, 6));
                    }
                    if (!entity.level().isClientSide()) {
                        SoulParticleMessage message = new SoulParticleMessage(entity);
                        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
                    }
                }
            }
            ;
        }
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }
}
