package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
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

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("TAIL"))
    public void submit(EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        boolean enchanted = state.getRenderDataOrDefault(EnchantLayer.ENCHANTED, false);

        if (enchanted) {
                poseStack.pushPose();
                float f = state.getHistoricalPos(7).yRot();
                float f1 = (float) (state.getHistoricalPos(5).y() - state.getHistoricalPos(10).y());
                poseStack.mulPose(Axis.YP.rotationDegrees(-f));
                poseStack.mulPose(Axis.XP.rotationDegrees(f1 * 10.0F));
                poseStack.translate(0.0F, 0.0F, 1.0F);
                poseStack.scale(-1.0F, -1.0F, 1.0F);
                poseStack.translate(0.0F, -1.501F, 0.0F);
                this.model.setupAnim(state);
                if (state.deathTime <= 0) {
                    submitNodeCollector.submitModel(this.model, state, poseStack, EnchantLayer.enchantSwirl(ItemFeatureRenderer.ENCHANTED_GLINT_ARMOR), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
                }

                poseStack.popPose();
            }

    }
}
