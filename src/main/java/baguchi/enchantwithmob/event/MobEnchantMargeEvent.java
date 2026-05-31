package baguchi.enchantwithmob.event;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.attachment.MobEnchantContent;
import baguchi.enchantwithmob.registry.ModAttachments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.MobSplitEvent;

import java.util.List;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MobEnchantMargeEvent {

    @SubscribeEvent
    public static void onEntityConversion(LivingConversionEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();
        LivingEntity outcome = event.getOutcome();

        MobEnchantAttachment outcomeAttachment = outcome.getData(ModAttachments.MOB_ENCHANTS);
        MobEnchantAttachment attachment = livingEntity.getData(ModAttachments.MOB_ENCHANTS);

        if (attachment.hasEnchant()) {
            for (MobEnchantContent mobEnchantContent : attachment.getMobEnchants()) {
                outcomeAttachment.addMobEnchant(outcome, mobEnchantContent.getMobEnchant(), mobEnchantContent.getEnchantLevel());
            }
        }
    }

    @SubscribeEvent
    public static void onEntitySplit(MobSplitEvent event) {
        LivingEntity livingEntity = event.getParent();
        List<Mob> children = event.getChildren();
        for (Mob outcome : children) {
            MobEnchantAttachment outcomeAttachment = outcome.getData(ModAttachments.MOB_ENCHANTS);
            MobEnchantAttachment attachment = livingEntity.getData(ModAttachments.MOB_ENCHANTS);

            if (attachment.hasEnchant()) {
                for (MobEnchantContent mobEnchantContent : attachment.getMobEnchants()) {
                    outcomeAttachment.addMobEnchant(outcome, mobEnchantContent.getMobEnchant(), mobEnchantContent.getEnchantLevel());
                }
            }

        }
    }
}
