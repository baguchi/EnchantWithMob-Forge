package baguchan.enchantwithmob.mixin.client;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.api.IEnchantedTime;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    public abstract M getModel();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", shift = At.Shift.AFTER))
    public void setRenderScales(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo callbackInfo) {
        if (p_115308_ instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.SMALL.get());
                    float cappedScale = Mth.clamp(0.15F * level, 0.0F, 0.9F);
                    p_115311_.scale(1.0F - cappedScale, 1.0F - cappedScale, 1.0F - cappedScale);
                } else if (MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get())) {
                    int level = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.HUGE.get());
                    p_115311_.scale(1.0F + 0.15F * level, 1.0F + 0.15F * level, 1.0F + 0.15F * level);
                } else if (EnchantConfig.COMMON.changeSizeWhenEnchant.get()) {
                    p_115311_.scale(1.05F, 1.05F, 1.05F);
                }
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;prepareMobModel(Lnet/minecraft/world/entity/Entity;FFF)V", shift = At.Shift.BEFORE))
    public void setRenderWithTime(T instance, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo callbackInfo) {
        if (this.getModel() instanceof IEnchantedTime enchantedTime) {
            if (instance instanceof IEnchantCap enchantCap) {
                //ajust time
                int fastTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), MobEnchants.FAST.get()), 0, 255);
                int slowTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), MobEnchants.SLOW.get()), 0, 255);
                float different = 1 + fastTime * 0.125F - slowTime * 0.125F;

                enchantedTime.setDifferentTime(different);
            }
        }
    }
}