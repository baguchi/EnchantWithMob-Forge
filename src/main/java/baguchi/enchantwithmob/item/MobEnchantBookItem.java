package baguchi.enchantwithmob.item;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.ModDataCompnents;
import baguchi.enchantwithmob.registry.ModItems;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import baguchi.enchantwithmob.utils.MobEnchantmentData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class MobEnchantBookItem extends Item {
	public MobEnchantBookItem(Properties group) {
		super(group);
	}


    /*
     * Implemented onRightClick (method) inside CommonEventHandler instead of this method
     */
    /*@Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (MobEnchantUtils.hasMobEnchant(stack)) {
            target.getCapability(EnchantWithMob.MOB_ENCHANT_CAP).ifPresent(cap ->
            {
                MobEnchantUtils.addMobEnchantToEntityFromItem(stack, target, cap);
            });
            playerIn.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

            stack.damageItem(1, playerIn, (entity) -> entity.sendBreakAnimation(hand));

            return ActionResultType.SUCCESS;
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }*/

	@Override
	public boolean isEnabled(FeatureFlagSet p_249172_) {
		return super.isEnabled(p_249172_) && !EnchantConfig.COMMON.disableMobEnchantStuffItems.get();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (EnchantConfig.COMMON.enchantYourSelf.get() && MobEnchantUtils.hasMobEnchant(stack)) {
				if (playerIn instanceof IEnchantCap cap) {
					boolean flag = MobEnchantUtils.addItemMobEnchantToEntity(stack, playerIn, playerIn, cap);


					//When flag is true, enchanting is success.
					if (flag) {
						level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS);
						playerIn.getCooldowns().addCooldown(stack.getItem(), 40);
						stack.hurtAndBreak(1, playerIn, LivingEntity.getSlotForHand(handIn));

						return InteractionResultHolder.success(stack);
					} else {
						playerIn.displayClientMessage(Component.translatable("enchantwithmob.cannot.enchant_yourself"), true);

						playerIn.getCooldowns().addCooldown(stack.getItem(), 20);

						return InteractionResultHolder.fail(stack);
					}
				}
		}
		return super.use(level, playerIn, handIn);
	}

	public static ItemStack createMobEnchantBook(MobEnchantmentData p_363915_) {
		ItemStack itemstack = new ItemStack(ModItems.MOB_ENCHANT_BOOK.get());
		MobEnchantUtils.enchant(p_363915_.enchantment, itemstack, p_363915_.enchantmentLevel);
		return itemstack;
	}

	public static ItemStack createEnchanterBook(MobEnchantmentData p_363915_) {
		ItemStack itemstack = new ItemStack(ModItems.ENCHANTERS_BOOK.get());
		MobEnchantUtils.enchant(p_363915_.enchantment, itemstack, p_363915_.enchantmentLevel);
		return itemstack;
	}

	public static void generateEnchantmentBookTypesOnlyMaxLevel(
			BuildCreativeModeTabContentsEvent output, HolderLookup<MobEnchant> enchantments, CreativeModeTab.TabVisibility tabVisibility
	) {
		enchantments.listElements()
				.map(p_360016_ -> createMobEnchantBook(new MobEnchantmentData(p_360016_, p_360016_.value().getMaxLevel())))
				.forEach(p_269989_ -> output.accept(p_269989_, tabVisibility));
		enchantments.listElements()
				.map(p_360016_ -> createEnchanterBook(new MobEnchantmentData(p_360016_, p_360016_.value().getMaxLevel())))
				.forEach(p_269989_ -> output.accept(p_269989_, tabVisibility));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext level, List<Component> tooltip, TooltipFlag p_41424_) {
		super.appendHoverText(stack, level, tooltip, p_41424_);
		ChatFormatting[] textformatting2 = new ChatFormatting[]{ChatFormatting.DARK_PURPLE};
        Consumer<Component> consumer = tooltip::add;
        stack.addToTooltip(ModDataCompnents.MOB_ENCHANTMENTS.get(), level, consumer, p_41424_);
		tooltip.add(Component.translatable("mobenchant.enchantwithmob.mob_enchant_book.tooltip").withStyle(textformatting2));
	}

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
