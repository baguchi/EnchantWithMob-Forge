package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantCapability;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
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
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;

import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.ANCIENT_GLINT;
import static baguchi.enchantwithmob.client.render.layer.EnchantLayer.enchantBeamSwirl;

/*
 * Base from Bumble Zone Lazer Layer
 * https://github.com/TelepathicGrunt/Bumblezone/blob/1.20-Arch/common/src/main/java/com/telepathicgrunt/the_bumblezone/client/rendering/cosmiccrystal/CosmicCrystalRenderer.java
 */

@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public static void renderEnchantBeam(RenderLivingEvent.Post<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> event) {
        PoseStack matrixStack = event.getPoseStack();
        SubmitNodeCollector bufferBuilder = event.getSubmitNodeCollector();
        float particalTick = event.getPartialTick();
		if (event.getRenderState() instanceof IEnchantCap cap) {
			if (cap.getEnchantCap().hasOwner() && cap.getEnchantCap().hasEnchant()) {

				LivingEntity entity = cap.getEnchantCap().getEnchantOwner();
                if (entity != null) {
					renderBeam(cap.getEnchantCap(), event.getRenderState(), particalTick, matrixStack, bufferBuilder, entity, event.getRenderer());
                }
            }
        }
    }

    private static void renderBeam(@NotNull MobEnchantCapability cap, LivingEntityRenderState p_229118_1_, float p_229118_2_, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Entity level, LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderer) {
		float tick = (float) p_229118_1_.ageInTicks;
        poseStack.pushPose();
        Vec3 vector3d = level.getRopeHoldPosition(p_229118_2_);
		double d0 = p_229118_1_.bodyRot * ((float) Math.PI / 180F) + (Math.PI / 2D);
		Vector3d vector3d1 = new Vector3d(0.0D, (double) p_229118_1_.eyeHeight / 2, 0.0F);
		double d1 = Math.cos(d0) * vector3d1.z + Math.sin(d0) * vector3d1.x;
		double d2 = Math.sin(d0) * vector3d1.z - Math.cos(d0) * vector3d1.x;
		double d3 = p_229118_1_.x + d1;
		double d4 = p_229118_1_.y + vector3d1.y;
		double d5 = p_229118_1_.z + d2;
        poseStack.translate(d1, vector3d1.y, d2);
		float f = (float) (vector3d.x - d3);
		float f1 = (float) (vector3d.y - d4);
		float f2 = (float) (vector3d.z - d5);
		float f3 = 0.1F;
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
		float f4 = Mth.fastInvCubeRoot(f * f + f2 * f2) * 0.1F / 2.0F;
		float f5 = f2 * f4;
		float f6 = f * f4;
		BlockPos blockpos = new BlockPos((int) p_229118_1_.x, (int) (p_229118_1_.eyeHeight + p_229118_1_.y), (int) p_229118_1_.z);
        BlockPos blockpos1 = new BlockPos((int) level.getX(), (int) (p_229118_1_.eyeHeight + level.getY()), (int) level.getZ());
        int i = getBlockLightLevel(level.level(), p_229118_1_, blockpos);
        int j = getBlockLightLevel(level, blockpos1);
        int k = level.level().getBrightness(LightLayer.SKY, blockpos);
        int l = level.level().getBrightness(LightLayer.SKY, blockpos1);

        submitNodeCollector.submitCustomGeometry(poseStack, enchantBeamSwirl(cap.isAncient() ? ANCIENT_GLINT : ItemRenderer.ENCHANTED_GLINT_ARMOR), (pose, vertexConsumer) -> {
            renderSide(vertexConsumer, matrix4f, pose, f, f1, f2, i, j, k, l, 0.05F, 0.1F, f5, f6);
            renderSide(vertexConsumer, matrix4f, pose, f, f1, f2, i, j, k, l, 0.1F, 0.0F, f5, f6);
        });
        poseStack.popPose();
	}


	public static void renderSide(VertexConsumer p_229119_0_, Matrix4f p_229119_1_, PoseStack.Pose matrix3f, float p_229119_2_, float p_229119_3_, float p_229119_4_, int p_229119_5_, int p_229119_6_, int p_229119_7_, int p_229119_8_, float p_229119_9_, float p_229119_10_, float p_229119_11_, float p_229119_12_) {
		int i = 24;

		for (int j = 0; j < 24; ++j) {
			float f = (float) j / 23.0F;
			int k = (int) Mth.lerp(f, (float) p_229119_5_, (float) p_229119_6_);
			int l = (int) Mth.lerp(f, (float) p_229119_7_, (float) p_229119_8_);
			int i1 = LightTexture.pack(k, l);
			addVertexPair(p_229119_0_, p_229119_1_, matrix3f, i1, p_229119_2_, p_229119_3_, p_229119_4_, p_229119_9_, p_229119_10_, 24, j, false, p_229119_11_, p_229119_12_);
			addVertexPair(p_229119_0_, p_229119_1_, matrix3f, i1, p_229119_2_, p_229119_3_, p_229119_4_, p_229119_9_, p_229119_10_, 24, j + 1, true, p_229119_11_, p_229119_12_);
		}

	}

	public static void addVertexPair(VertexConsumer p_229120_0_, Matrix4f p_229120_1_, PoseStack.Pose matrix3f, int p_229120_2_, float p_229120_3_, float p_229120_4_, float p_229120_5_, float p_229120_6_, float p_229120_7_, int p_229120_8_, int p_229120_9_, boolean p_229120_10_, float p_229120_11_, float p_229120_12_) {
		float f3 = (float) p_229120_9_ / (float) p_229120_8_;
		float f4 = p_229120_3_ * f3;
		float f5 = p_229120_4_ * f3;
		float f6 = p_229120_5_ * f3;
		if (!p_229120_10_) {
			p_229120_0_.addVertex(p_229120_1_, f4 + p_229120_11_, f5 + p_229120_6_ - p_229120_7_, f6 - p_229120_12_).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(p_229120_2_).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
		}

		p_229120_0_.addVertex(p_229120_1_, f4 - p_229120_11_, f5 + p_229120_7_, f6 + p_229120_12_).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(p_229120_2_).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
		if (p_229120_10_) {
			p_229120_0_.addVertex(p_229120_1_, f4 + p_229120_11_, f5 + p_229120_6_ - p_229120_7_, f6 - p_229120_12_).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(p_229120_2_).setNormal(matrix3f, 0.0F, 1.0F, 0.0F);
		}

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

	private static void setupGlintTexturing(float p_110187_) {
		long i = Util.getMillis() * 8L;
		float f = (float) (i % 110000L) / 110000.0F;
		float f1 = (float) (i % 30000L) / 30000.0F;
		Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
		matrix4f.rotateZ(0.17453292F).scale(p_110187_);
		RenderSystem.setTextureMatrix(matrix4f);
	}

	public static Vec3 getPosition(Entity p_114803_, double p_114804_, float p_114805_) {
		double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.getX());
		double d1 = Mth.lerp(p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
		double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.getZ());
		return new Vec3(d0, d1, d2);
	}
}
