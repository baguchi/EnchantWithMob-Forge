package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.client.sound.EnchantBeamSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public class EnchantWithMobClientProxy {

    public static void playEnchantBeamSound(LivingEntity entity) {
        Minecraft.getInstance().getSoundManager().play(new EnchantBeamSoundInstance(entity));
    }
}
