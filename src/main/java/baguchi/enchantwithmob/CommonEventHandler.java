package baguchi.enchantwithmob;

import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.attachment.MobEnchantContent;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import baguchi.enchantwithmob.item.mobenchant.ItemMobEnchantments;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.registry.ModItems;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import baguchi.enchantwithmob.utils.MobEnchantmentData;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static net.minecraft.world.inventory.AnvilMenu.calculateIncreasedRepairCost;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class CommonEventHandler {
    /*
     * this event handle the Alway Enchant System
     */
    @SubscribeEvent
    public static void onMobSpawn(EntityJoinLevelEvent event) {
        MobEnchantAttachment attachment = event.getEntity().getData(ModAttachments.MOB_ENCHANTS);

        if (!event.loadedFromDisk()) {
            LevelAccessor world = event.getLevel();
            if (!world.isClientSide() && world instanceof ServerLevel serverLevel && event.getEntity() instanceof LivingEntity living) {
                if (!attachment.hasEnchant()) {
                    if (isSpawnAlwayEnchantableAncientEntity(living)) {
                        int i = 0;
                        float difficultScale = serverLevel.getCurrentDifficultyAt(living.blockPosition()).getEffectiveDifficulty() - 0.2F;
                        switch (world.getDifficulty()) {
                            case EASY:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 30);

                                MobEnchantUtils.addRandomEnchantmentToEntity(living, attachment, world.getRandom(), i);
                                break;
                            case NORMAL:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(15)) * difficultScale, 1, 60);

                                MobEnchantUtils.addRandomEnchantmentToEntity(living, attachment, world.getRandom(), i);
                                break;
                            case HARD:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(20)) * difficultScale, 1, 100);

                                MobEnchantUtils.addRandomEnchantmentToEntity(living, attachment, world.getRandom(), i);
                                break;
                        }
                        living.setHealth(living.getMaxHealth());
                        attachment.setEnchantType(living, MobEnchantTypes.ANCIENT);
                    }

                    // On add MobEnchant Alway Enchantable Mob
                    if (isSpawnAlwayEnchantableEntity(living)) {
                        int i = 0;
                        float difficultScale = serverLevel.getCurrentDifficultyAt(living.blockPosition()).getEffectiveDifficulty() - 0.2F;
                        switch (world.getDifficulty()) {
                            case EASY:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

                                MobEnchantUtils.addRandomEnchantmentToEntity(living, attachment, world.getRandom(), i);
                                break;
                            case NORMAL:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

                                MobEnchantUtils.addRandomEnchantmentToEntity(living, attachment, world.getRandom(), i);
                                break;
                            case HARD:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

                                MobEnchantUtils.addRandomEnchantmentToEntity(living, attachment, world.getRandom(), i);
                                break;
                        }

                        living.setHealth(living.getMaxHealth());
                    }
                }
            }
        }
    }

    /*
     * handle the Normal Entity Mob Enchant
     */
    @SubscribeEvent
    public static void onSpawnEntity(FinalizeSpawnEvent event) {
        MobEnchantAttachment attachment = event.getEntity().getData(ModAttachments.MOB_ENCHANTS);

            LevelAccessor world = event.getLevel();
            if (!world.isClientSide() && world instanceof ServerLevel serverLevel) {
                LivingEntity livingEntity = event.getEntity();
                float difficultScale = serverLevel.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                float difficultScaleOnPercent = serverLevel.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty();


                if (EnchantConfig.COMMON.naturalSpawnEnchantedMob.get() && isSpawnEnchantableEntity(event.getEntity())) {

                    if (!(livingEntity instanceof Animal) && !(livingEntity instanceof WaterAnimal) || EnchantConfig.COMMON.spawnEnchantedAnimal.get()) {
                        if (event.getSpawnType() != EntitySpawnReason.BREEDING && event.getSpawnType() != EntitySpawnReason.CONVERSION && event.getSpawnType() != EntitySpawnReason.STRUCTURE && event.getSpawnType() != EntitySpawnReason.MOB_SUMMONED) {
                            boolean flag = event.getSpawner() != null && isOminousTrialSpawner(event.getSpawner());
                            if (flag || world.getRandom().nextFloat() < (EnchantConfig.COMMON.difficultyBasePercent.get() * world.getDifficulty().getId()) + difficultScaleOnPercent * EnchantConfig.COMMON.effectiveBasePercent.get()) {
                                if (!world.isClientSide()) {
                                    int i = 0;
                                    float scale = flag ? 0.5F : 1F;
                                    difficultScale = flag ? 1.0F : difficultScale;
                                    switch (world.getDifficulty()) {
                                        case EASY:
                                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale * scale, 1, 20);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, attachment, world.getRandom(), i);
                                            break;
                                        case NORMAL:
                                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale * scale, 1, 40);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, attachment, world.getRandom(), i);
                                            break;
                                        case HARD:
                                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale * scale, 1, 50);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, attachment, world.getRandom(), i);
                                            break;
                                    }

                                    livingEntity.setHealth(livingEntity.getMaxHealth());
                                }
                            }
                        }

                        if (event.getSpawnType() == EntitySpawnReason.TRIAL_SPAWNER) {
                            if (world.getRandom().nextFloat() < 0.1F + difficultScaleOnPercent * EnchantConfig.COMMON.effectiveBasePercent.get()) {
                                MobEnchantUtils.addEnchantmentToEntity(livingEntity, attachment, new MobEnchantmentData(world.registryAccess().lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).get(MobEnchants.WIND.getKey()).get(), 1));
                            }
                        }

                    }
                }
        }
    }

    private static boolean isOminousTrialSpawner(Either<BlockEntity, Entity> spawner) {
        return spawner.left().isPresent() && spawner.left().get() instanceof TrialSpawnerBlockEntity trialSpawnerBlockEntity && trialSpawnerBlockEntity.getTrialSpawner().isOminous();
    }

    private static boolean isSpawnAlwayEnchantableEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && EnchantConfig.COMMON.ALWAY_ENCHANTABLE_MOBS.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    private static boolean isSpawnAlwayEnchantableAncientEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && EnchantConfig.COMMON.ALWAY_ENCHANTABLE_ANCIENT_MOBS.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    private static boolean isSpawnEnchantableEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && !EnchantConfig.COMMON.ENCHANT_ON_SPAWN_EXCLUSION_MOBS.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    @SubscribeEvent
    public static void onUpdateEnchanted(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);

        if (entity instanceof LivingEntity livingEntity) {
            for (MobEnchantContent enchantHandler : attachment.getMobEnchants()) {
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    enchantHandler.getMobEnchant().value().tick(livingEntity, enchantHandler.getEnchantLevel());
                }
            }
            if (attachment.hasEnchant()) {
                if (entity.level().isClientSide() && !EnchantConfig.CLIENT.disableAuraRender.get()) {
                    if (!(entity instanceof Player player) || !player.isSpectator()) {
                        if (attachment.getMobEnchantType().value().particle().isPresent() && entity.getRandom().nextFloat() < 0.25F) {
                            entity.level().addParticle(attachment.getMobEnchantType().value().particle().get(), entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
                        }

                    }
                } else {
                    if (attachment.hasEnchant()) {

                        if (attachment.getEnchantOwner().isPresent()) {
                            LivingEntity owner = attachment.getEnchantOwner().get().getEntity(entity.level(), LivingEntity.class);
                            if (owner == null || !owner.isAlive() || entity.distanceToSqr(owner) > 512) {
                                attachment.removeMobEnchantFromOwner(livingEntity);
                                entity.playSound(SoundEvents.ITEM_BREAK.value(), 1.5F, 1.6F);
                            }
                        }
                    }
                }

            }
        }
    }


    @SubscribeEvent
    public static void onEntityHurtPre(LivingDamageEvent.Pre event) {
        LivingEntity livingEntity = event.getEntity();
        MobEnchantAttachment attachment = livingEntity.getData(ModAttachments.MOB_ENCHANTS);

        int i = MobEnchantUtils.getMobEnchantLevelFromHandler(attachment.getMobEnchants(), MobEnchants.THORN.getKey());

        if (event.getSource().getDirectEntity() instanceof LivingEntity && !event.getSource().is(DamageTypeTags.IS_PROJECTILE) && !event.getSource().is(DamageTypes.THORNS) && livingEntity.getRandom().nextFloat() < i * 0.1F) {
            LivingEntity attacker = (LivingEntity) event.getSource().getDirectEntity();

            attacker.hurt(livingEntity.damageSources().thorns(livingEntity), event.getNewDamage() * (i / 8F));
        }

    }

    @SubscribeEvent
    public static void onEntityHurtPost(LivingDamageEvent.Post event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            if (attacker.level() instanceof ServerLevel serverLevel) {
                MobEnchantAttachment attachmentAttacker = attacker.getData(ModAttachments.MOB_ENCHANTS);

                if (attachmentAttacker.hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(attachmentAttacker.getMobEnchants(), MobEnchants.POISON.getKey())) {
                    int i = MobEnchantUtils.getMobEnchantLevelFromHandler(attachmentAttacker.getMobEnchants(), MobEnchants.POISON.getKey());

                    if (attacker.getRandom().nextFloat() < i * 0.125F) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), attacker);
                    }
                }

            }
        }
    }


    @SubscribeEvent
    public static void onEntityIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            if (attacker.level() instanceof ServerLevel serverLevel) {
                MobEnchantAttachment attachmentAttacker = attacker.getData(ModAttachments.MOB_ENCHANTS);

                if (attachmentAttacker.hasEnchant()) {
                        //make snowman stronger
                        if (event.getAmount() == 0 && event.getContainer().getBlockedDamage() <= 0) {
                            event.setAmount(MobEnchantUtils.modifyDamage(serverLevel, attacker, event.getSource(), event.getAmount()));

                        } else if (event.getAmount() > 0) {
                            event.setAmount(MobEnchantUtils.modifyDamage(serverLevel, attacker, event.getSource(), event.getAmount()));
                        }
                    }

            }
        }
        MobEnchantAttachment attachmentHurt = livingEntity.getData(ModAttachments.MOB_ENCHANTS);


        if (!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && attachmentHurt.hasEnchant()) {
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    float f = CombatRules.getDamageAfterMagicAbsorb(event.getAmount(), MobEnchantUtils.getDamageProtection(serverLevel, livingEntity, event.getSource()));
                    event.setAmount(f);
                }
            }

    }


    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        Entity entityTarget = event.getTarget();
        Player player = event.getEntity();

        if (!(entityTarget instanceof Player)) {
            if (stack.getItem() == ModItems.MOB_ENCHANT_BOOK.get() && !player.getCooldowns().isOnCooldown(stack)) {
                if (entityTarget instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) entityTarget;
                    if (MobEnchantUtils.hasMobEnchant(stack)) {
                        MobEnchantAttachment attachmentTarget = target.getData(ModAttachments.MOB_ENCHANTS);

                        boolean flag = MobEnchantUtils.addItemMobEnchantToEntity(stack, target, player, attachmentTarget);

                            if (flag) {
                                player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                                stack.hurtAndBreak(1, player, event.getHand());

                                player.getCooldowns().addCooldown(stack, 60);

                                event.setCancellationResult(InteractionResult.SUCCESS);
                                event.setCanceled(true);
                            } else {
                                player.sendOverlayMessage(Component.translatable("enchantwithmob.cannot.enchant"));
                                player.getCooldowns().addCooldown(stack, 20);
                                event.setCancellationResult(InteractionResult.FAIL);
                                event.setCanceled(true);
                            }
                    }
                }
            }

            if (stack.getItem() == ModItems.ENCHANATERS_BOTTLE.get() && !player.getCooldowns().isOnCooldown(stack)) {
                if (entityTarget instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) entityTarget;
                    ItemStack stack1 = new ItemStack(ModItems.ENCHANATERS_EXPERIENCE_BOTTLE.get());
                    MobEnchantAttachment attachmentTarget = target.getData(ModAttachments.MOB_ENCHANTS);

                    if (attachmentTarget.hasEnchant() && !attachmentTarget.isPreventRemoveSelf()) {
                        int xp = MobEnchantUtils.getExperienceFromMob(attachmentTarget);

                            if (xp > 0) {
                                stack1.set(ModDataCompnents.EXPERIENCE.get(), xp);
                            }
                        MobEnchantUtils.removeMobEnchantToEntity(target, attachmentTarget);
                            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                            stack.consume(1, player);
                            player.getCooldowns().addCooldown(stack, 80);

                            event.setCancellationResult(InteractionResult.SUCCESS);
                            event.setCanceled(true);

                            if (!player.hasInfiniteMaterials()) {
                                if (!player.getInventory().add(stack1)) {
                                    player.drop(stack1, false);
                                }
                            }
                        }
                    }
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack itemstack = event.getLeft();
        int i = 0;
        long j = 0L;
        int k = 0;
        if (!itemstack.isEmpty() && MobEnchantUtils.canStoreEnchantments(itemstack)) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = event.getRight();
            ItemMobEnchantments.Mutable itemenchantments$mutable = new ItemMobEnchantments.Mutable(MobEnchantUtils.getEnchantmentsForCrafting(itemstack1));
            j += (long) itemstack.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0)).intValue()
                    + (long) itemstack2.getOrDefault(DataComponents.REPAIR_COST, Integer.valueOf(0)).intValue();
            event.setXpCost(0);
            boolean flag = false;
            if (!itemstack2.isEmpty()) {
                flag = itemstack2.has(ModDataCompnents.MOB_ENCHANTMENTS.get());
                if (itemstack1.isDamageableItem() && itemstack.isValidRepairItem(itemstack2)) {
                    int l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        event.setOutput(ItemStack.EMPTY);
                        event.setXpCost(0);
                        return;
                    }

                    int j3;
                    for (j3 = 0; l2 > 0 && j3 < itemstack2.getCount(); j3++) {
                        int k3 = itemstack1.getDamageValue() - l2;
                        itemstack1.setDamageValue(k3);
                        i++;
                        l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    }
                    event.setXpCost(j3);
                } else {
                    if (!flag && (!itemstack1.is(itemstack2.getItem()) || !itemstack1.isDamageableItem())) {
                        event.setOutput(ItemStack.EMPTY);
                        event.setXpCost(0);
                        return;
                    }

                    if (itemstack1.isDamageableItem()) {
                        int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamageValue()) {
                            itemstack1.setDamageValue(l1);
                            i += 2;
                        }
                    }

                    ItemMobEnchantments itemenchantments = MobEnchantUtils.getEnchantmentsForCrafting(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for (Object2IntMap.Entry<Holder<MobEnchant>> entry : itemenchantments.entrySet()) {
                        Holder<MobEnchant> holder = entry.getKey();
                        int i2 = itemenchantments$mutable.getLevel(holder.value());
                        int j2 = entry.getIntValue();
                        j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                        MobEnchant enchantment = holder.value();
                        // Neo: Respect IItemExtension#supportsMobEnchant - we also delegate the logic for Enchanted Books to this method.
                        // Though we still allow creative players to combine any item with any enchantment in the anvil here.
                        boolean flag1 = true;
                        if (event.getPlayer().getAbilities().instabuild) {
                            flag1 = true;
                        }

                        for (Holder<MobEnchant> holder1 : itemenchantments$mutable.keySet()) {
                            if (!holder1.equals(holder) && !MobEnchant.areCompatible(holder, holder1)) {
                                flag1 = false;
                                i++;
                            }
                        }

                        if (!flag1) {
                            flag3 = true;
                        } else {
                            flag2 = true;
                            if (j2 > enchantment.getMaxLevel()) {
                                j2 = enchantment.getMaxLevel();
                            }

                            itemenchantments$mutable.set(holder.value(), j2);
                            int l3 = enchantment.getAnvilCost();
                            if (flag) {
                                l3 = Math.max(1, l3 / 2);
                            }

                            i += l3 * j2;
                            if (itemstack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }

                    if (flag3 && !flag2) {
                        event.setOutput(ItemStack.EMPTY);
                        event.setXpCost(0);
                        return;
                    }
                }
            }

            if (event.getName() != null && !StringUtil.isBlank(event.getName())) {
                if (!event.getName().equals(itemstack.getHoverName().getString())) {
                    k = 1;
                    i += k;
                    itemstack1.set(DataComponents.CUSTOM_NAME, Component.literal(event.getName()));
                }
            } else if (itemstack.has(DataComponents.CUSTOM_NAME)) {
                k = 1;
                i += k;
                itemstack1.remove(DataComponents.CUSTOM_NAME);
            }
            int k2 = i <= 0 ? 0 : (int) Mth.clamp(j + (long) i, 0L, 2147483647L);
            event.setXpCost(k2);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0) {
                if (event.getXpCost() >= 40) {
                    event.setXpCost(39);
                }

            }

            if (event.getXpCost() >= 40 && !event.getPlayer().getAbilities().instabuild) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int i3 = itemstack1.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (i3 < itemstack2.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                    i3 = itemstack2.getOrDefault(DataComponents.REPAIR_COST, 0);
                }

                if (k != i || k == 0) {
                    i3 = calculateIncreasedRepairCost(i3);
                }

                itemstack1.set(DataComponents.REPAIR_COST, i3);
                MobEnchantUtils.setEnchantments(itemstack1, itemenchantments$mutable.toImmutable());
            }
            event.setOutput(itemstack1);
            //event.getPlayer().containerMenu.broadcastChanges();
        }
    }

    @SubscribeEvent
    public static void onExpDropped(LivingExperienceDropEvent event) {
        LivingEntity entity = event.getEntity();
        MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);

        if (attachment.hasEnchant()) {
            event.setDroppedExperience((int) (event.getDroppedExperience() + (MobEnchantUtils.getExperienceFromMob(attachment) * attachment.getMobEnchantType().value().expGainScale())));
        }
    }


    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            MobEnchantAttachment attachment = player.getData(ModAttachments.MOB_ENCHANTS);

            serverPlayer.syncData(ModAttachments.MOB_ENCHANTS);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        MobEnchantAttachment attachment = player.getData(ModAttachments.MOB_ENCHANTS);
        if (!player.level().isClientSide()) {
            player.syncData(ModAttachments.MOB_ENCHANTS);
        }
    }

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);

        if (entity instanceof LivingEntity livingEntity) {
            if (!entity.level().isClientSide()) {
                entity.syncData(ModAttachments.MOB_ENCHANTS);
            }
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (!event.isWasDeath()) {
            oldPlayer.getData(ModAttachments.MOB_ENCHANTS.get()).getMobEnchants().forEach(mobEnchantHandler -> {
                newPlayer.getData(ModAttachments.MOB_ENCHANTS.get()).addMobEnchant(newPlayer, mobEnchantHandler.getMobEnchant(), mobEnchantHandler.getEnchantLevel());
            });
        }
    }
}
