package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

public class ModTags {
    public static class MobEnchantTags {
        public static final TagKey<MobEnchant> CURSE = create("curse");

        public static final TagKey<MobEnchant> TOOLTIP_ORDER = create("tooltip_order");
        public static final TagKey<MobEnchant> RANDOM_LOOT = create("random_loot");
        public static final TagKey<MobEnchant> RANDOM_SPAWN = create("random_spawn");
        public static final TagKey<MobEnchant> ENCHANTER_ENCHANT = create("enchanter_enchant");

        private static TagKey<MobEnchant> create(String p_341202_) {
            return TagKey.create(MobEnchants.MOB_ENCHANT_REGISTRY, Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, p_341202_));
        }
    }
}
