package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class DeflectMobEnchant extends MobEnchant {
    public DeflectMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

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
                Vec3 vec3 = projectile.getDeltaMovement();
                projectile.deflect(ProjectileDeflection.AIM_DEFLECT, entityHitResult.getEntity(), projectile.getOwner(), false);
            });
        }
    }

    @Override
    protected boolean canApplyTogether(MobEnchant ench) {
        return super.canApplyTogether(ench) && ench != MobEnchants.THORN.get();
    }

    @Override
    public boolean isTresureEnchant() {
        return true;
    }
}
