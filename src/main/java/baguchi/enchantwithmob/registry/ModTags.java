package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ModTags {
    public static class MobEnchantTags {
        public static final TagKey<MobEnchant> CURSE = create("curse");

        public static final TagKey<MobEnchant> TOOLTIP_ORDER = create("tooltip_order");
        public static final TagKey<MobEnchant> RANDOM_LOOT = create("random_loot");
        public static final TagKey<MobEnchant> RANDOM_SPAWN = create("random_spawn");
        public static final TagKey<MobEnchant> ENCHANTER_ENCHANT = create("enchanter_enchant");

        public static final TagKey<MobEnchant> AFFECT_SELF_REFLECT = create("affect_self_relect");
        public static final TagKey<MobEnchant> AFFECT_SPEED = create("affect_speed");
        public static final TagKey<MobEnchant> POST_ATTACK = create("post_attack");


        private static TagKey<MobEnchant> create(String p_341202_) {
            return TagKey.create(MobEnchants.MOB_ENCHANT_REGISTRY, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, p_341202_));
        }
    }

    public static class MobEnchantTypeTags {
        public static final TagKey<MobEnchantType> PREVENT_REMOVE_SELF = create("prevent_remove_self");

        private static TagKey<MobEnchantType> create(String p_341202_) {
            return TagKey.create(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, p_341202_));
        }
    }
}
