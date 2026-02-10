package baguchi.enchantwithmob.command;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class MobEnchantingCommand {

	private static final DynamicCommandExceptionType ERROR_INVALID_FEATURE = new DynamicCommandExceptionType(
			p_212392_ -> Component.translatable("commands.place.feature.invalid", p_212392_)
	);


	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		LiteralArgumentBuilder<CommandSourceStack> enchantCommand = Commands.literal("mob_enchanting")
				.requires(player -> player.hasPermission(2));
		enchantCommand.then(Commands.literal("clear").then(Commands.argument("target", EntityArgument.entity()).executes((ctx) -> {
			return setClear(ctx.getSource(), EntityArgument.getEntity(ctx, "target"));
		}))).then(Commands.literal("give").then(Commands.argument("target", EntityArgument.entity())
				.then(Commands.argument("mob_enchantment", ResourceKeyArgument.key(MobEnchants.MOB_ENCHANT_REGISTRY)).executes((p_198357_0_) -> setMobEnchants(p_198357_0_.getSource(), EntityArgument.getEntity(p_198357_0_, "target"), getMobEnchant(p_198357_0_, "mob_enchantment"), 1))
						.then(Commands.argument("level", IntegerArgumentType.integer(1)).executes((p_198357_0_) -> setMobEnchants(p_198357_0_.getSource(), EntityArgument.getEntity(p_198357_0_, "target"), getMobEnchant(p_198357_0_, "mob_enchantment"), IntegerArgumentType.getInteger(p_198357_0_, "level")))))));

		dispatcher.register(enchantCommand);

		LiteralArgumentBuilder<CommandSourceStack> mobEnchantType = Commands.literal("mob_enchant_type")
				.requires(player -> player.hasPermission(2));


		mobEnchantType.then(Commands.argument("target", EntityArgument.entity()).then(Commands.argument("mob_enchant_type", ResourceKeyArgument.key(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY)).executes((ctx) -> {
			return setMobEnchantType(ctx.getSource(), EntityArgument.getEntity(ctx, "target"), getMobEnchantType(ctx, "mob_enchant_type"));
		})));

		dispatcher.register(mobEnchantType);
	}

	public static Holder.Reference<MobEnchant> getMobEnchant(CommandContext<CommandSourceStack> p_249310_, String p_250729_) throws CommandSyntaxException {
		return resolveKey(p_249310_, p_250729_, MobEnchants.MOB_ENCHANT_REGISTRY, ERROR_INVALID_FEATURE);
	}

	public static Holder.Reference<MobEnchantType> getMobEnchantType(CommandContext<CommandSourceStack> p_249310_, String p_250729_) throws CommandSyntaxException {
		return resolveKey(p_249310_, p_250729_, MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY, ERROR_INVALID_FEATURE);
	}

	private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> p_212379_, ResourceKey<? extends Registry<T>> p_212380_) {
		return p_212379_.getSource().getServer().registryAccess().registryOrThrow(p_212380_);
	}

	private static <T> ResourceKey<T> getRegistryKey(
			CommandContext<CommandSourceStack> p_212374_, String p_212375_, ResourceKey<Registry<T>> p_212376_, DynamicCommandExceptionType p_212377_
	) throws CommandSyntaxException {
		ResourceKey<?> resourcekey = p_212374_.getArgument(p_212375_, ResourceKey.class);
		Optional<ResourceKey<T>> optional = resourcekey.cast(p_212376_);
		return optional.orElseThrow(() -> p_212377_.create(resourcekey));
	}

	private static <T> Holder.Reference<T> resolveKey(
			CommandContext<CommandSourceStack> p_248662_, String p_252172_, ResourceKey<Registry<T>> p_249701_, DynamicCommandExceptionType p_249790_
	) throws CommandSyntaxException {
		ResourceKey<T> resourcekey = getRegistryKey(p_248662_, p_252172_, p_249701_, p_249790_);
		return getRegistry(p_248662_, p_249701_).getHolder(resourcekey).orElseThrow(() -> p_249790_.create(resourcekey.location()));
	}

	private static int setClear(CommandSourceStack commandStack, Entity entity) {

		if (entity != null) {
			if (entity instanceof LivingEntity) {
				if (entity instanceof IEnchantCap enchantCap) {
					enchantCap.getEnchantCap().removeAllMobEnchant((LivingEntity) entity);
					enchantCap.getEnchantCap().setEnchantType((LivingEntity) entity, MobEnchantTypes.NORMAL);
				}

				commandStack.sendSuccess(() -> Component.translatable("commands.enchantwithmob.mob_enchanting.clear", entity.getDisplayName()), true);
				return 1;
			} else {
				commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.clear.fail.no_living_entity", entity.getDisplayName()));

				return 0;
			}
		} else {
			commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.clear.fail.no_entity"));

			return 0;
		}
	}

	private static int setMobEnchantType(CommandSourceStack commandStack, Entity entity, Holder.Reference<MobEnchantType> holder) {
		if (entity != null) {
			if (entity instanceof LivingEntity) {

				if (entity instanceof IEnchantCap enchantCap) {
					enchantCap.getEnchantCap().setEnchantType((LivingEntity) entity, holder.getKey());
				}

				commandStack.sendSuccess(() -> Component.translatable("commands.enchantwithmob.mob_enchant_type.set", entity.getDisplayName()), true);
				return 1;
			} else {
				commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchant_type.fail.no_living_entity", entity.getDisplayName()));

				return 0;
			}
		} else {
			commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchant_type.fail.no_entity"));

			return 0;
		}
	}

	private static int setMobEnchants(CommandSourceStack commandStack, Entity entity, Holder.Reference<MobEnchant> holder, int level) {
		Holder.Reference<MobEnchant> mobEnchant = holder;


		if (entity != null) {
			if (entity instanceof LivingEntity) {
				if (mobEnchant != null) {
					if (level > 255) {
						commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.too_high"));
						return 0;
					} else {
						if (entity instanceof IEnchantCap enchantCap) {
							enchantCap.getEnchantCap().addMobEnchant((LivingEntity) entity, mobEnchant, level);
						}

						commandStack.sendSuccess(() -> Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant", entity.getDisplayName(), commandStack.registryAccess().registryOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).getKey(mobEnchant.value()).toString()), true);
						return 1;
					}
				} else {
					commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_mobenchant"));

					return 0;
				}
			} else {
				commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_living_entity", entity.getDisplayName()));

				return 0;
			}
		} else {
			commandStack.sendFailure(Component.translatable("commands.enchantwithmob.mob_enchanting.set_enchant.fail.no_entity"));

			return 0;
		}
	}
}