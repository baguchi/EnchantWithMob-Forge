package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class BouncyMobEnchant extends MobEnchant {
    public BouncyMobEnchant(Properties properties) {
        super(properties);
    }

    private static Vec2 applyVerticalHitAnglePowerTransfer(
            float verticalHitAngleScale,
            float horizontalPower,
            float verticalPower,
            Vec3 attackerPosition,
            Vec3 attackerAimDirection,
            Vec3 targetCenteredPosition,
            float targetHeight
    ) {
        float targetHalfHeight = 0.5F * targetHeight;
        Vec3 targetTopPos = targetCenteredPosition.add(0.0, targetHalfHeight, 0.0);
        Vec3 tagetBottomPos = targetCenteredPosition.add(0.0, -targetHalfHeight, 0.0);
        Vec3 attackerToTargetTop = targetTopPos.subtract(attackerPosition).normalize();
        Vec3 attackerToTargetBottom = tagetBottomPos.subtract(attackerPosition).normalize();
        float verticalHitAngleFactor = (float) Mth.clampedMap(attackerAimDirection.y, attackerToTargetTop.y, attackerToTargetBottom.y, -1.0, 1.0);
        float transferredPowerRatio = Math.abs(verticalHitAngleFactor * verticalHitAngleScale);
        if (verticalHitAngleFactor < 0.0F) {
            transferredPowerRatio = -transferredPowerRatio;
        }

        float px = horizontalPower * (1.0F - transferredPowerRatio);
        float py = verticalPower * (1.0F + transferredPowerRatio);
        return new Vec2(px, py);
    }

    private static Vec2 applyVerticalPositionAnglePowerRotation(
            float verticalPositionAngleScale,
            float horizontalPower,
            float verticalPower,
            float originalHorizontalPower,
            float originalVerticalPower,
            Vec3 attackerFeetPosition,
            Vec3 targetFeetPosition
    ) {
        Vec3 attackerFeetToTargetFeet = targetFeetPosition.subtract(attackerFeetPosition);
        float verticalPositionAngle = (float) Math.atan2(-attackerFeetToTargetFeet.y, attackerFeetToTargetFeet.horizontalDistance());
        Vec2 powerBeforeRotation = new Vec2(horizontalPower, verticalPower);
        Vec2 rotatedPower = powerBeforeRotation.rotate(-verticalPositionAngle * verticalPositionAngleScale);
        float horizontalRatio = originalHorizontalPower > 0.0F ? Mth.abs(rotatedPower.x) / originalHorizontalPower : 0.0F;
        float verticalRatio = originalVerticalPower > 0.0F ? Mth.abs(rotatedPower.y) / originalVerticalPower : 0.0F;
        float maxRatio = Math.max(horizontalRatio, verticalRatio);
        if (maxRatio > 1.0F) {
            rotatedPower = rotatedPower.scale(1.0F / maxRatio);
        }

        return rotatedPower;
    }

    private static Vec2 applyHorizontalHitAngleScale(
            float horizontalAngleScale, Vec2 originalAngle, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenter
    ) {
        Vec3 attackerToTarget = targetCenter.subtract(attackerPosition).normalize();
        float angleDiff = (float) Math.atan2(
                attackerAimDirection.x * attackerToTarget.z - attackerAimDirection.z * attackerToTarget.x,
                attackerAimDirection.x * attackerToTarget.x + attackerAimDirection.z * attackerToTarget.z
        );
        return originalAngle.rotate(angleDiff * horizontalAngleScale);
    }

    @SubscribeEvent
    public static void knockbackEvent(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();
        MobEnchantUtils.executeIfPresent(entity, MobEnchants.BOUNCE.getKey(), () -> {
            event.setCanceled(true);
            float horizontalHitAngleScale = 1.6F;
            float verticalHitAngleScale = 0.5F;
            float verticalPositionAngleScale = 0.8F;
            float horizontalPower = 1.6F;
            float verticalPower = 0.8F;
            float originalHorizontalPower = horizontalPower;
            float originalVerticalPower = verticalPower;
            Vec2 originalAngle = new Vec2((float) event.getOriginalRatioX(), (float) event.getOriginalRatioZ());
            Vec2 newAngle = applyHorizontalHitAngleScale(
                    1.6F, originalAngle, entity.getEyePosition(), entity.getLookAngle().normalize(), entity.getBoundingBox().getCenter()
            );
            Vec2 newPower = applyVerticalHitAnglePowerTransfer(
                    0.5F,
                    horizontalPower,
                    verticalPower,
                    entity.getEyePosition(),
                    entity.getLookAngle().normalize(),
                    entity.getBoundingBox().getCenter(),
                    entity.getBbHeight()
            );
            horizontalPower = newPower.x;
            verticalPower = newPower.y;
            newPower = applyVerticalPositionAnglePowerRotation(
                    0.8F, horizontalPower, verticalPower, originalHorizontalPower, originalVerticalPower, entity.position(), entity.position()
            );
            horizontalPower = newPower.x;
            verticalPower = newPower.y;
            event.setRatioX(newAngle.x);
            event.setRatioZ(newAngle.y);
            float powerMultiplier = Mth.sqrt(event.getStrength()) * (1.0F);
            horizontalPower *= powerMultiplier;
            verticalPower *= powerMultiplier;
            double knockBackResistance = entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            horizontalPower *= (float) (1.0 - knockBackResistance);
            verticalPower *= (float) (1.0 - knockBackResistance);
            entity.needsSync = true;
            Vec3 deltaMovement = entity.getDeltaMovement();
            horizontalPower *= 0.4F;
            horizontalPower = Mth.clamp(horizontalPower, -128.0F, 128.0F);
            verticalPower = Mth.clamp(verticalPower, -128.0F, 128.0F);
            Vec3 horizontalKnockback = new Vec3(event.getOriginalRatioX(), 0.0, event.getOriginalRatioZ()).normalize().scale(horizontalPower);
            entity.setDeltaMovement(deltaMovement.x - horizontalKnockback.x, deltaMovement.y + verticalPower * 1.2, deltaMovement.z - horizontalKnockback.z);
            entity.playSound(SoundEvents.SLIME_BLOCK_HIT);

        });
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 15;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 40;
    }

    @Override
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return super.canApplyTogether(holder, anotherHolder) && anotherHolder.is(ModTags.MobEnchantTags.AFFECT_SELF_REFLECT);
    }

    @Override
    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return super.isCompatibleMob(livingEntity) && livingEntity instanceof AbstractCubeMob && !(livingEntity instanceof SulfurCube);
    }
}
