package baguchi.enchantwithmob.mixin.client;

import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.ClientEventHandler;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    public void shouldRender(T entity, Frustum frustum, double p_114493_, double p_114494_, double p_114495_, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            if (entity instanceof IEnchantCap enchantCap) {
                Optional<EntityReference<LivingEntity>> optional = enchantCap.getEnchantCap().getEnchantOwner();
                if (optional.isPresent()) {
                    LivingEntity livingEntity = optional.get().getEntity(entity.level(), LivingEntity.class);

                    if (livingEntity != null) {
                        Vec3 vec3 = ClientEventHandler.getPosition(livingEntity, (double) livingEntity.getBbHeight() * 0.5F, 1.0F);
                        Vec3 vec31 = ClientEventHandler.getPosition(entity, (double) entity.getBbHeight() * 0.5F, 1.0F);
                        cir.setReturnValue(frustum.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z)));
                    }
                }
            }
        }
    }
}
