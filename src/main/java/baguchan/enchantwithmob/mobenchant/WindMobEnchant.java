package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class WindMobEnchant extends MobEnchant {
    public WindMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }


    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        MobEnchantUtils.executeIfPresent(event.getEntity(), MobEnchants.WIND.get(), () -> {

            event.setDistance(event.getDistance() - 4F);
        });
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) && !(livingEntity instanceof Breeze);
    }
}
