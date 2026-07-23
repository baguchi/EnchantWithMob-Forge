package baguchi.enchantwithmob.attachment;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.List;
import java.util.Optional;


public class MobEnchantAttachment implements ValueIOSerializable {
    private static final Identifier HEALTH_MODIFIER_NAME = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "health_boost");

    private static final AttributeModifier HEALTH_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "health_boost"), 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final Identifier SCALE_MODIFIER_NAME = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "scale_boost");

    private static final AttributeModifier SCALE_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "scale_boost"), 0.025F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);


    protected List<MobEnchantContent> mobEnchants = Lists.newArrayList();

    protected Optional<EntityReference<LivingEntity>> enchantOwner = Optional.empty();
    private Holder<MobEnchantType> mobEnchantTypeCached;
    private ResourceKey<MobEnchantType> mobEnchantTypeKey = MobEnchantTypes.NORMAL;

    public MobEnchantAttachment() {
    }

    /**
     * add MobEnchant on Entity
     *
     * @param entity       Entity given a MobEnchant
     * @param mobEnchant   Mob Enchant attached to mob
     * @param enchantLevel Mob Enchant Level
     */
    public void addMobEnchant(LivingEntity entity, Holder<MobEnchant> mobEnchant, int enchantLevel) {

        this.mobEnchants.add(new MobEnchantContent(mobEnchant, enchantLevel));

        this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
        if (!entity.level().isClientSide()) {
            entity.syncData(ModAttachments.MOB_ENCHANTS);
        }
        //Sync Client Enchant
        //size changed like minecraft dungeons
        entity.refreshDimensions();
    }

    public void setEnchantType(LivingEntity entity, ResourceKey<MobEnchantType> enchantType) {
        this.mobEnchantTypeKey = enchantType;
        this.mobEnchantTypeCached = null;
        if (!entity.level().isClientSide()) {
            entity.syncData(ModAttachments.MOB_ENCHANTS);
        }
    }

    protected void setEnchantTypeWithoutSync(ResourceKey<MobEnchantType> enchantType) {
        this.mobEnchantTypeKey = enchantType;
        this.mobEnchantTypeCached = null;
    }

    /**
     * add MobEnchant on Entity From Owner
     *
     * @param entity       Entity given a MobEnchant
     * @param mobEnchant   Mob Enchant attached to mob
     * @param enchantLevel Mob Enchant Level
     * @param owner        OwnerEntity with a mob Enchant attached to that mob
     */
    public void addMobEnchantFromOwner(LivingEntity entity, Holder<MobEnchant> mobEnchant, int enchantLevel, LivingEntity owner) {

        this.mobEnchants.add(new MobEnchantContent(mobEnchant, enchantLevel));
        this.addOwner(entity, owner);
        this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
        if (!entity.level().isClientSide()) {
            entity.syncData(ModAttachments.MOB_ENCHANTS);
        }
        entity.refreshDimensions();
    }

    public void addOwner(LivingEntity entity, LivingEntity owner) {
        EntityReference<LivingEntity> reference = EntityReference.of(owner);
        this.enchantOwner = Optional.ofNullable(reference);
    }

    public void removeOwner(LivingEntity livingEntity) {
        this.enchantOwner = Optional.empty();
    }

    /*
     * Remove MobEnchant on Entity
     */
    public void removeAllMobEnchant(LivingEntity entity) {

        for (int i = 0; i < mobEnchants.size(); ++i) {
            this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant(), mobEnchants.get(i).getEnchantLevel());
        }
        this.mobEnchants.clear();
        //Sync Client Enchant
        if (!entity.level().isClientSide()) {
            entity.syncData(ModAttachments.MOB_ENCHANTS);
        }
        //size changed like minecraft dungeons
        entity.refreshDimensions();
    }

    /*
     * Remove MobEnchant on Entity from owner
     */
    public void removeMobEnchantFromOwner(LivingEntity entity) {
        for (int i = 0; i < mobEnchants.size(); ++i) {
            this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant(), mobEnchants.get(i).getEnchantLevel());
        }

        this.mobEnchants.clear();
        this.removeOwner(entity);
        //Sync Client Enchant
        if (!entity.level().isClientSide()) {
            entity.syncData(ModAttachments.MOB_ENCHANTS);
        }
        //size changed like minecraft dungeons
        entity.refreshDimensions();
    }


    /*
     * Add Enchant Attribute
     */
    public void onNewEnchantEffect(LivingEntity entity, Holder<MobEnchant> enchant, int enchantLevel) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            enchant.value().applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel - 1);
        }
        enchant.value().afterEnchanted(entity, enchantLevel);

        if (EnchantConfig.COMMON.dungeonsLikeHealth.get()) {
            AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
            if (modifiableattributeinstance != null && !modifiableattributeinstance.hasModifier(HEALTH_MODIFIER_NAME)) {
                modifiableattributeinstance.removeModifier(HEALTH_MODIFIER_NAME);
                modifiableattributeinstance.addPermanentModifier(HEALTH_MODIFIER);
                entity.setHealth(entity.getHealth() * 1.25F);
            }
        }

        if (EnchantConfig.COMMON.changeSizeWhenEnchant.getAsBoolean()) {

            AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.SCALE);
            if (modifiableattributeinstance != null && !modifiableattributeinstance.hasModifier(SCALE_MODIFIER_NAME)) {

                modifiableattributeinstance.removeModifier(SCALE_MODIFIER_NAME);
                modifiableattributeinstance.addPermanentModifier(SCALE_MODIFIER);
            }
        }
    }

    /*
     * Changed Enchant Attribute When Enchant is Changed
     */
    public void onChangedEnchantEffect(LivingEntity entity, Holder<MobEnchant> enchant, int enchantLevel) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            enchant.value().removeAttributesModifiersFromEntity(entity, entity.getAttributes());

            enchant.value().applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel - 1);
        }
    }

    /*
     * Remove Enchant Attribute effect
     */
    protected void onRemoveEnchantEffect(LivingEntity entity, Holder<MobEnchant> enchant, int enchantLevel) {

        enchant.value().removeAttributesModifiersFromEntity(entity, entity.getAttributes());

        AttributeInstance modifiableattributeinstance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
        if (modifiableattributeinstance != null) {
            if (modifiableattributeinstance.hasModifier(HEALTH_MODIFIER_NAME)) {
                entity.setHealth(entity.getHealth() / 1.25F);
                modifiableattributeinstance.removeModifier(HEALTH_MODIFIER_NAME);
            }
        }

        AttributeInstance modifiableattributeinstance2 = entity.getAttributes().getInstance(Attributes.SCALE);
        if (modifiableattributeinstance2 != null) {
            if (modifiableattributeinstance2.hasModifier(SCALE_MODIFIER_NAME)) {
                modifiableattributeinstance2.removeModifier(SCALE_MODIFIER_NAME);
            }
        }
    }

    public List<MobEnchantContent> getMobEnchants() {
        return mobEnchants;
    }

    public boolean hasEnchant() {
        return !this.mobEnchants.isEmpty();
    }

    public Optional<EntityReference<LivingEntity>> getEnchantOwner() {
        return enchantOwner;
    }

    public boolean hasOwner() {
        return this.enchantOwner.isPresent();
    }

    public Holder<MobEnchantType> getMobEnchantType(Entity entity) {
        if (mobEnchantTypeCached != null) {
            return mobEnchantTypeCached;
        }

        // Entityが存在するレベルから RegistryAccess / RegistryLookup を取得する
        // ※コンストラクタ時ではなく、ワールドに参加した「後」に呼ばれるため安全
        RegistryAccess registryAccess = entity.level().registryAccess();

        // 1. レジストリ自体が存在するか安全に確認
        Optional<Registry<MobEnchantType>> lookupOpt =
                registryAccess.lookup(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY);

        if (lookupOpt.isPresent()) {
            HolderLookup.RegistryLookup<MobEnchantType> lookup = lookupOpt.get();

            // check is mob enchant in registry
            Optional<Holder.Reference<MobEnchantType>> holderOpt = lookup.get(this.mobEnchantTypeKey);
            if (holderOpt.isPresent()) {
                this.mobEnchantTypeCached = holderOpt.get();
                return this.mobEnchantTypeCached;
            }
        }

        //fallback
        return getAbsoluteDefault(registryAccess);
    }

    public ResourceKey<MobEnchantType> getMobEnchantTypeKey() {
        return mobEnchantTypeKey;
    }

    private Holder<MobEnchantType> getAbsoluteDefault(RegistryAccess access) {
        return access.lookupOrThrow(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY)
                .getOrThrow(MobEnchantTypes.NORMAL); // もしくは適当な最初の1要素
    }


    public boolean isPreventRemoveSelf() {
        return mobEnchantTypeCached.is(ModTags.MobEnchantTypeTags.PREVENT_REMOVE_SELF);
    }

    @Override
    public void serialize(ValueOutput output) {
        ValueOutput.TypedOutputList<MobEnchantContent> list = output.list(MobEnchantUtils.TAG_STORED_MOB_ENCHANTS, MobEnchantContent.CODEC);

        for (int i = 0; i < mobEnchants.size(); i++) {
            list.add(mobEnchants.get(i));
        }

        this.enchantOwner.ifPresent(livingEntityEntityReference -> output.store("EnchantOwner", EntityReference.codec(), livingEntityEntityReference));

        output.store("EnchantType", ResourceKey.codec(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY), this.mobEnchantTypeKey);

    }

    @Override
    public void deserialize(ValueInput input) {
        ValueInput.TypedInputList<MobEnchantContent> list = MobEnchantUtils.getEnchantmentList(input);

        mobEnchants.clear();

        list.forEach(mobEnchantReference -> mobEnchants.add(mobEnchantReference));


        this.enchantOwner = input.read("EnchantOwner", EntityReference.codec());
        Optional<ResourceKey<MobEnchantType>> optional = input.read("EnchantType", ResourceKey.codec(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY));

        optional.ifPresent(mobEnchantTypeReference -> mobEnchantTypeKey = mobEnchantTypeReference);

    }

}