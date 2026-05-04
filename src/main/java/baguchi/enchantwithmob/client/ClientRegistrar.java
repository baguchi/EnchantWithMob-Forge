package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.api.MobEnchantEye;
import baguchi.enchantwithmob.capability.ItemMobEnchantCapability;
import baguchi.enchantwithmob.client.model.EnchantedWindModel;
import baguchi.enchantwithmob.client.model.EnchanterModel;
import baguchi.enchantwithmob.client.overlay.MobEnchantOverlay;
import baguchi.enchantwithmob.client.render.EnchanterRenderer;
import baguchi.enchantwithmob.client.render.layer.EnchantLayer;
import baguchi.enchantwithmob.client.render.layer.EnchantedEyesLayer;
import baguchi.enchantwithmob.client.render.layer.EnchantedWindLayer;
import baguchi.enchantwithmob.client.render.layer.SlimeEnchantLayer;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantEyes;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModEntities;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import java.util.Optional;

import static net.minecraft.client.renderer.RenderPipelines.*;


@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientRegistrar {
	public static final RenderPipeline MOB_ENCHANT =
			RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET, GLOBALS_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "pipeline/mob_enchant"))
					.withVertexShader("core/glint").withFragmentShader("core/glint").withSampler("Sampler0").withCull(false).withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false))
					.withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE)).withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS).build();
	public static final RenderPipeline MOB_ENCHANT_BEAM =
			RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET, GLOBALS_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "pipeline/mob_enchant_no_cull"))
					.withVertexShader("core/glint").withFragmentShader("core/glint").withSampler("Sampler0").withCull(false).withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE))
					.withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false))
					.withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS).build();
	public static final RenderPipeline MOB_ENCHANT_EYE =
			RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET, FOG_SNIPPET, GLOBALS_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "pipeline/mob_enchant_eye"))
					.withVertexShader("core/glint").withFragmentShader("core/glint").withSampler("Sampler0").withCull(false).withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false)).withColorTargetState(new ColorTargetState(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA))).withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS).build();

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.ENCHANTER.get(), EnchanterRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ENCHANTER, EnchanterModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.ENCHANTED_WIND, EnchantedWindModel::createWindBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.AddLayers event) {
        event.getContext().getEntityRenderDispatcher().getPlayerRenderers().forEach((model, player) ->
		{
            if (event.getPlayerRenderer(model) != null) {
				if (player instanceof LivingEntityRenderer) {
                    ((LivingEntityRenderer<?, ?, ?>) player).addLayer(new EnchantLayer(event.getPlayerRenderer(model)));
                    ((LivingEntityRenderer<?, ?, ?>) player).addLayer(new EnchantedWindLayer(event.getPlayerRenderer(model), event.getEntityModels()));

				}
			}
		});
		event.getEntityTypes().forEach(entityType -> {
			if (event.getRenderer(entityType) instanceof SlimeRenderer r) {
				(r).addLayer(new SlimeEnchantLayer<>(r, event.getEntityModels()));
			}

			if (event.getRenderer(entityType) instanceof LivingEntityRenderer r) {
				r.addLayer(new EnchantLayer(r));
				r.addLayer(new EnchantedWindLayer(r, event.getEntityModels()));

			}


			if (event.getRenderer(entityType) instanceof LivingEntityRenderer r) {
				r.addLayer(new EnchantedEyesLayer(r));
			}
		});
    }

	@SubscribeEvent
	public static void registerRenderState(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>(LivingEntityRenderer.class) {
		}, (entity, state) -> {
			if (entity instanceof IEnchantCap cap) {
				state.setRenderData(EnchantLayer.ENCHANTED, cap.getEnchantCap().hasEnchant());
				state.setRenderData(EnchantLayer.MOB_ENCHANT_TYPE, cap.getEnchantCap().getMobEnchantType().value());
				//reset
				state.setRenderData(EnchantedWindLayer.WIND, false);
				if (cap.getEnchantCap().getEnchantOwner().isPresent()) {
					LivingEntity owner = cap.getEnchantCap().getEnchantOwner().get().getEntity(entity.level(), LivingEntity.class);
					if (owner != null) {
						state.setRenderData(ClientEventHandler.ENCHANTER_POS, owner.position());
					}
				} else {
					state.setRenderData(ClientEventHandler.ENCHANTER_POS, null);
				}

				MobEnchantUtils.executeIfPresent(entity, MobEnchants.WIND.getKey(), () -> {
					state.setRenderData(EnchantedWindLayer.WIND, true);

				});
				Optional<Holder.Reference<MobEnchantEye>> enchantEye = MobEnchantEyes.getEyeVariant(entity.registryAccess(), entity.typeHolder());

				enchantEye.ifPresent(mobEnchantEye -> state.setRenderData(EnchantedEyesLayer.ENCHANT_EYE, mobEnchantEye.value().texture()));
			} else {
				ItemMobEnchantCapability capability = entity.getData(ModAttachments.ITEM_MOB_ENCHANT.get());

				if (capability != null) {
					state.setRenderData(EnchantLayer.ENCHANTED, capability.hasEnchant());
				}
			}
		});
	}

    @SubscribeEvent
	public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "mobenchant"), new MobEnchantOverlay());
    }

	@SubscribeEvent
	public static void registerPipelines(RegisterRenderPipelinesEvent event) {
		event.registerPipeline(MOB_ENCHANT);
		event.registerPipeline(MOB_ENCHANT_BEAM);
		event.registerPipeline(MOB_ENCHANT_EYE);
	}
}
