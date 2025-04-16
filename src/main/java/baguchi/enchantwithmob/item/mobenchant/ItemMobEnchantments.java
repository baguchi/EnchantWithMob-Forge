package baguchi.enchantwithmob.item.mobenchant;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModTags;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipProvider;

import javax.annotation.Nullable;
import java.util.*;
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

    public int getLevel(MobEnchant p_330552_) {
        return this.enchantments.getInt(getHolder(p_330552_));
    }

    @Override
    public void addToTooltip(Item.TooltipContext p_341290_, Consumer<Component> p_331119_, TooltipFlag p_330400_) {
        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();

        if (this.showInTooltip) {

            HolderLookup.Provider holderlookup$provider = p_341290_.registries();
            HolderSet<MobEnchant> holderset = getTagOrEmpty(holderlookup$provider, MobEnchants.MOB_ENCHANT_REGISTRY, ModTags.MobEnchantTags.TOOLTIP_ORDER);
            Iterator var6 = holderset.iterator();

            while (var6.hasNext()) {
                Holder<MobEnchant> holder = (Holder) var6.next();
                int i = this.enchantments.getInt(holder);
                if (i > 0) {
                    holder.value().createModifiers(i, (p_331556_, p_330860_) -> list.add(new Pair<>(p_331556_, p_330860_)));

                    p_331119_.accept(((MobEnchant) holder.value()).getFullname(i));
                }
            }

            ObjectIterator var9 = this.enchantments.object2IntEntrySet().iterator();

            while (var9.hasNext()) {
                Object2IntMap.Entry<Holder<MobEnchant>> entry = (Object2IntMap.Entry) var9.next();
                Holder<MobEnchant> holder1 = (Holder) entry.getKey();
                if (!holderset.contains(holder1)) {
                    p_331119_.accept(((MobEnchant) holder1.value()).getFullname(entry.getIntValue()));
                }
            }
        }
        if (!list.isEmpty()) {
            p_331119_.accept(CommonComponents.EMPTY);
            p_331119_.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                AttributeModifier attributemodifier = pair.getSecond();
                double d1 = attributemodifier.amount();
                double d0;
                if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    d0 = attributemodifier.amount();
                } else {
                    d0 = attributemodifier.amount() * 100.0;
                }

                if (d1 > 0.0) {
                    p_331119_.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d1 < 0.0) {
                    d0 *= -1.0;
                    p_331119_.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + attributemodifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d0),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }
        }
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
        LEVELS_CODEC = Codec.unboundedMap(MobEnchants.getRegistry().holderByNameCodec(), LEVEL_CODEC).xmap(Object2IntOpenHashMap::new, Function.identity());
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
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2IntOpenHashMap::new, ByteBufCodecs.holderRegistry(MobEnchants.MOB_ENCHANT_REGISTRY), ByteBufCodecs.VAR_INT), (p_340784_) -> {
            return p_340784_.enchantments;
        }, ByteBufCodecs.BOOL, (p_330450_) -> {
            return p_330450_.showInTooltip;
        }, ItemMobEnchantments::new);
    }

    public static Holder<MobEnchant> getHolder(MobEnchant mobEnchant) {
        return MobEnchants.getRegistry().wrapAsHolder(mobEnchant);
    }

    public static class Mutable {
        private final Object2IntOpenHashMap<Holder<MobEnchant>> enchantments = new Object2IntOpenHashMap();
        private final boolean showInTooltip;

        public Mutable(ItemMobEnchantments p_330722_) {
            this.enchantments.putAll(p_330722_.enchantments);
            this.showInTooltip = p_330722_.showInTooltip;
        }

        public void set(MobEnchant p_331872_, int p_330832_) {
            if (p_330832_ <= 0) {
                this.enchantments.removeInt(getHolder(p_331872_));
            } else {
                this.enchantments.put(getHolder(p_331872_), Math.min(p_330832_, 255));
            }

        }

        public void upgrade(MobEnchant p_330536_, int p_331153_) {
            if (p_331153_ > 0) {
                this.enchantments.merge(getHolder(p_330536_), Math.min(p_331153_, 255), Integer::max);
            }

        }

        public void removeIf(Predicate<Holder<MobEnchant>> p_332079_) {
            this.enchantments.keySet().removeIf(p_332079_);
        }

        public int getLevel(MobEnchant p_331330_) {
            return this.enchantments.getOrDefault(getHolder(p_331330_), 0);
        }

        public Set<Holder<MobEnchant>> keySet() {
            return this.enchantments.keySet();
        }

        public ItemMobEnchantments toImmutable() {
            return new ItemMobEnchantments(this.enchantments, this.showInTooltip);
        }
    }
}