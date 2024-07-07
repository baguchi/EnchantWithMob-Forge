package baguchan.enchantwithmob;

import baguchan.enchantwithmob.client.ModParticles;
import baguchan.enchantwithmob.command.MobEnchantingCommand;
import baguchan.enchantwithmob.message.*;
import baguchan.enchantwithmob.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EnchantWithMob.MODID)
public class EnchantWithMob {

	// Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "enchantwithmob";
    public static final String NETWORK_PROTOCOL = "2";


    public EnchantWithMob(ModContainer modContainer, IEventBus modEventBus) {
        // Register the setup method for modloading
		modEventBus.addListener(this::preSetup);
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::setupPackets);
		MobEnchants.MOB_ENCHANT.register(modEventBus);
		ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModDataCompnents.DATA_COMPONENT_TYPES.register(modEventBus);
		ModItems.ITEM_REGISTRY.register(modEventBus);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
		ModLootItemFunctions.LOOT_REGISTRY.register(modEventBus);
		ModCapability.ATTACHMENT_TYPES.register(modEventBus);
		ModSounds.SOUND_EVENTS.register(modEventBus);
		ModParticles.PARTICLE_TYPES.register(modEventBus);

		NeoForge.EVENT_BUS.addListener(this::registerCommands);


        modContainer.registerConfig(ModConfig.Type.COMMON, EnchantConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, EnchantConfig.CLIENT_SPEC);
	}

	private void preSetup(final FMLConstructModEvent event) {
	}

	private void setup(final FMLCommonSetupEvent event) {
	}

    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playBidirectional(AncientMessage.TYPE, AncientMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(MobEnchantedMessage.TYPE, MobEnchantedMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(MobEnchantFromOwnerMessage.TYPE, MobEnchantFromOwnerMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(RemoveAllMobEnchantMessage.TYPE, RemoveAllMobEnchantMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(RemoveMobEnchantOwnerMessage.TYPE, RemoveMobEnchantOwnerMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(SoulParticleMessage.TYPE, SoulParticleMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
	}

    public static ResourceLocation prefix(String path) {
		return ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, path);
    }


	private void registerCommands(RegisterCommandsEvent evt) {
		MobEnchantingCommand.register(evt.getDispatcher());
	}
}
