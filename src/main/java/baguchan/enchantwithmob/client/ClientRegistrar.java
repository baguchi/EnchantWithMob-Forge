package baguchan.enchantwithmob.client;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.client.model.EnchantedWindModel;
import baguchan.enchantwithmob.client.model.EnchanterClothesModel;
import baguchan.enchantwithmob.client.model.EnchanterModel;
import baguchan.enchantwithmob.client.overlay.MobEnchantOverlay;
import baguchan.enchantwithmob.client.render.EnchanterRenderer;
import baguchan.enchantwithmob.client.render.layer.EnchantLayer;
import baguchan.enchantwithmob.client.render.layer.EnchantedEyesLayer;
import baguchan.enchantwithmob.client.render.layer.EnchantedWindLayer;
import baguchan.enchantwithmob.client.render.layer.SlimeEnchantLayer;
import baguchan.enchantwithmob.compat.GeckoLibCompat;
import baguchan.enchantwithmob.compat.GeckoLibCompatClient;
import baguchan.enchantwithmob.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {
	private static final RenderType BLAZE_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_blaze_eyes.png"));
	private static final RenderType CREEPER_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_creeper_eyes.png"));
	private static final RenderType EVOKER_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_evoker_eyes.png"));
	private static final RenderType PILLAGER_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_pillager_eyes.png"));
	private static final RenderType SKELETON_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_skeleton_eyes.png"));
	private static final RenderType SLIME_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_slime_eyes.png"));
	private static final RenderType SPIDER_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_spider_eyes.png"));
	private static final RenderType VEX_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_vex_eyes.png"));
	private static final RenderType VINDICATOR_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_vindicator_eyes.png"));
	private static final RenderType WITCH_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_witch_eyes.png"));
	private static final RenderType WOLF_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_wolf_eyes.png"));
	private static final RenderType ZOMBIE_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_zombie_eyes.png"));
	private static final RenderType GUARDIAN_EYES = EnchantedEyesLayer.enchantedEyes(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchant_eye/enchanted_guardian_eyes.png"));

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.ENCHANTER.get(), EnchanterRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ENCHANTER, EnchanterModel::createBodyLayer);
		event.registerLayerDefinition(ModModelLayers.ENCHANTED_WIND, EnchantedWindModel::createWindBodyLayer);
		event.registerLayerDefinition(ModModelLayers.ENCHANTER_CLOTHES, EnchanterClothesModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.AddLayers event) {
		event.getContext().getEntityRenderDispatcher().getSkinMap().forEach((model, player) ->
		{
			if (event.getSkin(model) != null) {
				if (player instanceof LivingEntityRenderer) {
					((LivingEntityRenderer<?, ?>) player).addLayer(new EnchantLayer(event.getSkin(model)));
					((LivingEntityRenderer<?, ?>) player).addLayer(new EnchantedWindLayer(event.getSkin(model), event.getEntityModels()));

				}
			}
		});

		Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(r -> {
			if (r instanceof SlimeRenderer) {
				((SlimeRenderer) r).addLayer(new SlimeEnchantLayer<>((SlimeRenderer) r, event.getEntityModels()));
			}

			if (r instanceof LivingEntityRenderer) {
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantLayer((LivingEntityRenderer<?, ?>) r));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedWindLayer((LivingEntityRenderer<?, ?>) r, event.getEntityModels()));

			}
			if (GeckoLibCompat.isLoaded) {
				GeckoLibCompatClient.addLayer(r);
			}


			if (r instanceof LivingEntityRenderer) {
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), BLAZE_EYES, EntityType.BLAZE));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), CREEPER_EYES, EntityType.CREEPER));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), EVOKER_EYES, EntityType.EVOKER));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), PILLAGER_EYES, EntityType.PILLAGER));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), SKELETON_EYES, (entity) -> {
					return entity instanceof AbstractSkeleton;
				}));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), SLIME_EYES, EntityType.SLIME));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), SPIDER_EYES, EntityType.SPIDER));
				//TODO Vex Eyes
				//((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), VEX_EYES, EntityType.VEX));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), VINDICATOR_EYES, EntityType.VINDICATOR));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), WITCH_EYES, EntityType.WITCH));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), WOLF_EYES, EntityType.WOLF));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), ZOMBIE_EYES, (entity) -> {
					return entity instanceof Zombie && !(entity instanceof ZombifiedPiglin);
				}));
				((LivingEntityRenderer<?, ?>) r).addLayer(new EnchantedEyesLayer(((LivingEntityRenderer<?, ?>) r), GUARDIAN_EYES, (entity) -> {
					return entity instanceof Guardian;
				}));

			}
        });
    }

    @SubscribeEvent
	public static void registerOverlay(RegisterGuiLayersEvent event) {
		event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mobenchant"), new MobEnchantOverlay());
    }
}
