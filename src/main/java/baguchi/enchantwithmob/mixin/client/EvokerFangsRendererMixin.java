package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantVisual;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EvokerFangsRenderer;
import net.minecraft.client.renderer.entity.state.EvokerFangsRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EvokerFangsRenderer.class)
public class EvokerFangsRendererMixin {

	@Shadow
	@Final
	private EvokerFangsModel model;

    @Shadow
    @Final
    private static ResourceLocation TEXTURE_LOCATION;

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EvokerFangsRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("TAIL"))
    public void render(EvokerFangsRenderState evokerFangsRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState p_450960_, CallbackInfo ci) {
        if (evokerFangsRenderState instanceof IEnchantVisual enchantVisual && enchantVisual.hasEnchantVisual()) {
            float f = evokerFangsRenderState.biteProgress;
			if (f != 0.0F) {
				float f1 = 2.0F;
				if (f > 0.9F) {
					f1 *= (1.0F - f) / 0.1F;
				}

                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - evokerFangsRenderState.yRot));
                poseStack.scale(-f1, -f1, f1);
				float f2 = 0.03125F;
                poseStack.translate(0.0D, -0.626D, 0.0D);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                this.model.setupAnim(evokerFangsRenderState);
                submitNodeCollector.submitModel(this.model, evokerFangsRenderState, poseStack, this.model.renderType(TEXTURE_LOCATION), evokerFangsRenderState.lightCoords, OverlayTexture.NO_OVERLAY, evokerFangsRenderState.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
                poseStack.popPose();
			}
		}
	}
}
