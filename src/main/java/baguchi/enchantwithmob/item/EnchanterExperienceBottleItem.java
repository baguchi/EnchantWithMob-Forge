package baguchi.enchantwithmob.item;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.registry.ModItems;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class EnchanterExperienceBottleItem extends Item {
    public EnchanterExperienceBottleItem(Properties group) {
        super(group);
    }

    @Override
    public boolean isEnabled(FeatureFlagSet p_249172_) {
        return super.isEnabled(p_249172_) && !EnchantConfig.COMMON.disableMobEnchantStuffItems.get();
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        if (ticksRemaining % 4 == 0) {
            float drinkPitch = Mth.randomBetween(livingEntity.getRandom(), 0.9F, 1.0F);
            level.playSound(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.4F, drinkPitch);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack stack1 = new ItemStack(ModItems.ENCHANATERS_BOTTLE.get());
        if (entity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.USING_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }
        int xp = stack.getOrDefault(ModDataCompnents.EXPERIENCE.get(), 0);

        if (xp > 0) {
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.getCooldowns().addCooldown(stack1, 40);
                serverPlayer.giveExperiencePoints(xp);
            }
        }

        stack.consume(1, entity);

        if (stack.isEmpty()) {
            return stack1;
        } else {
            if (entity instanceof Player player && !player.hasInfiniteMaterials()) {
                if (!player.getInventory().add(stack1)) {
                    player.drop(stack1, false);
                }
            }

            return stack;
        }
    }

    @Override
    public InteractionResult use(Level p_41352_, Player p_41353_, InteractionHand p_41354_) {
        return ItemUtils.startUsingInstantly(p_41352_, p_41353_, p_41354_);
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack p_40680_, LivingEntity p_345962_) {
        return 30;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack p_41452_) {
        return ItemUseAnimation.DRINK;
    }
}
