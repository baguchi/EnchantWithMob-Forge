package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantedTime;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Model.class)
public class ModelMixin implements IEnchantedTime {
    @Shadow
    @Final
    private static Vector3f ANIMATION_VECTOR_CACHE;
    @Unique
    private float enchantWithMob_Forge$differentTime = 1.0F;

    @Override
    public float enchantWithMob_Forge$getDifferentTime() {
        return enchantWithMob_Forge$differentTime;
    }

    @Override
    public void enchantWithMob_Forge$setDifferentTime(float time) {
        enchantWithMob_Forge$differentTime = time;
    }

    @Inject(method = "animate(Lnet/minecraft/world/entity/AnimationState;Lnet/minecraft/client/animation/AnimationDefinition;FF)V", at = @At(value = "HEAD"), cancellable = true)
    public void animate(AnimationState p_364413_, AnimationDefinition p_361459_, float p_361947_, float p_362164_, CallbackInfo ci) {
        Model model = (Model) ((Object) this);
        if (enchantWithMob_Forge$getDifferentTime() != 1.0F) {
            p_364413_.ifStarted(
                    p_361743_ -> KeyframeAnimations.animate(
                            model, p_361459_, (long) ((float) p_361743_.getTimeInMillis(p_361947_) * p_362164_ * enchantWithMob_Forge$getDifferentTime()), 1.0F, ANIMATION_VECTOR_CACHE
                    )
            );
            ci.cancel();
        }
    }
}