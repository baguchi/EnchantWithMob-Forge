package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.WitherSkullRenderer;
import net.minecraft.client.renderer.entity.state.WitherSkullRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WitherSkullRenderer.class)
public abstract class WitherSkullRendererMixin {

	@Shadow
	@Final
	private SkullModel model;

    @Shadow
    protected abstract Identifier getTextureLocation(WitherSkullRenderState p_361091_);

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/WitherSkullRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("TAIL"))
    public void render(WitherSkullRenderState witherSkullRenderState, PoseStack poseStack, SubmitNodeCollector p_433466_, CameraRenderState p_450994_, CallbackInfo ci) {
        if (witherSkullRenderState instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (cap.getEnchantCap().hasEnchant()) {
                    poseStack.pushPose();
                    poseStack.scale(-1.0F, -1.0F, 1.0F);
                    this.model.setupAnim(witherSkullRenderState.modelState);
                    p_433466_.submitModel(this.model, witherSkullRenderState.modelState, poseStack, EnchantLayer.enchantSwirl(cap.getEnchantCap().isAncient() ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR), witherSkullRenderState.lightCoords, OverlayTexture.NO_OVERLAY, witherSkullRenderState.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
                    poseStack.popPose();
				}
			}
		}
	}
}
