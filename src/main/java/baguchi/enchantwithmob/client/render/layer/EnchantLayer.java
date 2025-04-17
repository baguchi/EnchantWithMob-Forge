package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class EnchantLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);
    protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENERGY_SWIRL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEnergySwirlShader);
    protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
    protected static final RenderStateShard.TexturingStateShard ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> {
        setupGlintTexturing(0.16F);
    }, () -> {
        RenderSystem.resetTextureMatrix();
    });
    protected static final RenderStateShard.DepthTestStateShard EQUAL_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("==", 514);

    protected static final RenderStateShard.WriteMaskStateShard COLOR_WRITE = new RenderStateShard.WriteMaskStateShard(true, false);


    public static final ResourceLocation ANCIENT_GLINT = ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/ancient_glint.png");

    public EnchantLayer(RenderLayerParent<T, M> p_i50947_1_) {
        super(p_i50947_1_);
    }

    public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
        if (entitylivingbaseIn instanceof IEnchantCap cap && !EnchantConfig.CLIENT.disableAuraRender.get()) {

            if (cap.getEnchantCap().hasEnchant() && !entitylivingbaseIn.isInvisible()) {
                float f = (float) entitylivingbaseIn.tickCount + partialTicks;
                float intensity = cap.getEnchantCap().getMobEnchants().size() < 3 ? ((float) cap.getEnchantCap().getMobEnchants().size() / 3) : 3;
                EntityModel<T> entitymodel = this.getParentModel();
                entitymodel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.getParentModel().copyPropertiesTo(entitymodel);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(enchantSwirl(cap.getEnchantCap().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ENTITY));
                entitymodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                entitymodel.renderToBuffer(poseStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(intensity, 1F, 1F, 1F));
            }
        }
        ;
    }

    public static RenderType enchantSwirl(ResourceLocation resourceLocation) {
        return RenderType.create("enchant_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setWriteMaskState(COLOR_WRITE).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    }

    public static RenderType enchantBeamSwirl(ResourceLocation resourceLocation) {
        return RenderType.create("enchant_beam_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setWriteMaskState(COLOR_WRITE).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(ADDITIVE_TRANSPARENCY).setCullState(NO_CULL).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false));
    }

    private static void setupGlintTexturing(float p_110187_) {
        long i = Util.getMillis() * 8L;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
        matrix4f.rotateZ(0.17453292F).scale(p_110187_);
        RenderSystem.setTextureMatrix(matrix4f);
    }
}