package baguchi.enchantwithmob.mixin;

import baguchi.bagus_lib.api.IBaguPacket;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantCapability;
import baguchi.enchantwithmob.message.MobEnchantedMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, remap = false)
public abstract class LivingEntityMixin extends Entity implements IEnchantCap, IBaguPacket {

    public MobEnchantCapability capability = new MobEnchantCapability();
    public LivingEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(ValueOutput p_421587_, CallbackInfo ci) {
        p_421587_.store("MobEnchantData", CompoundTag.CODEC, this.getEnchantCap().serializeNBT(this.registryAccess()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(ValueInput p_422570_, CallbackInfo ci) {
        MobEnchantCapability mobEnchantCapability = new MobEnchantCapability();
        mobEnchantCapability.deserializeNBT(p_422570_.read("MobEnchantData", CompoundTag.CODEC).orElse(new CompoundTag()), this.registryAccess());
        this.setEnchantCap(mobEnchantCapability);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo callbackInfo) {
        if ((this.getEnchantCap().hasOwner() || this.getEnchantCap().isFromOwner()) && this.getEnchantCap().hasEnchant()) {
            if (this.getEnchantCap().getEnchantOwner() == null || !this.getEnchantCap().getEnchantOwner().isAlive()) {
                LivingEntity livingEntity = (LivingEntity) ((Object) this);
                this.getEnchantCap().removeMobEnchantFromOwner(livingEntity);
                this.playSound(SoundEvents.ITEM_BREAK.value(), 1.5F, 1.6F);
            } else if (this.distanceToSqr(this.getEnchantCap().getEnchantOwner()) > 512) {
                LivingEntity livingEntity = (LivingEntity) ((Object) this);
                this.getEnchantCap().removeMobEnchantFromOwner(livingEntity);
                this.playSound(SoundEvents.ITEM_BREAK.value(), 1.5F, 1.6F);
            }
        }
    }

    @Override
    public void resync(Entity entity) {
        if (!this.level().isClientSide()) {
            for (int i = 0; i < this.getEnchantCap().getMobEnchants().size(); i++) {
                MobEnchantedMessage message = new MobEnchantedMessage(this, this.getEnchantCap().getMobEnchants().get(i));
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(this, message);
            }
        }
    }


    @Override
    public MobEnchantCapability getEnchantCap() {
        return this.capability;
    }

    @Override
    public void setEnchantCap(MobEnchantCapability cap) {
        this.capability = cap;
    }

    @Shadow
    public boolean isBaby() {
        return false;
    }

}
