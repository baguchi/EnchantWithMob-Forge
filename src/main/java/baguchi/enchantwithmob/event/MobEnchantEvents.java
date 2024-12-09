package baguchi.enchantwithmob.event;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.message.SoulParticleMessage;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModCapability;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MobEnchantEvents {
    private static boolean isAdding = false;

    @SubscribeEvent
    public static void onEntityImpactWorld(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (!shooterIsLiving(projectile) || !EnchantConfig.COMMON.ALLOW_MULTISHOT_PROJECTILE.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).toString()))
            return;
        LivingEntity owner = (LivingEntity) projectile.getOwner();
        MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT, () -> {
            if (!projectile.level().isClientSide) {
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
                MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT, () -> {
                    if (level instanceof ServerLevel serverLevel && projectile.tickCount == 0 && !isAdding) {
                        isAdding = true;
                        CompoundTag compoundNBT = new CompoundTag();
                        compoundNBT = projectile.saveWithoutId(compoundNBT);
                        addProjectile(projectile, compoundNBT, serverLevel, 15.0F);
                        addProjectile(projectile, compoundNBT, serverLevel, -15.0F);
                        isAdding = false;
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathAndStealEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (damageSource != null && damageSource.getDirectEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) damageSource.getDirectEntity();
            if (attacker instanceof IEnchantCap cap) {
                int enchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SOUL_STEAL);
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

    @SubscribeEvent
    public static void onHit(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();

        if (event.getRayTraceResult() instanceof EntityHitResult) {
            EntityHitResult entityHitResult = (EntityHitResult) event.getRayTraceResult();
            MobEnchantUtils.executeIfPresent(entityHitResult.getEntity(), MobEnchants.DEFLECT, () -> {
                event.setCanceled(true);
                Vec3 vec3 = projectile.getDeltaMovement();
                projectile.deflect(ProjectileDeflection.REVERSE, entityHitResult.getEntity(), projectile.getOwner(), false);
            });
        }
    }

    @SubscribeEvent
    public static void onImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (!shooterIsLiving(projectile) || !EnchantConfig.COMMON.ALLOW_POISON_CLOUD_PROJECTILE.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(projectile.getType()).toString()))
            return;
        LivingEntity owner = (LivingEntity) projectile.getOwner();
        if (owner instanceof IEnchantCap cap) {
            int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.POISON_CLOUD);

            if (cap.getEnchantCap().hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.POISON_CLOUD)) {
                //arrow is different
                if (!(projectile instanceof AbstractArrow) || !projectile.onGround()) {
                    AreaEffectCloud areaeffectcloud = new AreaEffectCloud(owner.level(), event.getRayTraceResult().getLocation().x, event.getRayTraceResult().getLocation().y, event.getRayTraceResult().getLocation().z);
                    areaeffectcloud.setRadius(0.6F);
                    areaeffectcloud.setRadiusOnUse(-0.01F);
                    areaeffectcloud.setWaitTime(10);
                    areaeffectcloud.setDuration(80);
                    areaeffectcloud.setOwner(owner);
                    areaeffectcloud.setRadiusPerTick(-0.001F);

                    areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.POISON, 80, i - 1));
                    owner.level().addFreshEntity(areaeffectcloud);
                }
            }
        }
        ;
    }


    private static void addProjectile(Projectile projectile, CompoundTag compoundNBT, ServerLevel level, float rotation) {
        Projectile newProjectile = (Projectile) projectile.getType().create(level);
        UUID uuid = newProjectile.getUUID();
        newProjectile.load(compoundNBT);
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

        newProjectile.getData(ModCapability.ITEM_MOB_ENCHANT.get()).setHasEnchant(true);

        level.addFreshEntity(newProjectile);
    }

    public static boolean shooterIsLiving(Projectile projectile) {
        return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
    }
}
