package baguchi.enchantwithmob.mobenchant;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.utils.MobEnchantConfigUtils;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;

public class MobEnchant implements FeatureElement {
    private final Map<Holder<Attribute>, AttributeTemplate> attributeModifierMap = Maps.newHashMap();
    protected final Rarity rarity;
    private final int level;
    private int minlevel = 1;
    private final int anvilCost;
    private final FeatureFlagSet requiredFeatures;
    @Nullable
    private String descriptionId;

    public MobEnchant(Properties properties) {
        this.rarity = properties.enchantType;
        this.level = properties.level;
        this.anvilCost = properties.getAnvilCost();
        this.requiredFeatures = properties.requiredFeatures;
    }

    public static boolean areCompatible(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return holder.value().isCompatibleWith(holder, anotherHolder);
    }

    public Rarity getRarity() {
        return rarity;
    }

    public MobEnchant setMinLevel(int level) {
        this.minlevel = level;

        return this;
    }


    /**
     * Returns the minimum level that the enchantment can have.
     */
    public int getMinLevel() {
        return minlevel;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return level;
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 5;
    }


    public void tick(LivingEntity entity, int level) {

    }

    public void afterEnchanted(LivingEntity entity, int level) {

    }

    public final boolean isCompatibleWith(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return this.canApplyTogether(holder, anotherHolder) && anotherHolder.value().canApplyTogether(anotherHolder, holder);
    }

    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return !(livingEntity instanceof Player) || MobEnchantConfigUtils.isPlayerEnchantable(this);
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    protected boolean canApplyTogether(Holder<MobEnchant> holder, Holder<MobEnchant> anotherHolder) {
        return holder.value() != anotherHolder.value();
    }

    public MobEnchant addAttributesModifier(Holder<Attribute> p_316656_, Identifier p_350368_, double p_19475_, AttributeModifier.Operation p_19476_) {
        this.attributeModifierMap.put(p_316656_, new AttributeTemplate(p_350368_, p_19475_, p_19476_));
        return this;
    }

    public void createModifiers(int p_316803_, BiConsumer<Holder<Attribute>, AttributeModifier> p_316902_) {
        this.attributeModifierMap.forEach((p_349971_, p_349972_) -> p_316902_.accept(p_349971_, p_349972_.create(p_316803_)));
    }

    public Map<Holder<Attribute>, AttributeTemplate> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn) {
        for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance modifiableattributeinstance = attributeMapIn.getInstance(entry.getKey());
            if (modifiableattributeinstance != null) {
                modifiableattributeinstance.removeModifier(entry.getValue().id());
            }
        }

    }

    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : this.attributeModifierMap.entrySet()) {
            AttributeInstance modifiableattributeinstance = attributeMapIn.getInstance(entry.getKey());
            if (modifiableattributeinstance != null) {
                AttributeTemplate attributemodifier = entry.getValue();
                modifiableattributeinstance.removeModifier(attributemodifier.id());
                modifiableattributeinstance.addPermanentModifier(((AttributeTemplate) entry.getValue()).create(amplifier));
            }
        }
    }

    public boolean isDisabled() {
        return EnchantConfig.COMMON.DISABLE_ENCHANTS.get().contains(MobEnchants.getRegistry().getKey(this).toString());
    }

    public boolean isCursedEnchant() {
        return false;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet p_249172_) {
        return !this.isDisabled() && this.requiredFeatures().isSubsetOf(p_249172_);
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mob_enchant", MobEnchants.getRegistry().getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getFullname(int p_44701_) {
        MutableComponent mutablecomponent = Component.translatable(this.getDescriptionId());
        if (this.isCursedEnchant()) {
            mutablecomponent.withStyle(ChatFormatting.RED);
        } else {
            mutablecomponent.withStyle(ChatFormatting.AQUA);
        }

        if (p_44701_ != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + p_44701_));
        }

        return mutablecomponent;
    }

    public int getAnvilCost() {
        return this.anvilCost;
    }

    public void modifyDamageProtection(ServerLevel level, int p344605, LivingEntity entity, DamageSource damageSource, MutableFloat mutablefloat) {
    }

    public void modifyDamage(ServerLevel level, int p344526, Entity entity, DamageSource damageSource, MutableFloat mutablefloat) {
    }


    public static class Properties {
        private final Rarity enchantType;
        private final int level;
        private final int anvilCost;

        FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

        public Properties(Rarity enchantType, int level, int anvilCost) {
            this.enchantType = enchantType;
            this.level = level;
            this.anvilCost = anvilCost;
        }

        public int getAnvilCost() {
            return anvilCost;
        }

        public FeatureFlagSet getRequiredFeatures() {
            return requiredFeatures;
        }

        public Properties requiredFeatures(FeatureFlag... p_250948_) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset(p_250948_);
            return this;
        }
    }

    public static enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight) {
            this.weight = rarityWeight;
        }

        /**
         * Retrieves the weight of Rarity.
         */
        public int getWeight() {
            return this.weight;
        }


    }

    static record AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) {
        AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) {
            this.id = id;
            this.amount = amount;
            this.operation = operation;
        }

        public AttributeModifier create(int p_316614_) {
            return new AttributeModifier(this.id, this.amount * (double) (p_316614_ + 1), this.operation);
        }

        public Identifier id() {
            return this.id;
        }

        public double amount() {
            return this.amount;
        }

        public AttributeModifier.Operation operation() {
            return this.operation;
        }
    }
}