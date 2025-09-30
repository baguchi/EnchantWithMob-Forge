package baguchi.enchantwithmob.mixin;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class)
public abstract class EntityMixin {

    @Shadow
    private EntityDimensions dimensions;
    @Shadow
    private float eyeHeight;

    @Inject(method = "refreshDimensions", at = @At("RETURN"))
    public void refreshDimensions(CallbackInfo callbackInfo) {
        if (this instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (EnchantConfig.COMMON.changeSizeWhenEnchant.get()) {
                    float totalWidth = this.dimensions.width() * 1.025F;
                    float totalHeight = this.dimensions.height() * 1.025F;
                    this.eyeHeight = (this.eyeHeight * (1.025F));
                    dimensions = EntityDimensions.fixed(totalWidth, totalHeight);
                }
            }
        }
    }

}
