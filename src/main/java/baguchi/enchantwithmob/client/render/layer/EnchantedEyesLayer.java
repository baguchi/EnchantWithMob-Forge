package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
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
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class EnchantedEyesLayer<T extends LivingEntity, M extends EntityModel<T>> extends EyesLayer<T, M> {
	protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENERGY_SWIRL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEnergySwirlShader);
	protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);
	protected static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
	private static final Function<ResourceLocation, RenderType> ENCHANTED_EYES = Util.memoize((p_173253_) -> {
		RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173253_, false, false);
		return RenderType.create("enchanted_eyes", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER).setTextureState(renderstateshard$texturestateshard).setCullState(NO_CULL).setTransparencyState(TRANSLUCENT_TRANSPARENCY).createCompositeState(false));
	});

	public final RenderType render_types;

	public EnchantedEyesLayer(RenderLayerParent<T, M> p_116964_, RenderType renderType) {
		super(p_116964_);

		this.render_types = renderType;
	}

	@Override
	public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, T p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
		if (p_116986_ instanceof IEnchantCap cap) {
			if (cap.getEnchantCap().hasEnchant() && !EnchantConfig.CLIENT.disableEyeRender.get()) {
				VertexConsumer ivertexbuilder = p_116984_.getBuffer(this.renderType());
				this.getParentModel().renderToBuffer(p_116983_, ivertexbuilder, p_116985_, OverlayTexture.NO_OVERLAY);
			}
		}
		;
	}

	public static RenderType enchantedEyes(ResourceLocation p_110455_) {
		return ENCHANTED_EYES.apply(p_110455_);
	}

	public RenderType renderType() {
		return render_types;
	}
}