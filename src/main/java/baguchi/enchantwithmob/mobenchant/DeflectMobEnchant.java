package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class DeflectMobEnchant extends MobEnchant {
    public DeflectMobEnchant(Properties properties) {
        super(properties);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }

    @SubscribeEvent
    public static void onHit(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();

        if (event.getRayTraceResult() instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult) event.getRayTraceResult();
            MobEnchantUtils.executeIfPresent(entityHitResult.getEntity(), MobEnchants.DEFLECT.getKey(), () -> {
                event.setCanceled(true);
                projectile.deflect(ProjectileDeflection.AIM_DEFLECT, entityHitResult.getEntity(), EntityReference.of(projectile.getOwner()), false);
            });
        }
    }

    @Override
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && anotherHolder.is(ModTags.MobEnchantTags.AFFECT_SELF_REFLECT);
    }
}
