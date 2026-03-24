package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.utils.EntityUtils;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

import java.util.UUID;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MultiShotMobEnchant extends MobEnchant {
    private static boolean isAdding = false;

    public MultiShotMobEnchant(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onEntityImpactWorld(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (!shooterIsLiving(projectile) || !EnchantConfig.COMMON.ALLOW_MULTISHOT_PROJECTILE.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).toString()))
            return;
        LivingEntity owner = (LivingEntity) projectile.getOwner();
        MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT.getKey(), () -> {
            if (!projectile.level().isClientSide()) {
                if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
                    if (entityHitResult.getEntity() instanceof Projectile projectile2) {
                        if (shooterIsLiving(projectile2) && projectile2.getOwner() == projectile.getOwner()) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk()) {
            Entity entity = event.getEntity();
            Level level = event.getLevel();
            if (entity instanceof Projectile) {
                Projectile projectile = (Projectile) entity;
                if (!shooterIsLiving(projectile) || !EnchantConfig.COMMON.ALLOW_MULTISHOT_PROJECTILE.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))
                    return;
                LivingEntity owner = (LivingEntity) projectile.getOwner();
                MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT.getKey(), () -> {
                    if (!level.isClientSide() && projectile.tickCount == 0 && !isAdding) {
                        isAdding = true;
                        addProjectile(projectile, level, 15.0F);
                        addProjectile(projectile, level, -15.0F);
                        isAdding = false;
                    }
                });
            }
        }
    }

    private static void addProjectile(Projectile projectile, Level level, float rotation) {
        Projectile newProjectile = (Projectile) projectile.getType().create(level, EntitySpawnReason.EVENT);
        UUID uuid = newProjectile.getUUID();
        EntityUtils.loadFrom(newProjectile, projectile);
        newProjectile.setUUID(uuid);
        Vec3 vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));

        newProjectile.setDeltaMovement(vector3d);
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        newProjectile.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI)));
        newProjectile.setXRot((float) (Mth.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI)));
        newProjectile.yRotO = newProjectile.getYRot();
        newProjectile.xRotO = newProjectile.getXRot();
        if (newProjectile instanceof Projectile) {
            Projectile newDamagingProjectile = (Projectile) newProjectile;
            Vec3 newPower = new Vec3(newDamagingProjectile.getDeltaMovement().x, newDamagingProjectile.getDeltaMovement().y, newDamagingProjectile.getDeltaMovement().z).yRot((float) (Math.PI / rotation));

            newDamagingProjectile.setDeltaMovement(newPower);
        }

        if (newProjectile instanceof AbstractArrow) {
            ((AbstractArrow) newProjectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        newProjectile.getData(ModAttachments.ITEM_MOB_ENCHANT.get()).setHasEnchant(true);
        level.addFreshEntity(newProjectile);
    }

    public static boolean shooterIsLiving(Projectile projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 40;
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return EnchantConfig.COMMON.WHITELIST_SHOOT_ENTITY.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType()).toString()) || super.isCompatibleMob(livingEntity);
    }
}
