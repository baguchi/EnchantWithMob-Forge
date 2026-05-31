package baguchi.enchantwithmob.client.sound;

import baguchi.enchantwithmob.registry.ModAttachments;
import baguchi.enchantwithmob.registry.ModSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class EnchantBeamSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_MAX = 0.7F;
    private static final float PITCH_MIN = 0.0F;
    private static final float PITCH_MAX = 1.0F;
    private static final float PITCH_DELTA = 0.0025F;
    private final LivingEntity livingEntity;

    public EnchantBeamSoundInstance(LivingEntity owner) {
        super(ModSoundEvents.ENCHANTER_BEAM_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.livingEntity = owner;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.6F;
        this.x = (float) owner.getX();
        this.y = (float) owner.getY();
        this.z = (float) owner.getZ();
    }

    public boolean canPlaySound() {
        return !this.livingEntity.isSilent();
    }

    public boolean canStartSilent() {
        return false;
    }

    public void tick() {
        if (this.livingEntity.isRemoved() || !this.livingEntity.getData(ModAttachments.MOB_ENCHANTS).hasOwner()) {
            this.stop();
        } else {
            this.x = (float) this.livingEntity.getX();
            this.y = (float) this.livingEntity.getY();
            this.z = (float) this.livingEntity.getZ();
            this.pitch = 1.0F;
            this.volume = 0.6F;
        }

    }
}
