package baguchi.enchantwithmob.registry;

import net.minecraft.world.entity.raid.Raid;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class RaiderEnumParams {
    @SuppressWarnings("unused")
    public static final EnumProxy<Raid.RaiderType> ENCHANTER = new EnumProxy<>(
            Raid.RaiderType.class, ModEntities.ENCHANTER, new int[]{0, 0, 1, 0, 1, 1, 2, 1}
    );
}
