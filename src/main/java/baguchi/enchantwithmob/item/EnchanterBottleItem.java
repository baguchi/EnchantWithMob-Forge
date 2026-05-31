package baguchi.enchantwithmob.item;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.registry.ModItems;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class EnchanterBottleItem extends Item {
	public EnchanterBottleItem(Properties group) {
		super(group);
	}

	@Override
	public boolean isEnabled(FeatureFlagSet p_249172_) {
		return super.isEnabled(p_249172_) && !EnchantConfig.COMMON.disableMobEnchantStuffItems.get();
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		ItemStack stack1 = new ItemStack(ModItems.ENCHANATERS_EXPERIENCE_BOTTLE.get());

		MobEnchantAttachment attachment = entity.getData(ModAttachments.MOB_ENCHANTS);

			int xp = 0;
		if (!attachment.isPreventRemoveSelf() && attachment.hasEnchant()) {
			xp += MobEnchantUtils.getExperienceFromMob(attachment);

				if (xp > 0) {
					stack1.set(ModDataCompnents.EXPERIENCE.get(), xp);
				}

			if (attachment.hasOwner()) {
				attachment.removeOwner(entity);
				}
			MobEnchantUtils.removeMobEnchantToEntity(entity, attachment);

				entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

			}

			if (xp <= 0) {
				if (entity instanceof ServerPlayer serverplayer) {
					if (serverplayer.totalExperience >= 30) {

						stack1.set(ModDataCompnents.EXPERIENCE.get(), 30);
						serverplayer.giveExperiencePoints(-30);
					} else if (serverplayer.totalExperience > 0) {
						stack1.set(ModDataCompnents.EXPERIENCE.get(), serverplayer.totalExperience);
						serverplayer.giveExperiencePoints(-serverplayer.totalExperience);
					} else {
						return stack;
					}
				}
			}
		stack.consume(1, entity);

		if (entity instanceof ServerPlayer serverplayer) {
			CriteriaTriggers.USING_ITEM.trigger(serverplayer, stack);
			serverplayer.awardStat(Stats.ITEM_USED.get(this));
		}

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
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		MobEnchantAttachment attachment = player.getData(ModAttachments.MOB_ENCHANTS);

		int xp = MobEnchantUtils.getExperienceFromMob(attachment);
		if (xp > 0) {
			return ItemUtils.startUsingInstantly(level, player, hand);
		}


		if (player.totalExperience > 0) {
			return ItemUtils.startUsingInstantly(level, player, hand);
		}
		return super.use(level, player, hand);
	}

	@Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer1, TooltipFlag flag) {
        super.appendHoverText(stack, tooltipContext, tooltipDisplay, consumer1, flag);
		ChatFormatting[] textformatting2 = new ChatFormatting[]{ChatFormatting.DARK_PURPLE};
        consumer1.accept(Component.translatable("mobenchant.enchantwithmob.enchanters_bottle.tooltip").withStyle(textformatting2));
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
