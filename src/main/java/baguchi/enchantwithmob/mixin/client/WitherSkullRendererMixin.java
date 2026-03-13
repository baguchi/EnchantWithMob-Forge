package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.WitherSkullRenderer;
import net.minecraft.client.renderer.entity.state.WitherSkullRenderState;
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

@Mixin(value = WitherSkullRenderer.class)
public abstract class WitherSkullRendererMixin {

	@Shadow
	@Final
	private SkullModel model;

    @Shadow
    protected abstract Identifier getTextureLocation(WitherSkullRenderState p_361091_);

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/WitherSkullRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("TAIL"))
    public void render(WitherSkullRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (state instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (cap.getEnchantCap().hasEnchant()) {
                    poseStack.pushPose();
                    poseStack.scale(-1.0F, -1.0F, 1.0F);
                    this.model.setupAnim(state.modelState);
                    submitNodeCollector.submitModel(this.model, state.modelState, poseStack, EnchantLayer.enchantSwirl(cap.getEnchantCap().getMobEnchantType().is(MobEnchantTypes.ANCIENT) ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
                    poseStack.popPose();
				}
			}
		}
	}
}
