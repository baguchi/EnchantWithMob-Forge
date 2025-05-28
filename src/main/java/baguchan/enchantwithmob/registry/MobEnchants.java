package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.mobenchant.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEnchants {
	public static final DeferredRegister<MobEnchant> MOB_ENCHANT = DeferredRegister.create(new ResourceLocation(EnchantWithMob.MODID, "mob_enchant"), EnchantWithMob.MODID);


	public static final RegistryObject<MobEnchant> PROTECTION = MOB_ENCHANT.register("protection", () -> new ProtectionMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 4)));
	public static final RegistryObject<MobEnchant> TOUGH = MOB_ENCHANT.register("tough", () -> new ToughMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.ARMOR, "313644c5-ead2-4670-b9eb-0b41d59ce5a2", (double) 2.0F, AttributeModifier.Operation.ADDITION).addAttributesModifier(Attributes.ARMOR_TOUGHNESS, "8135df8f-38d9-490a-8d6f-c908fa973b34", (double) 0.5F, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEnchant> SPEEDY = MOB_ENCHANT.register("speedy", () -> new SpeedyMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.UNCOMMON, 2)).addAttributesModifier(Attributes.MOVEMENT_SPEED, "501f27a9-4a75-4c2e-a2ab-91eeed71d748", (double) 0.05F, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEnchant> STRONG = MOB_ENCHANT.register("strong", () -> new StrongMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 3)));
	public static final RegistryObject<MobEnchant> THORN = MOB_ENCHANT.register("thorn", () -> new ThornEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 3)));
	public static final RegistryObject<MobEnchant> HEALTH_BOOST = MOB_ENCHANT.register("health_boost", () -> new HealthBoostMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 5)).addAttributesModifier(Attributes.MAX_HEALTH, "f5d32c9f-2a3d-4157-bbf7-469d348ce097", 2.0D, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEnchant> POISON = MOB_ENCHANT.register("poison", () -> new PoisonMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 3)));
	public static final RegistryObject<MobEnchant> POISON_CLOUD = MOB_ENCHANT.register("poison_cloud", () -> new PoisonCloudMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 2)));
	public static final RegistryObject<MobEnchant> HUGE = MOB_ENCHANT.register("huge", () -> new HugeMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.MAX_HEALTH, "c988bca7-7fa9-4fea-bb44-c3625ac74241", 0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<MobEnchant> MULTISHOT = MOB_ENCHANT.register("multishot", () -> new MultiShotMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.RARE, 1)));

	public static final RegistryObject<MobEnchant> SOUL_STEAL = MOB_ENCHANT.register("soul_steal", () -> new SoulStealMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)));
	public static final RegistryObject<MobEnchant> DEFLECT = MOB_ENCHANT.register("deflect", () -> new DeflectMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 1)));
	public static final RegistryObject<MobEnchant> SMALL = MOB_ENCHANT.register("small", () -> new SmallMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 2)).addAttributesModifier(Attributes.MAX_HEALTH, "b4170c63-d50b-a0ee-15b7-9156c6e41940", -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));
	public static final RegistryObject<MobEnchant> FAST = MOB_ENCHANT.register("fast", () -> new FastMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 4)));
	public static final RegistryObject<MobEnchant> SLOW = MOB_ENCHANT.register("slow", () -> new SlowMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.VERY_RARE, 3)));

	private static Supplier<IForgeRegistry<MobEnchant>> registry;

	@SubscribeEvent
	public static void onNewRegistry(NewRegistryEvent event) {
		registry = event.create(new RegistryBuilder<MobEnchant>()
				.addCallback(MobEnchant.class)
				.setName(new ResourceLocation(EnchantWithMob.MODID, "mob_enchant"))
				.setDefaultKey(new ResourceLocation(EnchantWithMob.MODID, "protection")));
	}

	public static Supplier<IForgeRegistry<MobEnchant>> getRegistry() {
		if (registry == null) {
			throw new IllegalStateException("Registry not yet initialized");
		}
		return registry;
	}
}