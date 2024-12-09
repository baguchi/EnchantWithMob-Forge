package baguchi.enchantwithmob.item;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.registry.ModItems;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

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

		if (entity instanceof IEnchantCap cap) {
			int xp = 0;
			if (!cap.getEnchantCap().isAncient() && cap.getEnchantCap().hasEnchant()) {
				xp += MobEnchantUtils.getExperienceFromMob(cap);

				if (xp > 0) {
					stack1.set(ModDataCompnents.EXPERIENCE.get(), xp);
				}

				if (cap.getEnchantCap().hasOwner()) {
					cap.getEnchantCap().removeOwner(entity);
				}
				MobEnchantUtils.removeMobEnchantToEntity(entity, cap);

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
	public InteractionResultHolder<ItemStack> use(Level p_41352_, Player p_41353_, InteractionHand p_41354_) {
		if (p_41353_ instanceof IEnchantCap cap) {
			int xp = MobEnchantUtils.getExperienceFromMob(cap);

			if (xp > 0) {
				return ItemUtils.startUsingInstantly(p_41352_, p_41353_, p_41354_);
			}
		}

		if (p_41353_.totalExperience > 0) {
			return ItemUtils.startUsingInstantly(p_41352_, p_41353_, p_41354_);
		}
		return super.use(p_41352_, p_41353_, p_41354_);
	}



	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext level, List<Component> tooltip, TooltipFlag p_41424_) {
		super.appendHoverText(stack, level, tooltip, p_41424_);
		ChatFormatting[] textformatting2 = new ChatFormatting[]{ChatFormatting.DARK_PURPLE};

		tooltip.add(Component.translatable("mobenchant.enchantwithmob.enchanters_bottle.tooltip").withStyle(textformatting2));
	}

	@Override
	public int getUseDuration(ItemStack p_40680_, LivingEntity p_345962_) {
		return 30;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_41358_) {
		return UseAnim.DRINK;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}
}
