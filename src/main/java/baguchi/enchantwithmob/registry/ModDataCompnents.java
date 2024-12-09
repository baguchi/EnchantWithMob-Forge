package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.item.mobenchant.ItemMobEnchantments;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataCompnents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, EnchantWithMob.MODID);


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemMobEnchantments>> MOB_ENCHANTMENTS = register("mob_enchantments", (p_341840_) -> {
        return p_341840_.persistent(ItemMobEnchantments.CODEC).networkSynchronized(ItemMobEnchantments.STREAM_CODEC).cacheEncoding();
    });

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> EXPERIENCE = register("experience", (p_341840_) -> {
        return p_341840_.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding();
    });

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String p_332092_, UnaryOperator<DataComponentType.Builder<T>> p_331261_) {
        return DATA_COMPONENT_TYPES.register(p_332092_, () -> p_331261_.apply(DataComponentType.builder()).build());
    }
}