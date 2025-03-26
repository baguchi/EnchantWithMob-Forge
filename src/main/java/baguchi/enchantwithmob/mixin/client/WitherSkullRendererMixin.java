package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.WitherSkullRenderer;
import net.minecraft.client.renderer.entity.state.WitherSkullRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WitherSkullRenderer.class, remap = false)
public class WitherSkullRendererMixin {

	@Shadow
	@Final
	private SkullModel model;

	@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/WitherSkullRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
	public void render(WitherSkullRenderState p_365156_, PoseStack p_116475_, MultiBufferSource p_116476_, int p_116477_, CallbackInfo ci) {
		if (p_365156_ instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (cap.getEnchantCap().hasEnchant()) {
					p_116475_.pushPose();
					p_116475_.scale(-1.0F, -1.0F, 1.0F);
                    VertexConsumer vertexconsumer = p_116476_.getBuffer(EnchantLayer.enchantSwirl(cap.getEnchantCap().isAncient() ? EnchantLayer.ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR));
					this.model.setupAnim(0.0F, p_365156_.yRot, p_365156_.xRot);
					this.model.renderToBuffer(p_116475_, vertexconsumer, p_116477_, OverlayTexture.NO_OVERLAY);
					p_116475_.popPose();
				}
			}
			;
		}
	}
}
