package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantVisual;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.effects.EvokerFangsModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EvokerFangsRenderer;
import net.minecraft.client.renderer.entity.state.EvokerFangsRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
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
    private static Identifier TEXTURE_LOCATION;

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EvokerFangsRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("TAIL"))
    public void submit(EvokerFangsRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (state instanceof IEnchantVisual enchantVisual && enchantVisual.hasEnchantVisual()) {
            float f = state.biteProgress;
			if (f != 0.0F) {
				float f1 = 2.0F;
				if (f > 0.9F) {
					f1 *= (1.0F - f) / 0.1F;
				}

                poseStack.pushPose();
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - state.yRot));
                poseStack.scale(-f1, -f1, f1);
				float f2 = 0.03125F;
                poseStack.translate(0.0D, -0.626D, 0.0D);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                this.model.setupAnim(state);
                submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(TEXTURE_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
                poseStack.popPose();
			}
		}
	}
}
