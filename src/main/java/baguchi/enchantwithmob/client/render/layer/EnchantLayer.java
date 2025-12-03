package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.ClientRegistrar;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;


public class EnchantLayer<T extends LivingEntityRenderState, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public static final Identifier ANCIENT_GLINT = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/ancient_glint.png");

    public EnchantLayer(RenderLayerParent<T, M> p_i50947_1_) {
        super(p_i50947_1_);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, T entitylivingbaseIn, float v, float v1) {
        float tick = (float) entitylivingbaseIn.ageInTicks;
        if (entitylivingbaseIn instanceof IEnchantCap cap && !EnchantConfig.CLIENT.disableAuraRender.get()) {
            if (cap.getEnchantCap().hasEnchant() && !entitylivingbaseIn.isInvisible) {
                float f = (float) entitylivingbaseIn.ageInTicks;
                float intensity = cap.getEnchantCap().getMobEnchants().size() < 3 ? ((float) cap.getEnchantCap().getMobEnchants().size() / 3) : 3;
                M entitymodel = this.getParentModel();
                entitymodel.setupAnim(entitylivingbaseIn);
                submitNodeCollector.submitModel(entitymodel, entitylivingbaseIn, poseStack, enchantSwirl(cap.getEnchantCap().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR), i, OverlayTexture.NO_OVERLAY, -1,
                        null,
                        entitylivingbaseIn.outlineColor,
                        null);
            }
        }
    }

    public static RenderType enchantSwirl(Identifier resourceLocation) {
        return RenderType.create("enchant_effect", RenderSetup.builder(ClientRegistrar.MOB_ENCHANT)
                .withTexture("Sampler0", resourceLocation)
                .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING).createRenderSetup());
    }

    public static RenderType enchantBeamSwirl(Identifier resourceLocation) {
        return RenderType.create("enchant_beam_effect", RenderSetup.builder(ClientRegistrar.MOB_ENCHANT_BEAM)
                .withTexture("Sampler0", resourceLocation)
                .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING).createRenderSetup());
    }

}