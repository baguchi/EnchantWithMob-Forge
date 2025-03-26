package baguchi.enchantwithmob.item.mobenchant;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.ModRegistries;
import baguchi.enchantwithmob.registry.ModTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemMobEnchantments implements TooltipProvider {
    public static final ItemMobEnchantments EMPTY = new ItemMobEnchantments(new Object2IntOpenHashMap(), true);
    public static final int MAX_LEVEL = 255;
    private static final Codec<Integer> LEVEL_CODEC = Codec.intRange(0, 255);
    private static final Codec<Object2IntOpenHashMap<Holder<MobEnchant>>> LEVELS_CODEC;
    private static final Codec<ItemMobEnchantments> FULL_CODEC;
    public static final Codec<ItemMobEnchantments> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemMobEnchantments> STREAM_CODEC;
    final Object2IntOpenHashMap<Holder<MobEnchant>> enchantments;
    final boolean showInTooltip;

    ItemMobEnchantments(Object2IntOpenHashMap<Holder<MobEnchant>> p_341287_, boolean p_330219_) {
        this.enchantments = p_341287_;
        this.showInTooltip = p_330219_;
        ObjectIterator var3 = p_341287_.object2IntEntrySet().iterator();

        Object2IntMap.Entry entry;
        int i;
        do {
            if (!var3.hasNext()) {
                return;
            }

            entry = (Object2IntMap.Entry) var3.next();
            i = entry.getIntValue();
        } while (i >= 0 && i <= 255);

        String var10002 = String.valueOf(entry.getKey());
        throw new IllegalArgumentException("Enchantment " + var10002 + " has invalid level " + i);
    }

    private static <T> HolderSet<T> getTagOrEmpty(@Nullable HolderLookup.Provider p_341186_, ResourceKey<Registry<T>> p_341113_, TagKey<T> p_341409_) {
        if (p_341186_ != null) {
            Optional<HolderSet.Named<T>> optional = p_341186_.lookupOrThrow(p_341113_).get(p_341409_);
            if (optional.isPresent()) {
                return (HolderSet) optional.get();
            }
        }

        return HolderSet.direct(new Holder[0]);
    }

    public ItemMobEnchantments withTooltip(boolean p_335616_) {
        return new ItemMobEnchantments(this.enchantments, p_335616_);
    }

    public Set<Holder<MobEnchant>> keySet() {
        return Collections.unmodifiableSet(this.enchantments.keySet());
    }

    public Set<Object2IntMap.Entry<Holder<MobEnchant>>> entrySet() {
        return Collections.unmodifiableSet(this.enchantments.object2IntEntrySet());
    }

    public int size() {
        return this.enchantments.size();
    }

    public boolean isEmpty() {
        return this.enchantments.isEmpty();
    }

    public boolean equals(Object p_331697_) {
        if (this == p_331697_) {
            return true;
        } else {
            boolean var10000;
            if (p_331697_ instanceof ItemMobEnchantments) {
                ItemMobEnchantments itemenchantments = (ItemMobEnchantments) p_331697_;
                var10000 = this.showInTooltip == itemenchantments.showInTooltip && this.enchantments.equals(itemenchantments.enchantments);
            } else {
                var10000 = false;
            }

            return var10000;
        }
    }

    public int hashCode() {
        int i = this.enchantments.hashCode();
        return 31 * i + (this.showInTooltip ? 1 : 0);
    }

    public String toString() {
        String var10000 = String.valueOf(this.enchantments);
        return "ItemMobEnchantments{enchantments=" + var10000 + ", showInTooltip=" + this.showInTooltip + "}";
    }

    static {
        LEVELS_CODEC = Codec.unboundedMap(MobEnchant.CODEC, LEVEL_CODEC).xmap(Object2IntOpenHashMap::new, Function.identity());
        FULL_CODEC = RecordCodecBuilder.create((p_337961_) -> {
            return p_337961_.group(LEVELS_CODEC.fieldOf("levels").forGetter((p_340785_) -> {
                return p_340785_.enchantments;
            }), Codec.BOOL.optionalFieldOf("show_in_tooltip", true).forGetter((p_331891_) -> {
                return p_331891_.showInTooltip;
            })).apply(p_337961_, ItemMobEnchantments::new);
        });
        CODEC = Codec.withAlternative(FULL_CODEC, LEVELS_CODEC, (p_340783_) -> {
            return new ItemMobEnchantments(p_340783_, true);
        });
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2IntOpenHashMap::new, MobEnchant.STREAM_CODEC, ByteBufCodecs.VAR_INT), (p_340784_) -> {
            return p_340784_.enchantments;
        }, ByteBufCodecs.BOOL, (p_330450_) -> {
            return p_330450_.showInTooltip;
        }, ItemMobEnchantments::new);
    }

    public int getLevel(Holder<MobEnchant> enchantment) {
        return this.enchantments.getInt(enchantment);
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        if (this.showInTooltip) {
            HolderLookup.Provider holderlookup$provider = tooltipContext.registries();
            HolderSet<MobEnchant> holderset = getTagOrEmpty(holderlookup$provider, ModRegistries.MOB_ENCHANT, ModTags.MobEnchantTags.TOOLTIP_ORDER);

            for (Holder<MobEnchant> holder : holderset) {
                int i = this.enchantments.getInt(holder);
                if (i > 0) {
                    consumer.accept(MobEnchant.getFullname(holder, i));
                }
            }

            for (Object2IntMap.Entry<Holder<MobEnchant>> entry : this.enchantments.object2IntEntrySet()) {
                Holder<MobEnchant> holder1 = entry.getKey();
                if (!holderset.contains(holder1)) {
                    consumer.accept(MobEnchant.getFullname(entry.getKey(), entry.getIntValue()));
                }
            }
        }
    }


    public static class Mutable {
        private final Object2IntOpenHashMap<Holder<MobEnchant>> enchantments = new Object2IntOpenHashMap();
        private final boolean showInTooltip;

        public Mutable(ItemMobEnchantments p_330722_) {
            this.enchantments.putAll(p_330722_.enchantments);
            this.showInTooltip = p_330722_.showInTooltip;
        }

        public void set(Holder<MobEnchant> enchantment, int p_330832_) {
            if (p_330832_ <= 0) {
                this.enchantments.removeInt(getLevel(enchantment));
            } else {
                this.enchantments.put(enchantment, Math.min(p_330832_, 255));
            }

        }

        public void upgrade(Holder<MobEnchant> enchantment, int p_331153_) {
            if (p_331153_ > 0) {
                this.enchantments.merge(enchantment, Math.min(p_331153_, 255), Integer::max);
            }

        }

        public void removeIf(Predicate<Holder<MobEnchant>> p_332079_) {
            this.enchantments.keySet().removeIf(p_332079_);
        }

        public int getLevel(Holder<MobEnchant> p_331330_) {
            return this.enchantments.getOrDefault(p_331330_, 0);
        }

        public Set<Holder<MobEnchant>> keySet() {
            return this.enchantments.keySet();
        }

        public ItemMobEnchantments toImmutable() {
            return new ItemMobEnchantments(this.enchantments, this.showInTooltip);
        }
    }
}