package baguchi.enchantwithmob.mobenchant.effects.entity;

import baguchi.enchantwithmob.mobenchant.effects.location.MobEnchantLocationBasedEffect;
import com.google.common.collect.HashMultimap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record MobEnchantAttributeEffect(ResourceLocation id, Holder<Attribute> attribute, LevelBasedValue amount,
                                        AttributeModifier.Operation operation)
        implements MobEnchantLocationBasedEffect {
    public static final MapCodec<MobEnchantAttributeEffect> CODEC = RecordCodecBuilder.mapCodec(
            p_350198_ -> p_350198_.group(
                            ResourceLocation.CODEC.fieldOf("id").forGetter(MobEnchantAttributeEffect::id),
                            Attribute.CODEC.fieldOf("attribute").forGetter(MobEnchantAttributeEffect::attribute),
                            LevelBasedValue.CODEC.fieldOf("amount").forGetter(MobEnchantAttributeEffect::amount),
                            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(MobEnchantAttributeEffect::operation)
                    )
                    .apply(p_350198_, MobEnchantAttributeEffect::new)
    );


    public AttributeModifier getModifier(int enchantmentLevel) {
        return new AttributeModifier(this.id, (double) this.amount().calculate(enchantmentLevel), this.operation());
    }

    @Override
    public void onChangedBlock(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity owner, Entity entity, Vec3 pos, boolean applyTransientEffects) {
        if (applyTransientEffects && entity instanceof LivingEntity livingentity) {
            livingentity.getAttributes().addTransientAttributeModifiers(this.makeAttributeMap(enchantmentLevel));
        }
    }

    @Override
    public void onDeactivated(@Nullable LivingEntity owner, Entity entity, Vec3 pos, int enchantmentLevel) {
        if (entity instanceof LivingEntity livingentity) {
            livingentity.getAttributes().removeAttributeModifiers(this.makeAttributeMap(enchantmentLevel));
        }
    }

    private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int enchantmentLevel) {
        HashMultimap<Holder<Attribute>, AttributeModifier> hashmultimap = HashMultimap.create();
        hashmultimap.put(this.attribute, this.getModifier(enchantmentLevel));
        return hashmultimap;
    }

    @Override
    public MapCodec<MobEnchantAttributeEffect> codec() {
        return CODEC;
    }
}
