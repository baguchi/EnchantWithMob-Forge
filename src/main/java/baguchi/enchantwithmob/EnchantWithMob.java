package baguchi.enchantwithmob;

import baguchi.enchantwithmob.client.ModParticles;
import baguchi.enchantwithmob.command.MobEnchantingCommand;
import baguchi.enchantwithmob.message.*;
import baguchi.enchantwithmob.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
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


	public EnchantWithMob(ModContainer modContainer, Dist dist, IEventBus modEventBus) {

		if (dist.isClient()) {
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
		// Register the setup method for modloading
		modEventBus.addListener(this::preSetup);
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::setupPackets);


		ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModDataCompnents.DATA_COMPONENT_TYPES.register(modEventBus);
		ModItems.ITEM_REGISTRY.register(modEventBus);
		ModLootItemFunctions.LOOT_REGISTRY.register(modEventBus);
		ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
		ModSounds.SOUND_EVENTS.register(modEventBus);
		ModParticles.PARTICLE_TYPES.register(modEventBus);
		MobEnchants.MOB_ENCHANT.register(modEventBus);

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
		registrar.playToClient(AncientMessage.TYPE, AncientMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
		registrar.playToClient(MobEnchantedMessage.TYPE, MobEnchantedMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
		registrar.playToClient(MobEnchantFromOwnerMessage.TYPE, MobEnchantFromOwnerMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
		registrar.playToClient(RemoveAllMobEnchantMessage.TYPE, RemoveAllMobEnchantMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
		registrar.playToClient(RemoveMobEnchantOwnerMessage.TYPE, RemoveMobEnchantOwnerMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(SoulParticleMessage.TYPE, SoulParticleMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
	}

    public static ResourceLocation prefix(String path) {
		return ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, path);
    }


	private void registerCommands(RegisterCommandsEvent evt) {
		MobEnchantingCommand.register(evt.getDispatcher());
	}
}
