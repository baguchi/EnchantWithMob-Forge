package baguchi.enchantwithmob.capability;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import baguchi.enchantwithmob.message.*;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.ModTags;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


public class MobEnchantCapability {
    private static final Identifier HEALTH_MODIFIER_NAME = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "health_boost");

    private static final AttributeModifier HEALTH_MODIFIER = new AttributeModifier(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "health_boost"), 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);


	private List<MobEnchantHandler> mobEnchants = Lists.newArrayList();
	@Nullable
	private LivingEntity enchantOwner;
	private boolean fromOwner;
    private Holder<MobEnchantType> mobEnchantType;

    private final RegistryAccess registryAccess;

    public MobEnchantCapability(RegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
        this.mobEnchantType = registryAccess.lookupOrThrow(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY).getOrThrow(MobEnchantTypes.NORMAL);
    }


	/**
	 * add MobEnchant on Entity
	 *
	 * @param entity       Entity given a MobEnchant
	 * @param mobEnchant   Mob Enchant attached to mob
	 * @param enchantLevel Mob Enchant Level
	 */
	public void addMobEnchant(LivingEntity entity, Holder<MobEnchant> mobEnchant, int enchantLevel) {

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
        if (!entity.level().isClientSide()) {
			MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		}
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		//Sync Client Enchant
		//size changed like minecraft dungeons
		entity.refreshDimensions();
	}

    public void setEnchantType(LivingEntity entity, ResourceKey<MobEnchantType> enchantType) {
        this.mobEnchantType = registryAccess.getOrThrow(enchantType);
        if (!entity.level().isClientSide()) {
            MobEnchantTypeMessage message = new MobEnchantTypeMessage(entity, enchantType);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		}
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

		this.mobEnchants.add(new MobEnchantHandler(mobEnchant, enchantLevel));
        if (!entity.level().isClientSide()) {
			MobEnchantedMessage message = new MobEnchantedMessage(entity, mobEnchant, enchantLevel);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		}
		this.addOwner(entity, owner);
		this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
		entity.refreshDimensions();
	}

	public void addOwner(LivingEntity entity, LivingEntity owner) {
		this.fromOwner = true;
		this.enchantOwner = owner;
        if (!entity.level().isClientSide()) {
			MobEnchantFromOwnerMessage message = new MobEnchantFromOwnerMessage(entity, owner);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		}
	}
	public void removeOwner(LivingEntity livingEntity) {
		this.fromOwner = false;
		this.enchantOwner = null;
		//Sync Client Enchant
        if (!livingEntity.level().isClientSide()) {
			RemoveMobEnchantOwnerMessage message = new RemoveMobEnchantOwnerMessage(livingEntity);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, message);
		}
	}

	/*
	 * Remove MobEnchant on Entity
	 */
	public void removeAllMobEnchant(LivingEntity entity) {

		for (int i = 0; i < mobEnchants.size(); ++i) {
			this.onRemoveEnchantEffect(entity, mobEnchants.get(i).getMobEnchant(), mobEnchants.get(i).getEnchantLevel());
		}
		//Sync Client Enchant
        if (!entity.level().isClientSide()) {
			RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		}
        this.mobEnchants.clear();
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
		//Sync Client Enchant
        if (!entity.level().isClientSide()) {
			RemoveAllMobEnchantMessage message = new RemoveAllMobEnchantMessage(entity);
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		}
        this.mobEnchants.clear();
		this.removeOwner(entity);
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
	}

	public List<MobEnchantHandler> getMobEnchants() {
		return mobEnchants;
	}

	public boolean hasEnchant() {
		return !this.mobEnchants.isEmpty();
	}

	@Nullable
	public LivingEntity getEnchantOwner() {
		return enchantOwner;
	}

	public boolean hasOwner() {
		return this.enchantOwner != null && this.enchantOwner.isAlive();
	}

	//check this enchant from owner
	public boolean isFromOwner() {
		return this.fromOwner;
	}

    public Holder<@NotNull MobEnchantType> getMobEnchantType() {
        return mobEnchantType;
	}

    public boolean isPreventRemoveSelf() {
        return mobEnchantType.is(ModTags.MobEnchantTypeTags.PREVENT_REMOVE_SELF);
	}

	public CompoundTag serializeNBT(RegistryAccess registryAccess) {
		CompoundTag nbt = new CompoundTag();

		ListTag listnbt = new ListTag();

		for (int i = 0; i < mobEnchants.size(); i++) {
			listnbt.add(mobEnchants.get(i).writeNBT(registryAccess));
		}

		nbt.put("StoredMobEnchants", listnbt);
		nbt.putBoolean("FromOwner", fromOwner);

        nbt.putString("EnchantType", registryAccess.lookupOrThrow(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY).getKey(mobEnchantType.value()).toString());

		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt, RegistryAccess registryAccess) {
		ListTag list = MobEnchantUtils.getEnchantmentListForNBT(nbt);

		mobEnchants.clear();

		for (int i = 0; i < list.size(); ++i) {
            CompoundTag compoundnbt = list.getCompoundOrEmpty(i);

			Optional<Holder.Reference<MobEnchant>> mobEnchant = MobEnchantUtils.getEnchantFromNBT(compoundnbt, registryAccess);
			//check mob enchant is not null
            mobEnchant.ifPresent(mobEnchantReference -> mobEnchants.add(new MobEnchantHandler(mobEnchantReference, MobEnchantUtils.getEnchantLevelFromNBT(compoundnbt))));
		}

        fromOwner = nbt.getBooleanOr("FromOwner", false);
        Optional<Holder.Reference<MobEnchantType>> optional = registryAccess.lookupOrThrow(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY).get(Identifier.parse(nbt.getStringOr("EnchantType", MobEnchantTypes.NORMAL.identifier().toString())));

        optional.ifPresent(mobEnchantTypeReference -> mobEnchantType = mobEnchantTypeReference);
	}
}