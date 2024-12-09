package baguchi.enchantwithmob.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class GeoEnchantAuraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
/*
	private final EnchantedAuraGeoModel modelProvider;
	protected float widthScale = 1;
	protected float heightScale = 1;
*/

	public GeoEnchantAuraLayer(RenderLayerParent<T, M> p_i50947_1_) {
		super(p_i50947_1_);
		//this.modelProvider = new EnchantedAuraGeoModel();
	}

	@Override
	public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {

	}

	/*
	public void render(PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float tick = (float) entitylivingbaseIn.tickCount + partialTicks;
		entitylivingbaseIn.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			if (cap.hasEnchant() && !EnchantConfig.CLIENT.disableAuraRender.get()) {
				float f = (float) entitylivingbaseIn.tickCount + partialTicks;
				poseStackIn.pushPose();
				poseStackIn.mulPose(Vector3f.YP.rotationDegrees(f * 15F));
				poseStackIn.scale(-1.0F, -1.0F, 1.0F);
				poseStackIn.translate(0.0D, (double) -1.501F, 0.0D);
				GeoModel model = modelProvider.getModel(modelProvider.getModelResource(entitylivingbaseIn));
				RenderType renderType = RenderType.entityTranslucentEmissive(this.modelProvider.getTextureResource(entitylivingbaseIn));
				if (!entitylivingbaseIn.isInvisibleTo(Minecraft.getInstance().player))
					render(model, entitylivingbaseIn, partialTicks, renderType, poseStackIn, bufferIn, null, packedLightIn,
							getPackedOverlay(entitylivingbaseIn, 0), 1.0f,
							(float) 1.0f, (float) 1.0f,
							(float) 1.0f);
				poseStackIn.popPose();
			}
		});
	}


	public static int getPackedOverlay(Entity livingEntityIn, float uIn) {
		return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(false));
	}

	protected MultiBufferSource rtb = null;

	@Override
	public MultiBufferSource getCurrentRTB() {
		return rtb;
	}

	@Override
	public void setCurrentRTB(MultiBufferSource rtb) {
		this.rtb = rtb;
	}

	@Override
	public GeoModelProvider<T> getGeoModelProvider() {
		return this.modelProvider;
	}

	@Override
	public ResourceLocation getTextureLocation(Object instance) {
		return this.modelProvider.getTextureResource(instance);
	}

	@Override
	public float getWidthScale(Object animatable2) {
		return this.widthScale;
	}

	@Override
	public float getHeightScale(Object entity) {
		return this.heightScale;
	}*/
}