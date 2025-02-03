package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.api.IEnchantedTime;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
        extends EntityRenderer<T, S> {
    @Shadow
    public abstract M getModel();

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    public void extractRenderState(T p_362733_, S p_360515_, float p_361157_, CallbackInfo ci) {
        if (p_362733_ instanceof IEnchantCap enchantCap) {
            if (p_360515_ instanceof IEnchantCap stateCap) {
                stateCap.setEnchantCap(enchantCap.getEnchantCap());
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)V", shift = At.Shift.BEFORE))
    public void setRenderWithTime(S p_361886_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        if (this.getModel() instanceof IEnchantedTime enchantedTime) {
            if (p_361886_ instanceof IEnchantCap enchantCap) {
                //ajust time
                int fastTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), MobEnchants.FAST), 0, 2);
                int slowTime = Mth.clamp(MobEnchantUtils.getMobEnchantLevelFromHandler(enchantCap.getEnchantCap().getMobEnchants(), MobEnchants.SLOW), 0, 2);
                float different = 1 + fastTime * 0.125F - slowTime * 0.125F;

                enchantedTime.enchantWithMob_Forge$setDifferentTime(different);
            }
        }
    }
}
