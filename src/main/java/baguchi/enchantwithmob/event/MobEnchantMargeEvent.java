package baguchi.enchantwithmob.event;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantHandler;
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

        if (outcome instanceof IEnchantCap cap) {
            if (livingEntity instanceof IEnchantCap livingCap) {
                if (livingCap.getEnchantCap().hasEnchant()) {
                    for (MobEnchantHandler mobEnchantHandler : livingCap.getEnchantCap().getMobEnchants()) {
                        cap.getEnchantCap().addMobEnchant(outcome, mobEnchantHandler.getMobEnchant(), mobEnchantHandler.getEnchantLevel());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntitySplit(MobSplitEvent event) {
        LivingEntity livingEntity = event.getParent();
        List<Mob> children = event.getChildren();
        for (Mob outcome : children) {
            if (outcome instanceof IEnchantCap cap) {
                if (livingEntity instanceof IEnchantCap livingCap) {
                    if (livingCap.getEnchantCap().hasEnchant()) {
                        for (MobEnchantHandler mobEnchantHandler : livingCap.getEnchantCap().getMobEnchants()) {
                            cap.getEnchantCap().addMobEnchant(outcome, mobEnchantHandler.getMobEnchant(), mobEnchantHandler.getEnchantLevel());
                        }
                    }
                }
            }
        }
    }
}
