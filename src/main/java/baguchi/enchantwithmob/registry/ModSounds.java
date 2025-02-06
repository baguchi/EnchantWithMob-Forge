package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, EnchantWithMob.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_IDLE = createEvent("entity.enchanter.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_HURT = createEvent("entity.enchanter.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_DEATH = createEvent("entity.enchanter.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_SPELL = createEvent("entity.enchanter.spell");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_ATTACK = createEvent("entity.enchanter.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_BEAM = createEvent("entity.enchanter.beam");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENCHANTER_BEAM_LOOP = createEvent("entity.enchanter.beam_loop");

    private static DeferredHolder<SoundEvent, SoundEvent> createEvent(String sound) {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, sound);
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(name));
    }

}