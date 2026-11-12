package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.client.EnchantRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;


public class EnchantLayer<T extends LivingEntityRenderState, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public static final Identifier ANCIENT_GLINT = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/ancient_glint.png");
    public static final ContextKey<MobEnchantType> MOB_ENCHANT_TYPE = new ContextKey<>(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant_type"));
    public static final ContextKey<Boolean> ENCHANTED = new ContextKey<>(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanted"));

    public EnchantLayer(RenderLayerParent<T, M> p_i50947_1_) {
        super(p_i50947_1_);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, T entitylivingbaseIn, float v, float v1) {
        boolean enchanted = entitylivingbaseIn.getRenderDataOrDefault(EnchantLayer.ENCHANTED, false);
        MobEnchantType mobEnchantType = entitylivingbaseIn.getRenderData(EnchantLayer.MOB_ENCHANT_TYPE);
        if (!EnchantConfig.CLIENT.disableAuraRender.get()) {
            if (enchanted && !entitylivingbaseIn.isInvisible) {
                if (mobEnchantType != null) {
                    M entitymodel = this.getParentModel();
                    entitymodel.setupAnim(entitylivingbaseIn);
                    submitNodeCollector.order(2).submitModel(entitymodel, entitylivingbaseIn, poseStack, EnchantRenderTypes.MOB_ENCHANT.apply(mobEnchantType.texture()), i, OverlayTexture.NO_OVERLAY, -1,
                            null,
                            entitylivingbaseIn.outlineColor);
                } else {
                    M entitymodel = this.getParentModel();
                    entitymodel.setupAnim(entitylivingbaseIn);
                    submitNodeCollector.order(2).submitModel(entitymodel, entitylivingbaseIn, poseStack, EnchantRenderTypes.MOB_ENCHANT.apply(ItemFeatureRenderer.ENCHANTED_GLINT_ARMOR), i, OverlayTexture.NO_OVERLAY, -1,
                            null,
                            entitylivingbaseIn.outlineColor);
                }
            }
        }
    }

}