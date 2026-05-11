package baguchi.enchantwithmob.item;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class EnchantersBookItem extends Item {
	public EnchantersBookItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isEnabled(FeatureFlagSet p_249172_) {
		return super.isEnabled(p_249172_) && !EnchantConfig.COMMON.disableMobEnchantStuffItems.get();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
			if (MobEnchantUtils.hasMobEnchant(stack)) {
				List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, playerIn.getBoundingBox().inflate(16.0D));
				List<LivingEntity> hasEnchantedMoblist = level.getEntitiesOfClass(LivingEntity.class, playerIn.getBoundingBox().inflate(16.0D), (entity) -> {
					return entity instanceof IEnchantCap enchantCap && enchantCap.getEnchantCap().hasEnchant();
				});

				if (hasEnchantedMoblist.isEmpty() || hasEnchantedMoblist.size() < 5) {
					if (!list.isEmpty()) {
						int size = list.size();
						final boolean[] flag = {false};
						for (int i = 0; i < size; ++i) {
							LivingEntity enchantedMob = list.get(i);

							if (i >= 5) {
								break;
							}

							if (!enchantedMob.canAttack(playerIn) && playerIn != enchantedMob) {
								if (enchantedMob instanceof IEnchantCap cap) {
									if (!cap.getEnchantCap().hasEnchant()) {
										if (flag[0]) {
											MobEnchantUtils.addUnstableItemMobEnchantToEntity(stack, enchantedMob, playerIn, cap);
										} else {
											flag[0] = MobEnchantUtils.addUnstableItemMobEnchantToEntity(stack, enchantedMob, playerIn, cap);
										}
									}
								}
								;
							}
						}

						//When flag is true, enchanting is success.
						if (flag[0]) {
							level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS);

							playerIn.getCooldowns().addCooldown(stack.getItem(), 40);
							stack.hurtAndBreak(1, playerIn, LivingEntity.getSlotForHand(handIn));

							return InteractionResultHolder.success(stack);
						}
					} else {
						playerIn.displayClientMessage(Component.translatable("enchantwithmob.cannot.no_enchantable_ally"), true);

						playerIn.getCooldowns().addCooldown(stack.getItem(), 20);

						return InteractionResultHolder.fail(stack);
					}
				} else {
					playerIn.displayClientMessage(Component.translatable("enchantwithmob.cannot.no_enchantable_ally"), true);

					playerIn.getCooldowns().addCooldown(stack.getItem(), 20);

					return InteractionResultHolder.fail(stack);
				}
			}
		return super.use(level, playerIn, handIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext level, List<Component> tooltip, TooltipFlag p_41424_) {
		super.appendHoverText(stack, level, tooltip, p_41424_);
		ChatFormatting[] textformatting2 = new ChatFormatting[]{ChatFormatting.DARK_PURPLE};

		Consumer<Component> consumer = tooltip::add;
		stack.addToTooltip(ModDataCompnents.MOB_ENCHANTMENTS.get(), level, consumer, p_41424_);

		tooltip.add(Component.translatable("mobenchant.enchantwithmob.enchanter_book.tooltip").withStyle(textformatting2));
	}

	@Override
	public boolean isFoil(ItemStack p_77636_1_) {
		return true;
	}
}
