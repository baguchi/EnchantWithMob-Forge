package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;
import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.enchantSwirl;


public class SlimeEnchantLayer<T extends LivingEntityRenderState> extends RenderLayer<T, SlimeModel> {
	private final SlimeModel model;

	public SlimeEnchantLayer(RenderLayerParent<T, SlimeModel> p_174536_, EntityModelSet p_174537_) {
		super(p_174536_);
		this.model = new SlimeModel(p_174537_.bakeLayer(ModelLayers.SLIME_OUTER));
	}

	@Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, T entitylivingbaseIn, float v, float v1) {
        boolean enchanted = entitylivingbaseIn.getRenderDataOrDefault(EnchantLayer.ENCHANTED, false);
        boolean ancient = entitylivingbaseIn.getRenderDataOrDefault(EnchantLayer.ANCIENT, false);
        if (!EnchantConfig.CLIENT.disableAuraRender.get()) {
            if (enchanted && !entitylivingbaseIn.isInvisible) {
                this.model.setupAnim(entitylivingbaseIn);
                submitNodeCollector.submitModel(this.model, entitylivingbaseIn, poseStack, enchantSwirl(ancient ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR), i, OverlayTexture.NO_OVERLAY, -1,
                        null,
                        entitylivingbaseIn.outlineColor,
                        null);
            }
        }
	}
}