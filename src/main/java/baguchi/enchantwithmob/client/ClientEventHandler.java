package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3d;


@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	public static final ContextKey<Vec3> ENCHANTER_POS = new ContextKey<>(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanter_pos"));

	@SubscribeEvent
	public static void renderEnchantBeam(RenderLivingEvent.Post<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> event) {
		PoseStack matrixStack = event.getPoseStack();
		SubmitNodeCollector bufferBuilder = event.getSubmitNodeCollector();
		float particalTick = event.getPartialTick();
		Vec3 vec3 = event.getRenderState().getRenderData(ENCHANTER_POS);
		MobEnchantType mobEnchantType = event.getRenderState().getRenderData(EnchantLayer.MOB_ENCHANT_TYPE);

		if (event.getRenderState().getRenderDataOrDefault(EnchantLayer.ENCHANTED, false)) {
			if (vec3 != null && mobEnchantType != null) {
				renderBeam(mobEnchantType, event.getRenderState(), vec3, particalTick, matrixStack, bufferBuilder, event.getRenderer());
			}
		}
	}

	private static void renderBeam(@NotNull MobEnchantType mobEnchantType, LivingEntityRenderState livingEntityRenderState, Vec3 vec3, float p_229118_2_, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
		poseStack.pushPose();
		Vec3 vector3d = vec3;
		double d0 = livingEntityRenderState.bodyRot * ((float) Math.PI / 180F) + (Math.PI / 2D);
		Vector3d vector3d1 = new Vector3d(0.0D, (double) livingEntityRenderState.eyeHeight / 2, 0.0F);
		double d1 = Math.cos(d0) * vector3d1.z + Math.sin(d0) * vector3d1.x;
		double d2 = Math.sin(d0) * vector3d1.z - Math.cos(d0) * vector3d1.x;
		double d3 = livingEntityRenderState.x + d1;
		double d4 = livingEntityRenderState.y + vector3d1.y;
		double d5 = livingEntityRenderState.z + d2;
		poseStack.translate(d1, vector3d1.y, d2);
		float dx = (float) (vector3d.x - d3);
		float dy = (float) (vector3d.y - d4);
		float dz = (float) (vector3d.z - d5);
		float offsetFactor = Mth.invSqrt(dx * dx + dz * dz) * 0.05F / 2.0F;
		float dxOff = dz * offsetFactor;
		float dzOff = dx * offsetFactor;

		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.energySwirl(mobEnchantType.texture(), livingEntityRenderState.ageInTicks % 1.0F, livingEntityRenderState.ageInTicks * 0.01F % 1.0F), (pose, vertexConsumer) -> {
			int lightCoords = livingEntityRenderState.lightCoords;


			for (int k = 0; k <= 24; k++) {
				addVertexPair(vertexConsumer, pose.pose(), pose, lightCoords, dx, dy, dz, k, dxOff, dzOff);
			}

			for (int k = 24; k >= 0; k--) {
				addVertexPair(vertexConsumer, pose.pose(), pose, lightCoords, dx, dy, dz, k, dxOff, dzOff);
			}
		});
		poseStack.popPose();
	}


	public static void addVertexPair(VertexConsumer vertexConsumer, Matrix4f matrix4f, PoseStack.Pose matrix3f, int light, float x, float y, float z, int i, float dxOff, float dzOff) {
		float progress = (float) i / (float) 24.0F;
		float f4 = x * progress;
		float f5 = y * progress;
		float f6 = z * progress;
		float fudge = 0.05F;
		vertexConsumer.addVertex(matrix4f, f4 - dxOff, f5 - fudge, f6 - dzOff).setColor(236F / 255F, 150 / 255F, 254F / 255F, 0.8F).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
		vertexConsumer.addVertex(matrix4f, f4 + dxOff, y - fudge, f6 - dzOff).setColor(236F / 255F, 150 / 255F, 254F / 255F, 0.8F).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);

		vertexConsumer.addVertex(matrix4f, f4 + dxOff, y + fudge, f6 + dzOff).setColor(236F / 255F, 150 / 255F, 254F / 255F, 0.8F).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
		vertexConsumer.addVertex(matrix4f, f4 - dxOff, y + fudge, f6 + dzOff).setColor(236F / 255F, 150 / 255F, 254F / 255F, 0.8F).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);


	}

	protected static int getSkyLightLevel(Entity p_239381_1_, BlockPos p_239381_2_) {
		return p_239381_1_.level().getBrightness(LightLayer.SKY, p_239381_2_);
	}

	protected static int getBlockLightLevel(Level p_225624_1_, EntityRenderState entityRenderState, BlockPos p_225624_2_) {
		return entityRenderState.displayFireAnimation ? 15 : p_225624_1_.getBrightness(LightLayer.BLOCK, p_225624_2_);
	}

	protected static int getBlockLightLevel(Entity p_225624_1_, BlockPos p_225624_2_) {
		return p_225624_1_.isOnFire() ? 15 : p_225624_1_.level().getBrightness(LightLayer.BLOCK, p_225624_2_);
	}


	public static Vec3 getPosition(Entity p_114803_, double p_114804_, float p_114805_) {
		double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.getX());
		double d1 = Mth.lerp(p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
		double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.getZ());
		return new Vec3(d0, d1, d2);
	}
}
