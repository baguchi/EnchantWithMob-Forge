package baguchi.enchantwithmob.utils;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.core.Holder;

public class MobEnchantConfigUtils {

    public static boolean isPlayerEnchantable(Holder<MobEnchant> mobEnchant) {
        return !EnchantConfig.COMMON.BLACKLIST_PLAYER_ENCHANT.get().contains(mobEnchant.getRegisteredName());
    }
}
