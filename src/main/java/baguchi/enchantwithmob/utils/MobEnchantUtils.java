package baguchi.enchantwithmob.utils;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.attachment.MobEnchantContent;
import baguchi.enchantwithmob.item.mobenchant.ItemMobEnchantments;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.registry.ModTags;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MobEnchantUtils {
    public static final String TAG_MOB_ENCHANT = "MobEnchant";
    public static final String TAG_ENCHANT_LEVEL = "EnchantLevel";
    public static final String TAG_STORED_MOB_ENCHANTS = "StoredMobEnchants";

    //when projectile Shooter has mob enchant, start Runnable
    public static void executeIfPresent(Entity entity, ResourceKey<MobEnchant> mobEnchantment, Runnable runnable) {
        if (entity != null) {
            MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);

            if (MobEnchantUtils.findMobEnchantFromHandler(attachment.getMobEnchants(), mobEnchantment)) {
                runnable.run();
            }

        }
    }

    public static void executeIfPresent(MobEnchantAttachment cap, ResourceKey<MobEnchant> mobEnchantment, Runnable runnable) {
        if (MobEnchantUtils.findMobEnchantFromHandler(cap.getMobEnchants(), mobEnchantment)) {
            runnable.run();
        }
    }

    public static void executeIfPresent(Entity entity, Runnable runnable) {
        if (entity != null) {
            MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);
            if (attachment.hasEnchant()) {
                runnable.run();
            }
        }
    }

    /**
     * get MobEnchant From NBT
     *
     * @param tag nbt tag
     */
    public static Optional<Holder.Reference<MobEnchant>> getEnchantFromNBT(@Nullable CompoundTag tag, RegistryAccess registryAccess) {
        if (tag != null && registryAccess.lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).containsKey(Identifier.tryParse(tag.getStringOr(TAG_MOB_ENCHANT, "")))) {
            return registryAccess.lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).get(Identifier.tryParse(tag.getStringOr(TAG_MOB_ENCHANT, "")));
        } else {
            return Optional.empty();
        }
    }


    /**
     * get MobEnchant From String
     *
     * @param id MobEnchant id
     */

    /**
     * check ItemStack has Mob Enchant
     *
     * @param stack MobEnchanted Item
     */
    public static boolean hasMobEnchant(ItemStack stack) {
        @Nullable ItemMobEnchantments itemMobEnchantments = stack.get(ModDataCompnents.MOB_ENCHANTMENTS);
        return itemMobEnchantments != null;
    }

    /**
     * check NBT has Mob Enchant
     *
     * @param valueInput nbt tag
     */
    public static ValueInput.TypedInputList<MobEnchantContent> getEnchantmentList(ValueInput valueInput) {
        return valueInput.listOrEmpty(TAG_STORED_MOB_ENCHANTS, MobEnchantContent.CODEC);
    }


    /*
     * item Mob Enchant Start
     */
    public static ItemMobEnchantments getEnchantmentsForCrafting(ItemStack stack) {
        return stack.getOrDefault(ModDataCompnents.MOB_ENCHANTMENTS.get(), ItemMobEnchantments.EMPTY);
    }

    public static void setEnchantments(ItemStack stack, ItemMobEnchantments p_332148_) {
        stack.set(ModDataCompnents.MOB_ENCHANTMENTS.get(), p_332148_);
    }

    public static boolean canStoreEnchantments(ItemStack p_330666_) {
        return p_330666_.has(ModDataCompnents.MOB_ENCHANTMENTS.get());
    }

    //if using make mob enchant. use this
    public static void enchant(Holder<MobEnchant> p_41664_, ItemStack stack, int p_41665_) {
        updateEnchantments(stack, p_330091_ -> p_330091_.upgrade(p_41664_.value(), p_41665_));
    }

    public static ItemMobEnchantments updateEnchantments(ItemStack p_331034_, Consumer<ItemMobEnchantments.Mutable> p_332031_) {
        DataComponentType<ItemMobEnchantments> datacomponenttype = ModDataCompnents.MOB_ENCHANTMENTS.get();
        ItemMobEnchantments itemenchantments = p_331034_.getOrDefault(datacomponenttype, ItemMobEnchantments.EMPTY);

        ItemMobEnchantments.Mutable itemenchantments$mutable = new ItemMobEnchantments.Mutable(itemenchantments);
        p_332031_.accept(itemenchantments$mutable);
        ItemMobEnchantments itemenchantments1 = itemenchantments$mutable.toImmutable();
        p_331034_.set(datacomponenttype, itemenchantments1);
        return itemenchantments1;
    }

    /*
     * item Mob Enchant End
     */


    /**
     * add Mob Enchantments From ItemStack
     *
     * @param itemIn     MobEnchanted Item
     * @param entity     Enchanting target
     * @param capability MobEnchant Capability
     */
    public static boolean addItemMobEnchantToEntity(ItemStack itemIn, LivingEntity entity, LivingEntity user, MobEnchantAttachment capability) {
        ItemMobEnchantments itemMobEnchantments = getEnchantmentsForCrafting(itemIn);
        boolean flag = false;

        for (Holder<MobEnchant> mobEnchant : itemMobEnchantments.keySet()) {
            int level = itemMobEnchantments.getLevel(mobEnchant.value());
            if (checkAllowMobEnchantFromMob(mobEnchant, entity, capability)) {
                capability.addMobEnchant(entity, mobEnchant, level);
                flag = true;

            }
        }
        return flag;
    }

    public static boolean addUnstableItemMobEnchantToEntity(ItemStack itemIn, LivingEntity entity, LivingEntity owner, MobEnchantAttachment capability) {
        ItemMobEnchantments itemMobEnchantments = getEnchantmentsForCrafting(itemIn);
        boolean flag = false;

        for (Holder<MobEnchant> mobEnchant : itemMobEnchantments.keySet()) {
            int level = itemMobEnchantments.getLevel(mobEnchant.value());
            if (checkAllowMobEnchantFromMob(mobEnchant, entity, capability)) {
                capability.addMobEnchantFromOwner(entity, mobEnchant, level, owner);
                flag = true;
            }
        }
        return flag;
    }

    public static void removeMobEnchantToEntity(LivingEntity entity, MobEnchantAttachment capability) {
        capability.removeAllMobEnchant(entity);
    }

    public static int getExperienceFromMob(MobEnchantAttachment cap) {
        int l = 0;
        for (MobEnchantContent list : cap.getMobEnchants()) {
            Holder<MobEnchant> enchantment = list.getMobEnchant();
            int integer = list.getEnchantLevel();
            l += enchantment.value().getMinEnchantability(integer);
        }
        return l;
    }

    /**
     * add Mob Enchantments To Entity
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param data         MobEnchant Data
     */
    public static boolean addEnchantmentToEntity(LivingEntity livingEntity, MobEnchantAttachment capability, MobEnchantmentData data) {
        boolean flag = false;
        if (checkAllowMobEnchantFromMob(data.enchantment, livingEntity, capability)) {
            capability.addMobEnchant(livingEntity, data.enchantment, data.enchantmentLevel);
            flag = true;
        }
        return flag;
    }

    /**
     * add Mob Enchantments To Entity
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param random       Random
     * @param level        max limit level MobEnchant
     */
    public static boolean addRandomEnchantmentToEntity(LivingEntity livingEntity, MobEnchantAttachment capability, RandomSource random, int level, TagKey<MobEnchant> mobEnchantTagKey) {
        List<MobEnchantmentData> list = getSpawnEnchantmentList(livingEntity.registryAccess(), random, level, mobEnchantTagKey);
        ;

        boolean flag = false;
        for (MobEnchantmentData enchantmentdata : list) {
            if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
                capability.addMobEnchant(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * add Mob Enchantments To Entity
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param random       Random
     * @param level        max limit level MobEnchant
     */
    public static boolean addRandomEnchantmentToEntity(LivingEntity livingEntity, MobEnchantAttachment capability, RandomSource random, int level) {

        return addRandomEnchantmentToEntity(livingEntity, capability, random, level, ModTags.MobEnchantTags.RANDOM_SPAWN);
    }

    /**
     * add Mob Enchantments To Entity(but unstable enchant)
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param random       Random
     * @param level        max limit level MobEnchant
     */
    public static boolean addUnstableRandomEnchantmentToEntity(LivingEntity livingEntity, LivingEntity ownerEntity, MobEnchantAttachment capability, RandomSource random, int level, TagKey<MobEnchant> mobEnchantTagKey) {
        List<MobEnchantmentData> list = getSpawnEnchantmentList(livingEntity.registryAccess(), random, level, mobEnchantTagKey);

        boolean flag = false;

        for (MobEnchantmentData enchantmentdata : list) {
            if (checkAllowMobEnchantFromMob(enchantmentdata.enchantment, livingEntity, capability)) {
                capability.addMobEnchantFromOwner(livingEntity, enchantmentdata.enchantment, enchantmentdata.enchantmentLevel, ownerEntity);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * add Mob Enchantments To Entity(but unstable enchant)
     *
     * @param livingEntity Enchanting target
     * @param capability   MobEnchant Capability
     * @param random       Random
     * @param level        max limit level MobEnchant
     */
    public static boolean addUnstableRandomEnchantmentToEntity(LivingEntity livingEntity, LivingEntity ownerEntity, MobEnchantAttachment capability, RandomSource random, int level) {
        return addUnstableRandomEnchantmentToEntity(livingEntity, ownerEntity, capability, random, level, ModTags.MobEnchantTags.RANDOM_SPAWN);
    }

    public static List<MobEnchantmentData> getSpawnEnchantmentList(RegistryAccess registryAccess, RandomSource random, int cost, TagKey<MobEnchant> mobEnchantTagKey) {
        Optional<HolderSet.Named<MobEnchant>> optional = registryAccess.lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).get(mobEnchantTagKey);
        if (optional.isEmpty()) {
            return List.of();
        } else {
            List<MobEnchantmentData> list = MobEnchantUtils.buildEnchantmentList(registryAccess, random, cost, optional.get().stream());

            return list;
        }
    }

    public static ItemStack addRandomEnchantmentToItemStack(RandomSource random, RegistryAccess registryAccess, ItemStack stack, int level, Stream<Holder<MobEnchant>> possibleEnchantments) {
        List<MobEnchantmentData> list = buildEnchantmentList(registryAccess, random, level, possibleEnchantments);

        for (MobEnchantmentData enchantmentdata : list) {
            enchant(enchantmentdata.enchantment, stack, enchantmentdata.enchantmentLevel);

        }

        return stack;
    }

    public static boolean findMobEnchantHandler(List<MobEnchantContent> list, Holder<MobEnchant> findMobEnchant) {
        for (MobEnchantContent mobEnchant : list) {
            if (mobEnchant.getMobEnchant().equals(findMobEnchant)) {
                return true;
            }
        }
        return false;
    }

    public static boolean findMobEnchant(List<Holder<MobEnchant>> list, Holder<MobEnchant> findMobEnchant) {
        return list.contains(findMobEnchant);
    }

    public static boolean findMobEnchantFromHandler(List<MobEnchantContent> list, ResourceKey<MobEnchant> findMobEnchant) {
        for (MobEnchantContent mobEnchant : list) {
            if (mobEnchant != null) {
                if (mobEnchant.getMobEnchant().is(findMobEnchant)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAllowMobEnchantFromMob(@Nullable Holder<MobEnchant> mobEnchant, LivingEntity livingEntity, MobEnchantAttachment capability) {
        if (!EnchantConfig.COMMON.universalEnchant.get()) {
            if (mobEnchant != null && !mobEnchant.value().isCompatibleMob(livingEntity)) {
				return false;
            }
        }


        for (MobEnchantContent enchantHandler : capability.getMobEnchants()) {
            if (mobEnchant != null && enchantHandler.getMobEnchant() != null && (!enchantHandler.getMobEnchant().value().isCompatibleWith(mobEnchant.value()) || enchantHandler.getMobEnchant().value() == mobEnchant.value())) {
                return false;
            }
        }

        //check mob enchant is not null
        return mobEnchant != null;
    }

    public static int getMobEnchantLevelFromHandler(List<MobEnchantContent> list, ResourceKey<MobEnchant> findMobEnchant) {
        for (MobEnchantContent mobEnchant : list) {
            if (mobEnchant != null) {
                if (mobEnchant.getMobEnchant().is(findMobEnchant)) {
                    return mobEnchant.getEnchantLevel();
                }
            }
        }
        return 0;
    }

    /*
     * build MobEnchantment list like vanilla's enchantment
     */
    public static List<MobEnchantmentData> buildEnchantmentList(RegistryAccess registry, RandomSource randomIn, int level, Stream<Holder<MobEnchant>> possibleEnchantments) {
        List<MobEnchantmentData> list = Lists.newArrayList();
        int i = 1; //Cost
        if (i <= 0) {
            return list;
        } else {
            level = level + 1 + randomIn.nextInt(i / 4 + 1) + randomIn.nextInt(i / 4 + 1);
            float f = (randomIn.nextFloat() + randomIn.nextFloat() - 1.0F) * 0.15F;
            level = Mth.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE);
            List<MobEnchantmentData> list1 = getAvailableEnchantmentResults(level, possibleEnchantments);
            if (!list1.isEmpty()) {
                WeightedRandom.getRandomItem(randomIn, list1, MobEnchantmentData::weight).ifPresent(list::add);

                while (randomIn.nextInt(50) <= level) {
                    if (!list.isEmpty()) {
                        removeIncompatible(list1, list.getLast());
                    }
                    if (list1.isEmpty()) {
                        break;
                    }

                    WeightedRandom.getRandomItem(randomIn, list1, MobEnchantmentData::weight).ifPresent(list::add);
                    level /= 2;
                }
            }

            return list;
        }
    }

    /*
     * get MobEnchantment data.
     * when not allow rare enchantment,Ignore rare enchantment
     */
    public static List<MobEnchantmentData> getAvailableEnchantmentResults(int level, Stream<Holder<MobEnchant>> possibleEnchantments) {
        List<MobEnchantmentData> list = Lists.newArrayList();
        // Neo: Rewrite filter logic to call isPrimaryItemFor instead of hardcoded vanilla logic.
        // The original logic is recorded in the default implementation of IItemExtension#isPrimaryItemFor.
        possibleEnchantments.forEach(p_344478_ -> {
            MobEnchant enchantment = p_344478_.value();

            for (int i = enchantment.getMaxLevel(); i >= enchantment.getMinLevel(); i--) {
                if (level >= enchantment.getMinEnchantability(i) && level <= enchantment.getMaxEnchantability(i)) {
                    list.add(new MobEnchantmentData(p_344478_, i));
                    break;
                }
            }
        });
        return list;
    }

    private static void removeIncompatible(List<MobEnchantmentData> dataList, MobEnchantmentData data) {
        Iterator<MobEnchantmentData> iterator = dataList.iterator();

        while (iterator.hasNext()) {
            if (!data.enchantment.value().isCompatibleWith((iterator.next()).enchantment.value())) {
                iterator.remove();
            }
        }

    }

    public static float modifyDamage(ServerLevel level, Entity entity, DamageSource damageSource, float damage) {
        MutableFloat mutablefloat = new MutableFloat(damage);
        runIterationOnEntity(
                entity, (p_344525_, p_344526_) -> p_344525_.value().modifyDamage(level, p_344526_, entity, damageSource, mutablefloat)
        );
        return mutablefloat.floatValue();
    }

    public static float getDamageProtection(ServerLevel level, LivingEntity entity, DamageSource damageSource) {
        MutableFloat mutablefloat = new MutableFloat(0.0F);
        runIterationOnEntity(
                entity,
                (p_344604_, p_344605_) -> p_344604_.value()
                        .modifyDamageProtection(level, p_344605_, entity, damageSource, mutablefloat)
        );
        return mutablefloat.floatValue();
    }

    public static void runIterationOnEntity(Entity entity, MobEnchantUtils.MobEnchantmentVisitor visitor) {

        MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);

        for (MobEnchantContent handler : attachment.getMobEnchants()) {
            visitor.accept(handler.getMobEnchant(), handler.getEnchantLevel());
        }

    }

    @FunctionalInterface
    public interface MobEnchantmentVisitor {
        void accept(Holder<MobEnchant> enchantment, int level);
    }
}