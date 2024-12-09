package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, EnchantWithMob.MODID);

    public static final Supplier<SoundEvent> ENCHANTER_IDLE = createEvent("entity.enchanter.idle");
    public static final Supplier<SoundEvent> ENCHANTER_HURT = createEvent("entity.enchanter.hurt");
    public static final Supplier<SoundEvent> ENCHANTER_DEATH = createEvent("entity.enchanter.death");
    public static final Supplier<SoundEvent> ENCHANTER_SPELL = createEvent("entity.enchanter.spell");
    public static final Supplier<SoundEvent> ENCHANTER_ATTACK = createEvent("entity.enchanter.attack");
    public static final Supplier<SoundEvent> ENCHANTER_BEAM = createEvent("entity.enchanter.beam");
    public static final Supplier<SoundEvent> ENCHANTER_BEAM_LOOP = createEvent("entity.enchanter.beam_loop");

    private static Supplier<SoundEvent> createEvent(String sound) {
        ResourceLocation name = ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, sound);
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(name));
    }

}