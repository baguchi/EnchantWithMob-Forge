package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import baguchi.enchantwithmob.mobenchant.effects.location.MobEnchantLocationBasedEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

public class ModRegistries {

    public static final ResourceKey<Registry<MobEnchant>> MOB_ENCHANT = createRegistryKey(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant"));

    public static final ResourceKey<Registry<MapCodec<? extends MobEnchantLocationBasedEffect>>> MOB_ENCHANT_LOCATION_BASED_EFFECT_REGISTRY_KEY = createRegistryKey(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant_value_effect_type"));


    public static final ResourceKey<Registry<MapCodec<? extends MobEnchantEntityEffect>>> MOB_ENCHANT_ENTITY_EFFECT_REGISTRY_KEY = createRegistryKey(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant_entity_effect"));

    public static final ResourceKey<Registry<DataComponentType<?>>> MOB_ENCHANT_EFFECT_COMPONENT_TYPE_REGISTRY_KEY = createRegistryKey(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "mob_enchant_effect_component_type"));
}
