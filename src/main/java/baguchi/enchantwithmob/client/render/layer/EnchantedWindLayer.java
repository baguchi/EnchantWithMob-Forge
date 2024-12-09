package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.ModModelLayers;
import baguchi.enchantwithmob.client.model.EnchantedWindModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class EnchantedWindLayer<T extends LivingEntityRenderState, M extends EntityModel<LivingEntityRenderState>> extends RenderLayer<T, M> {
    private static final ResourceLocation WIND_TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/breeze/breeze_wind.png");

    private static final float TOP_PART_ALPHA = 1.0F;
    private static final float MIDDLE_PART_ALPHA = 1.0F;
    private static final float BOTTOM_PART_ALPHA = 1.0F;
    private final EnchantedWindModel<T> model;

    public EnchantedWindLayer(RenderLayerParent<T, M> p_312625_, EntityModelSet p_312909_) {
        super(p_312625_);
        this.model = new EnchantedWindModel<>(p_312909_.bakeLayer(ModModelLayers.ENCHANTED_WIND));
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_361554_, float p_117353_, float p_117354_) {
        if (p_361554_ instanceof IEnchantCap cap) {
            /*if (cap.getEnchantCap().hasEnchant() && MobEnchantUtils.hasWindEnchant(cap.getEnchantCap().getMobEnchants())) {

                float f = (float) p_361554_.ageInTicks;
                VertexConsumer vertexconsumer = p_117350_.getBuffer(RenderType.breezeWind(getWindTextureLocation(), this.xOffset(f) % 1.0F, 0.0F));
                this.model.setupAnim(p_361554_);
                this.model.windTop().skipDraw = true;
                this.model.windMiddle().skipDraw = true;
                this.model.windBottom().skipDraw = false;
                this.model.root().render(p_117349_, vertexconsumer, p_117351_, OverlayTexture.NO_OVERLAY);
                this.model.windTop().skipDraw = true;
                this.model.windMiddle().skipDraw = false;
                this.model.windBottom().skipDraw = true;
                this.model.root().render(p_117349_, vertexconsumer, p_117351_, OverlayTexture.NO_OVERLAY);
                this.model.windTop().skipDraw = false;
                this.model.windMiddle().skipDraw = true;
                this.model.windBottom().skipDraw = true;
                this.model.root().render(p_117349_, vertexconsumer, p_117351_, OverlayTexture.NO_OVERLAY);
            }*/
        }
    }

    private float xOffset(float p_312086_) {
        return p_312086_ * 0.02F;
    }


    public ResourceLocation getWindTextureLocation() {
        return WIND_TEXTURE_LOCATION;
    }
}
