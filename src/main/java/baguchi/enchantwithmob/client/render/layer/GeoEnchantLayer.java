package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.enchantSwirl;

public class GeoEnchantLayer<T extends Entity & GeoAnimatable> extends GeoRenderLayer<T>
{
    public GeoEnchantLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        float tick = (float) animatable.tickCount + partialTick;
        if (animatable instanceof IEnchantCap cap && !EnchantConfig.CLIENT.disableAuraRender.get()) {

            if (cap.getEnchantCap().hasEnchant() && !animatable.isInvisible() && cap.getEnchantCap().getMobEnchantType() != null) {
                renderType = enchantSwirl(cap.getEnchantCap().getMobEnchantType().value().texture());

                if (renderType != null) {
                    getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType,
                            bufferSource.getBuffer(renderType), partialTick, LightTexture.FULL_SKY, packedOverlay,
                            getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt());
                }
            }

        }
    }
}