package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnderDragonRenderer.class)
public class EnderDragonRendererMixin {

	@Shadow
	@Final
    private EnderDragonModel model;

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("TAIL"))
    public void render(EnderDragonRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (renderState instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                poseStack.pushPose();
                float f = renderState.getHistoricalPos(7).yRot();
                float f1 = (float) (renderState.getHistoricalPos(5).y() - renderState.getHistoricalPos(10).y());
                poseStack.mulPose(Axis.YP.rotationDegrees(-f));
                poseStack.mulPose(Axis.XP.rotationDegrees(f1 * 10.0F));
                poseStack.translate(0.0F, 0.0F, 1.0F);
                poseStack.scale(-1.0F, -1.0F, 1.0F);
                poseStack.translate(0.0F, -1.501F, 0.0F);
                this.model.setupAnim(renderState);
                if (renderState.deathTime <= 0) {
                    submitNodeCollector.submitModel(this.model, renderState, poseStack, EnchantLayer.enchantSwirl(cap.getEnchantCap().getMobEnchantType().is(MobEnchantTypes.ANCIENT) ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null);
                }

                poseStack.popPose();
            }
        }
    }
}
