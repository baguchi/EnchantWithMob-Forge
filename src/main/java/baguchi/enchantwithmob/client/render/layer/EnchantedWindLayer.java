package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.client.ModModelLayers;
import baguchi.enchantwithmob.client.model.EnchantedWindModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;

public class EnchantedWindLayer<T extends LivingEntityRenderState, M extends EntityModel<LivingEntityRenderState>> extends RenderLayer<T, M> {
    private static final Identifier WIND_TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/breeze/breeze_wind.png");
    public static final ContextKey<Boolean> WIND = new ContextKey<>(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "wind"));

    private static final float TOP_PART_ALPHA = 1.0F;
    private static final float MIDDLE_PART_ALPHA = 1.0F;
    private static final float BOTTOM_PART_ALPHA = 1.0F;
    private final EnchantedWindModel<T> model;

    public EnchantedWindLayer(RenderLayerParent<T, M> p_312625_, EntityModelSet p_312909_) {
        super(p_312625_);
        this.model = new EnchantedWindModel<>(p_312909_.bakeLayer(ModModelLayers.ENCHANTED_WIND));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int p_117351_, T entity, float p_117353_, float p_117354_) {
        boolean wind = entity.getRenderDataOrDefault(WIND, false);

        if (wind) {
            RenderType rendertype = RenderTypes.breezeWind(WIND_TEXTURE_LOCATION, this.xOffset(entity.ageInTicks) % 1.0F, 0.0F);
            submitNodeCollector.order(1).submitModel(this.model, entity, poseStack, rendertype, p_117351_, OverlayTexture.NO_OVERLAY, -1, null, entity.outlineColor, null);
        }
    }

    private float xOffset(float p_312086_) {
        return p_312086_ * 0.02F;
    }


    public Identifier getWindTextureLocation() {
        return WIND_TEXTURE_LOCATION;
    }
}
