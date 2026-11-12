package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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


@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	public static final ContextKey<Vec3> ENCHANTER_POS = new ContextKey<>(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanter_pos"));
	public static final ContextKey<Vec3> ENCHANTED_MOB_POS = new ContextKey<>(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanted_mob_pos"));

	@SubscribeEvent
	public static void renderEnchantBeam(RenderLivingEvent.Post<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> event) {
		PoseStack matrixStack = event.getPoseStack();
		SubmitNodeCollector bufferBuilder = event.getSubmitNodeCollector();
		float particalTick = event.getPartialTick();
		Vec3 vec3 = event.getRenderState().getRenderData(ENCHANTER_POS);
		MobEnchantType mobEnchantType = event.getRenderState().getRenderData(EnchantLayer.MOB_ENCHANT_TYPE);

		if (event.getRenderState().getRenderDataOrDefault(EnchantLayer.ENCHANTED, false)) {
			if (vec3 != null && mobEnchantType != null) {
				renderBeam(mobEnchantType, event.getRenderState(), vec3, matrixStack, bufferBuilder, event.getRenderer());
			}
		}
	}

	private static void renderBeam(@NotNull MobEnchantType mobEnchantType, LivingEntityRenderState livingEntityRenderState, Vec3 vec3, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
		poseStack.pushPose();
		float texVOff = 0F;
		Vec3 vec32 = livingEntityRenderState.getRenderData(ENCHANTED_MOB_POS);
		poseStack.translate(0.0F, livingEntityRenderState.eyeHeight - 0.4F, 0.0F);
		renderBeam(mobEnchantType, livingEntityRenderState, poseStack, submitNodeCollector, vec3.subtract(vec32), 0, texVOff);

		poseStack.popPose();
	}

	private static void renderBeam(@NotNull MobEnchantType mobEnchantType, LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 beamVector, float timeInTicks, float texVOff) {
		float length = (float) (beamVector.length() - 0.025F);
		beamVector = beamVector.normalize();
		float xRot = (float) Math.acos(beamVector.y);
		float yRot = ((float) Math.PI / 2F) - (float) Math.atan2(beamVector.z, beamVector.x);
		poseStack.rotateDegrees(Axis.YP, yRot * (180F / (float) Math.PI));
		poseStack.rotateDegrees(Axis.XP, xRot * (180F / (float) Math.PI));
		float rot = timeInTicks * 0.05F * -1.5F;
		float colorScale = 1F * 1;
		int red = 255;
		int green = 255;
		int blue = 255;
		float rr1 = 0.1F;
		float rr2 = 0.15F;
		float wnx = Mth.cos(rot + 2.3561945F) * 0.15F;
		float wnz = Mth.sin(rot + 2.3561945F) * 0.15F;
		float enx = Mth.cos(rot + ((float) Math.PI / 4F)) * 0.15F;
		float enz = Mth.sin(rot + ((float) Math.PI / 4F)) * 0.15F;
		float wsx = Mth.cos(rot + 3.926991F) * 0.15F;
		float wsz = Mth.sin(rot + 3.926991F) * 0.15F;
		float esx = Mth.cos(rot + 5.4977875F) * 0.15F;
		float esz = Mth.sin(rot + 5.4977875F) * 0.15F;
		float wx = Mth.cos(rot + (float) Math.PI) * 0.1F;
		float wz = Mth.sin(rot + (float) Math.PI) * 0.1F;
		float ex = Mth.cos(rot + 0.0F) * 0.1F;
		float ez = Mth.sin(rot + 0.0F) * 0.1F;
		float nx = Mth.cos(rot + ((float) Math.PI / 2F)) * 0.1F;
		float nz = Mth.sin(rot + ((float) Math.PI / 2F)) * 0.1F;
		float sx = Mth.cos(rot + ((float) Math.PI * 1.5F)) * 0.1F;
		float sz = Mth.sin(rot + ((float) Math.PI * 1.5F)) * 0.1F;
		float minU = 0.0F;
		float maxU = 0.4999F;
		float minV = -1.0F + texVOff;
		float maxV = minV + length * 2.5F;
		submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.energySwirl(mobEnchantType.texture(), Mth.cos(livingEntityRenderState.ageInTicks % 1.0F * 0.02F) * 3.0F, livingEntityRenderState.ageInTicks * 0.01F % 1.0F), (pose, buffer) -> {
			vertex(buffer, pose, wx, length, wz, red, green, blue, 0.4999F, maxV);
			vertex(buffer, pose, wx, 0.0F, wz, red, green, blue, 0.4999F, minV);
			vertex(buffer, pose, ex, 0.0F, ez, red, green, blue, 0.0F, minV);
			vertex(buffer, pose, ex, length, ez, red, green, blue, 0.0F, maxV);
			vertex(buffer, pose, nx, length, nz, red, green, blue, 0.4999F, maxV);
			vertex(buffer, pose, nx, 0.0F, nz, red, green, blue, 0.4999F, minV);
			vertex(buffer, pose, sx, 0.0F, sz, red, green, blue, 0.0F, minV);
			vertex(buffer, pose, sx, length, sz, red, green, blue, 0.0F, maxV);
			float vBase = Mth.floor(timeInTicks) % 2 == 0 ? 0.5F : 0.0F;
			vertex(buffer, pose, wnx, length, wnz, red, green, blue, 0.5F, vBase + 0.5F);
			vertex(buffer, pose, enx, length, enz, red, green, blue, 1.0F, vBase + 0.5F);
			vertex(buffer, pose, esx, length, esz, red, green, blue, 1.0F, vBase);
			vertex(buffer, pose, wsx, length, wsz, red, green, blue, 0.5F, vBase);
		});
	}

	private static void vertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, float z, int red, int green, int blue, float u, float v) {
		builder.addVertex(pose, x, y, z).setColor(red, green, blue, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
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
