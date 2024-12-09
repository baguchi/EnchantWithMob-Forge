package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import baguchi.enchantwithmob.mobenchant.effects.location.MobEnchantLocationBasedEffect;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.function.UnaryOperator;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModMobEnchantDataCompnents {

    public static final Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(() -> ModMobEnchantDataCompnents.getRegistry().byNameCodec());
    public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(ModRegistries.MOB_ENCHANT_EFFECT_COMPONENT_TYPE_REGISTRY_KEY, EnchantWithMob.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<TargetedConditionalEffect<MobEnchantEntityEffect>>>> POST_ATTACK = register(
            "post_attack",
            p_380879_ -> p_380879_.persistent(TargetedConditionalEffect.codec(MobEnchantEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<MobEnchantLocationBasedEffect>>>> LOCATION_CHANGED = register(
            "location_changed",
            p_380870_ -> p_380870_.persistent(ConditionalEffect.codec(MobEnchantLocationBasedEffect.CODEC, LootContextParamSets.ENCHANTED_LOCATION).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> DAMAGE_PROTECTION = register(
            "damage_protection",
            p_380888_ -> p_380888_.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<DamageImmunity>>>> DAMAGE_IMMUNITY = register(
            "damage_immunity", p_380885_ -> p_380885_.persistent(ConditionalEffect.codec(DamageImmunity.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> DAMAGE = register(
            "damage", p_380877_ -> p_380877_.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> KNOCKBACK = register(
            "knockback", p_380869_ -> p_380869_.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> ARMOR_EFFECTIVENESS = register(
            "armor_effectiveness",
            p_380887_ -> p_380887_.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<EnchantmentAttributeEffect>>> ATTRIBUTES = register(
            "attributes", p_345468_ -> p_345468_.persistent(EnchantmentAttributeEffect.CODEC.codec().listOf())
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<MobEnchantEntityEffect>>>> TICK = register(
            "tick", p_380878_ -> p_380878_.persistent(ConditionalEffect.codec(MobEnchantEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf())
    );


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String p_332092_, UnaryOperator<DataComponentType.Builder<T>> p_331261_) {
        return DATA_COMPONENT_TYPES.register(p_332092_, () -> p_331261_.apply(DataComponentType.builder()).build());
    }

    private static Registry<DataComponentType<?>> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(ModRegistries.MOB_ENCHANT_EFFECT_COMPONENT_TYPE_REGISTRY_KEY).sync(true));
    }

    public static Registry<DataComponentType<?>> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}