package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.ModModelLayers;
import baguchi.enchantwithmob.client.model.EnchantedWindModel;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class EnchantedWindLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation WIND_TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/breeze/breeze_wind.png");

    private static final float TOP_PART_ALPHA = 1.0F;
    private static final float MIDDLE_PART_ALPHA = 1.0F;
    private static final float BOTTOM_PART_ALPHA = 1.0F;
    private final EnchantedWindModel<T> model;

    public EnchantedWindLayer(RenderLayerParent<T, M> p_312625_, EntityModelSet p_312909_) {
        super(p_312625_);
        this.model = new EnchantedWindModel<>(p_312909_.bakeLayer(ModModelLayers.ENCHANTED_WIND));
    }

    public void render(
            PoseStack p_312822_,
            MultiBufferSource p_312869_,
            int p_311783_,
            T p_312046_,
            float p_312170_,
            float p_311773_,
            float p_312428_,
            float p_312287_,
            float p_312118_,
            float p_312531_
    ) {
        if (p_312046_ instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.WIND.getKey())) {
                float f = (float) p_312046_.tickCount + p_312428_;
                this.model.prepareMobModel(p_312046_, p_312170_, p_311773_, p_312428_);
                VertexConsumer vertexconsumer = p_312869_.getBuffer(RenderType.breezeWind(this.getTextureLocation(p_312046_), this.xOffset(f) % 1.0F, 0.0F));
                this.model.setupAnim(p_312046_, p_312170_, p_311773_, p_312287_, p_312118_, p_312531_);
                this.model.windTop().skipDraw = true;
                this.model.windMiddle().skipDraw = true;
                this.model.windBottom().skipDraw = false;
                this.model.root().render(p_312822_, vertexconsumer, p_311783_, OverlayTexture.NO_OVERLAY);
                this.model.windTop().skipDraw = true;
                this.model.windMiddle().skipDraw = false;
                this.model.windBottom().skipDraw = true;
                this.model.root().render(p_312822_, vertexconsumer, p_311783_, OverlayTexture.NO_OVERLAY);
                this.model.windTop().skipDraw = false;
                this.model.windMiddle().skipDraw = true;
                this.model.windBottom().skipDraw = true;
                this.model.root().render(p_312822_, vertexconsumer, p_311783_, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    private float xOffset(float p_312086_) {
        return p_312086_ * 0.02F;
    }

    protected ResourceLocation getTextureLocation(T p_312458_) {
        return WIND_TEXTURE_LOCATION;
    }
}
