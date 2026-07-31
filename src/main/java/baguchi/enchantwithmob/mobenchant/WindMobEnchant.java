package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
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

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }


    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        MobEnchantUtils.executeIfPresent(event.getEntity(), MobEnchants.WIND.getKey(), () -> {

            event.setDistance(event.getDistance() - 4F);
        });
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) && !(livingEntity instanceof Breeze);
    }
}
