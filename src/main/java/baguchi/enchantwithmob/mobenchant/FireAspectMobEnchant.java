package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class FireAspectMobEnchant extends MobEnchant {
    public FireAspectMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onEntityHurtPost(LivingDamageEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            if (attacker.level() instanceof ServerLevel serverLevel) {
                MobEnchantAttachment attachment = attacker.getData(ModAttachments.MOB_ENCHANTS);
                if (attachment.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(attachment.getMobEnchants(), MobEnchants.FIRE_ASPECT.getKey())) {
                    int i = MobEnchantUtils.getMobEnchantLevelFromHandler(attachment.getMobEnchants(), MobEnchants.FIRE_ASPECT.getKey());

                    livingEntity.igniteForSeconds(2 + 2 * (i - 1));
                }
            }
        }
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
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && !anotherHolder.is(ModTags.MobEnchantTags.POST_ATTACK);
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) && !livingEntity.getType().builtInRegistryHolder().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
    }
}
