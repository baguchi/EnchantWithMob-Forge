package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.dragon.EnderDragonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnderDragonRenderer.class, remap = false)
public class EnderDragonRendererMixin {

	@Shadow
	@Final
    private EnderDragonModel model;

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    public void render(EnderDragonRenderState p_364805_, PoseStack p_114211_, MultiBufferSource p_114212_, int p_114213_, CallbackInfo ci) {
        if (p_364805_ instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                p_114211_.pushPose();
                float f = p_364805_.getHistoricalPos(7).yRot();
                float f1 = (float) (p_364805_.getHistoricalPos(5).y() - p_364805_.getHistoricalPos(10).y());
                p_114211_.mulPose(Axis.YP.rotationDegrees(-f));
                p_114211_.mulPose(Axis.XP.rotationDegrees(f1 * 10.0F));
                p_114211_.translate(0.0F, 0.0F, 1.0F);
                p_114211_.scale(-1.0F, -1.0F, 1.0F);
                p_114211_.translate(0.0F, -1.501F, 0.0F);
                this.model.setupAnim(p_364805_);
                if (p_364805_.deathTime <= 0) {
                    VertexConsumer vertexconsumer3 = p_114212_.getBuffer(EnchantLayer.enchantSwirl(cap.getEnchantCap().isAncient() ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR));
                    this.model.renderToBuffer(p_114211_, vertexconsumer3, p_114213_, OverlayTexture.pack(0.0F, p_364805_.hasRedOverlay));
                }

                p_114211_.popPose();
            }
        }
        ;
    }
}
