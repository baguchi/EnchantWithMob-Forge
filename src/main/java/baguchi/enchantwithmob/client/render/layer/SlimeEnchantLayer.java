package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.api.IEnchantCap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;
import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.enchantSwirl;


@OnlyIn(Dist.CLIENT)
public class SlimeEnchantLayer<T extends LivingEntityRenderState> extends RenderLayer<T, SlimeModel> {
	private final SlimeModel model;

	public SlimeEnchantLayer(RenderLayerParent<T, SlimeModel> p_174536_, EntityModelSet p_174537_) {
		super(p_174536_);
		this.model = new SlimeModel(p_174537_.bakeLayer(ModelLayers.SLIME_OUTER));
	}

	public void render(PoseStack p_117470_, MultiBufferSource p_117471_, int p_117472_, T p_117473_, float p_117474_, float p_117475_, float p_117476_, float p_117477_, float p_117478_, float p_117479_) {
		Minecraft minecraft = Minecraft.getInstance();
		if (p_117473_ instanceof IEnchantCap cap) {
			if (cap.getEnchantCap().hasEnchant()) {
				if (!p_117473_.isInvisible) {
					float intensity = cap.getEnchantCap().getMobEnchants().size() < 3 ? ((float) cap.getEnchantCap().getMobEnchants().size() / 3) : 3;

                    VertexConsumer vertexconsumer = p_117471_.getBuffer(enchantSwirl(cap.getEnchantCap().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR));
					this.model.setupAnim(p_117473_);
					this.model.renderToBuffer(p_117470_, vertexconsumer, p_117472_, LivingEntityRenderer.getOverlayCoords(p_117473_, 0.0F));
				}
			}
		}
		;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entitylivingbaseIn, float v, float v1) {
		float tick = (float) entitylivingbaseIn.ageInTicks;
		if (entitylivingbaseIn instanceof IEnchantCap cap) {
			if (cap.getEnchantCap().hasEnchant() && !entitylivingbaseIn.isInvisible) {
				float f = (float) entitylivingbaseIn.ageInTicks;
				float intensity = cap.getEnchantCap().getMobEnchants().size() < 3 ? ((float) cap.getEnchantCap().getMobEnchants().size() / 3) : 3;
				SlimeModel entitymodel = this.getParentModel();
                VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(enchantSwirl(cap.getEnchantCap().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR));
				entitymodel.setupAnim(entitylivingbaseIn);
				entitymodel.renderToBuffer(poseStack, ivertexbuilder, i, OverlayTexture.NO_OVERLAY);
			}
		}
	}
}