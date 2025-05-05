package baguchan.enchantwithmob.mobenchant;

import baguchan.enchantwithmob.EnchantConfig;
import baguchan.enchantwithmob.EnchantWithMob;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class MultiShotMobEnchant extends MobEnchant {
	private static boolean isAdding = false;

	public MultiShotMobEnchant(Properties properties) {
		super(properties);
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		if (!event.loadedFromDisk()) {
			Entity entity = event.getEntity();
			Level level = event.getLevel();
			if (entity instanceof Projectile) {
				Projectile projectile = (Projectile) entity;
				if (!shooterIsLiving(projectile) || !EnchantConfig.COMMON.ALLOW_MULTISHOT_PROJECTILE.get().contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString()))
					return;
				LivingEntity owner = (LivingEntity) projectile.getOwner();
				MobEnchantUtils.executeIfPresent(owner, MobEnchants.MULTISHOT.get(), () -> {
					if (!level.isClientSide && projectile.tickCount == 0 && !isAdding) {
						isAdding = true;
						CompoundTag compoundNBT = new CompoundTag();
						compoundNBT = projectile.saveWithoutId(compoundNBT);
						addProjectile(projectile, compoundNBT, level, 15.0F);
						addProjectile(projectile, compoundNBT, level, -15.0F);
						isAdding = false;
					}
				});

			}
		}
	}

	private static void addProjectile(Projectile projectile, CompoundTag compoundNBT, Level level, float rotation) {
		Projectile newProjectile = (Projectile) projectile.getType().create(level);
		UUID uuid = newProjectile.getUUID();
		newProjectile.load(compoundNBT);
		newProjectile.setUUID(uuid);
		Vec3 vector3d = newProjectile.getDeltaMovement().yRot((float) (Math.PI / rotation));

		newProjectile.setDeltaMovement(vector3d);
		float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
		newProjectile.setYRot((float) (Mth.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI)));
		newProjectile.setXRot((float) (Mth.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI)));
		newProjectile.yRotO = newProjectile.getYRot();
		newProjectile.xRotO = newProjectile.getXRot();
		if (newProjectile instanceof Projectile) {
			Projectile newDamagingProjectile = (Projectile) newProjectile;
			Vec3 newPower = new Vec3(newDamagingProjectile.getDeltaMovement().x, newDamagingProjectile.getDeltaMovement().y, newDamagingProjectile.getDeltaMovement().z).yRot((float) (Math.PI / rotation));

			newDamagingProjectile.setDeltaMovement(newPower);
		}

		if (newProjectile instanceof AbstractHurtingProjectile abstractHurtingProjectile) {
			Projectile newDamagingProjectile = (Projectile) newProjectile;
			Vec3 newPower = new Vec3(abstractHurtingProjectile.xPower, abstractHurtingProjectile.yPower, abstractHurtingProjectile.zPower).yRot((float) (Math.PI / rotation));

			abstractHurtingProjectile.xPower = newPower.x;
			abstractHurtingProjectile.yPower = newPower.y;
			abstractHurtingProjectile.zPower = newPower.z;
		}

		if (newProjectile instanceof AbstractArrow) {
			((AbstractArrow) newProjectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
		}

		newProjectile.getCapability(EnchantWithMob.ITEM_MOB_ENCHANT_CAP).ifPresent(cap ->
		{
			cap.setHasEnchant(true);
		});

		level.addFreshEntity(newProjectile);
	}

	public static boolean shooterIsLiving(Projectile projectile) {
		return projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity;
	}

	public int getMinEnchantability(int enchantmentLevel) {
		return 10;
	}

	public int getMaxEnchantability(int enchantmentLevel) {
		return this.getMinEnchantability(enchantmentLevel) + 40;
	}

	@Override
	public boolean isCompatibleMob(LivingEntity livingEntity) {
		return EnchantConfig.COMMON.WHITELIST_SHOOT_ENTITY.get().contains(ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType()).toString()) || livingEntity instanceof Player;
	}
}
