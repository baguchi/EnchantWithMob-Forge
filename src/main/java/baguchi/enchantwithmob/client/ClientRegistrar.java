package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantEye;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
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
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import java.util.Optional;

import static net.minecraft.client.renderer.RenderPipelines.GLOBALS_SNIPPET;


@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class ClientRegistrar {
	public static final RenderPipeline MOB_ENCHANT =
			RenderPipeline.builder(GLOBALS_SNIPPET)
					.withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
					.withBindGroupLayout(BindGroupLayouts.FOG)
					.withLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "pipeline/mob_enchant"))
					.withVertexShader("core/glint").withFragmentShader("core/glint")
					.withPrimitiveTopology(PrimitiveTopology.QUADS)
					.withBindGroupLayout(BindGroupLayouts.SAMPLER0).withCull(false).withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false))
					.withColorTargetState(new ColorTargetState(BlendFunction.ADDITIVE)).withVertexBinding(0, DefaultVertexFormat.POSITION_TEX).build();
	public static final RenderPipeline MOB_ENCHANT_BEAM =
			RenderPipeline.builder(GLOBALS_SNIPPET)
					.withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
					.withBindGroupLayout(BindGroupLayouts.FOG)
					.withLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "pipeline/mob_enchant_no_cull"))
					.withVertexShader("core/glint").withFragmentShader("core/glint")
					.withPrimitiveTopology(PrimitiveTopology.QUADS)
					.withBindGroupLayout(BindGroupLayouts.SAMPLER0)
					.withColorTargetState(new ColorTargetState(BlendFunction.OVERLAY))
					.withCull(false)
					.withShaderDefine("APPLY_TEXTURE_MATRIX")
					.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
					.withVertexBinding(0, DefaultVertexFormat.POSITION_TEX).build();
	public static final RenderPipeline MOB_ENCHANT_EYE =
			RenderPipeline.builder(GLOBALS_SNIPPET)
					.withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
					.withBindGroupLayout(BindGroupLayouts.FOG)
					.withLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "pipeline/mob_enchant_eye"))
					.withVertexShader("core/glint").withFragmentShader("core/glint")
					.withPrimitiveTopology(PrimitiveTopology.QUADS)
					.withBindGroupLayout(BindGroupLayouts.SAMPLER0).withCull(false).withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false)).withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)).withVertexBinding(0, DefaultVertexFormat.POSITION_TEX).build();

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
			((LivingEntityRenderer<?, ?, ?>) player).addLayer(new EnchantLayer(event.getPlayerRenderer(model)));
			((LivingEntityRenderer<?, ?, ?>) player).addLayer(new EnchantedWindLayer(event.getPlayerRenderer(model), event.getEntityModels()));
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
			MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);


			state.setRenderData(EnchantLayer.ENCHANTED, attachment.hasEnchant());
			state.setRenderData(EnchantLayer.MOB_ENCHANT_TYPE, attachment.getMobEnchantType().value());
				//reset
				state.setRenderData(EnchantedWindLayer.WIND, false);
			if (attachment.getEnchantOwner().isPresent()) {
				LivingEntity ownerEntity = EntityReference.getLivingEntity(attachment.getEnchantOwner().get(), Minecraft.getInstance().player.level());
					if (ownerEntity != null) {
						state.setRenderData(ClientEventHandler.ENCHANTER_POS, ownerEntity.getEyePosition().add(0, -0.1F, 0));
					}
				} else {
					state.setRenderData(ClientEventHandler.ENCHANTER_POS, null);
				}

				MobEnchantUtils.executeIfPresent(entity, MobEnchants.WIND.getKey(), () -> {
					state.setRenderData(EnchantedWindLayer.WIND, true);

				});
				Optional<Holder.Reference<MobEnchantEye>> enchantEye = MobEnchantEyes.getEyeVariant(entity.registryAccess(), entity.typeHolder());

				enchantEye.ifPresent(mobEnchantEye -> state.setRenderData(EnchantedEyesLayer.ENCHANT_EYE, mobEnchantEye.value().texture()));
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
