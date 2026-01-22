package baguchi.enchantwithmob;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class EnchantConfig {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;
    static {
        Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ModConfigSpec> specPair2 = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue showEnchantedMobHud;
        public final ModConfigSpec.IntValue hudXPostion;
        public final ModConfigSpec.IntValue hudYPostion;

        public final ModConfigSpec.BooleanValue disablePoisonParticle;
        public final ModConfigSpec.BooleanValue disableAuraRender;
        public final ModConfigSpec.BooleanValue disableEyeRender;

        public final ModConfigSpec.BooleanValue oldStyleAnimation;

        public Client(ModConfigSpec.Builder builder) {
            showEnchantedMobHud = builder
                    .translation(EnchantWithMob.MODID + ".config.showEnchantedMobHud")
                    .define("Show Enchanted Mob Hud", true);
            hudXPostion = builder
                    .translation(EnchantWithMob.MODID + ".config.hudXPosition")
                    .defineInRange("Show Enchanted Mob Hud X Position", 20, 0, 1000);
            hudYPostion = builder
                    .translation(EnchantWithMob.MODID + ".config.hudYPosition")
                    .defineInRange("Show Enchanted Mob Hud Y Position", 50, 0, 1000);
            disablePoisonParticle = builder
                    .comment("Disable Poison Mob Enchant Particle. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.disablePoisonParticle")
                    .define("Disable Poison Particle", true);
            disableAuraRender = builder
                    .comment("Disable Aura Render. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.disableAuraRender")
                    .define("Disable Aura Render", false);
            disableEyeRender = builder
                    .comment("Disable Eye Render. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.disableEyeRender")
                    .define("Disable Eye Render", false);

            oldStyleAnimation = builder
                    .comment("Enable Old Style Animation. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.OldStyleAnimation")
                    .define("Old Style Animation", false);
        }
    }

    public static class Common {
        public final ModConfigSpec.ConfigValue<List<? extends String>> buffedDragon;
        public final ModConfigSpec.BooleanValue naturalSpawnEnchantedMob;
        public final ModConfigSpec.BooleanValue spawnEnchantedAnimal;
        public final ModConfigSpec.BooleanValue enchantYourSelf;
        public final ModConfigSpec.BooleanValue changeSizeWhenEnchant;
        public final ModConfigSpec.BooleanValue dungeonsLikeHealth;
        public final ModConfigSpec.BooleanValue bigYourSelf;
        public final ModConfigSpec.BooleanValue universalEnchant;
        public final ModConfigSpec.DoubleValue difficultyBasePercent;
        public final ModConfigSpec.DoubleValue effectiveBasePercent;
        public final ModConfigSpec.BooleanValue disableEnchanterArmor;
        public final ModConfigSpec.BooleanValue disableMobEnchantStuffItems;
        public final ModConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_MOBS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_ANCIENT_MOBS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> DISABLE_ENCHANTS;

        public final ModConfigSpec.ConfigValue<List<? extends String>> ALLOW_POISON_CLOUD_PROJECTILE;

        public final ModConfigSpec.ConfigValue<List<? extends String>> ALLOW_MULTISHOT_PROJECTILE;

        public final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_SHOOT_ENTITY;
        public final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_PLAYER_ENCHANT;

        public Common(ModConfigSpec.Builder builder) {
            naturalSpawnEnchantedMob = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.naturalSpawnEnchantedMob")
                    .define("Enchanted Mob can Spawn Natural", true);
            ENCHANT_ON_SPAWN_EXCLUSION_MOBS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: minecraft:ender_dragon.")
                    .translation(EnchantWithMob.MODID + ".config.enchantOnSpawnExclusionMobs")
                    .define("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            ALWAY_ENCHANTABLE_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments on spawn. Use the full name, eg: minecraft:zombie.")
                    .translation(EnchantWithMob.MODID + ".config.alwayEnchantableMobs")
                    .define("alwayEnchantableMobs", Lists.newArrayList());
            ALWAY_ENCHANTABLE_ANCIENT_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments as Ancient Mob on spawn(This feature may break for balance so be careful). Use the full name, eg: minecraft:zombie.")
                    .translation(EnchantWithMob.MODID + ".config.alwayEnchantableAncientMobs")
                    .define("alwayEnchantableAncientMobs", Lists.newArrayList());
            DISABLE_ENCHANTS = builder
                    .comment("Disables the specific mob enchant. Use the full name(This config only disabled mob enchant when mob spawn. not mean delete complete, eg: enchantwithmob:thorn.")
                    .translation(EnchantWithMob.MODID + ".config.disable_enchants")
                    .define("disableMobEnchants", Lists.newArrayList());
            ALLOW_POISON_CLOUD_PROJECTILE = builder
                    .comment("Allow the poison cloud for projectile. Use the full name(eg: minecraft:arrow.")
                    .translation(EnchantWithMob.MODID + ".config.allowPoisonCloudProjectiles")
                    .define("allowPoisonCloudProjectiles", Lists.newArrayList("minecraft:arrow", "minecraft:snowball", "earthmobsmod:melon_seeds", "earthmobsmod:zombie_flesh", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball", "tofucraft:fukumame", "tofucraft:nether_fukumame", "tofucraft:soul_fukumame"));
            ALLOW_MULTISHOT_PROJECTILE = builder
                    .comment("Allow the multi shot for projectile. Use the full name(eg: minecraft:arrow.")
                    .translation(EnchantWithMob.MODID + ".config.allowMultiShotProjectiles")
                    .define("allowMultiShotProjectiles", Lists.newArrayList("minecraft:arrow", "minecraft:snowball", "earthmobsmod:melon_seeds", "earthmobsmod:zombie_flesh", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball", "tofucraft:fukumame", "tofucraft:nether_fukumame", "tofucraft:soul_fukumame", "minecraft:wind_charge", "minecraft:breeze_wind_charge", "minecraft:dragon_fireball", "minecraft:fireball", "minecraft:small_fireball"));

            WHITELIST_SHOOT_ENTITY = builder
                    .comment("Whitelist the projectile mob enchant for mob. Use the full name(eg: minecraft:zombie.")
                    .translation(EnchantWithMob.MODID + ".config.whitelistShootEntity")
                    .define("whitelistShootEntity", Lists.newArrayList("minecraft:skeleton", "minecraft:pillager", "minecraft:shulker", "minecraft:llama", "conjurer_illager:conjurer", "earthmobsmod:bone_spider", "earthmobsmod:lobber_zombie", "earthmobsmod:lobber_drowned"
                            , "earthmobsmod:melon_golem", "minecraft:piglin", "minecraft:snow_golem", "minecraft:player", "minecraft:breeze", "minecraft:blaze"));
            BLACKLIST_PLAYER_ENCHANT = builder
                    .comment("Blacklist the mob enchant for player. Use the full name(eg: enchantwithmob:thorn.")
                    .translation(EnchantWithMob.MODID + ".config.blacklistPlayerEnchant")
                    .define("blacklistPlayerEnchant", Lists.newArrayList());

            buffedDragon = builder
                    .comment("Set the MobEnchant on the EnderDragon. If you want more harder fight. should set it! eg: enchantwithmob:thorn.")
                    .translation(EnchantWithMob.MODID + ".config.buffed_dragon")
                    .define("Buffed Dragon", Lists.newArrayList());
            spawnEnchantedAnimal = builder
                    .comment("Enable the the spawning of enchanted animal mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.spawnEnchantedAnimal")
                    .define("Enchanted Animal can Spawn Natural", false);
            enchantYourSelf = builder
                    .comment("Enable enchanting yourself. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.enchantYourSelf")
                    .define("Enchant yourself", true);
            changeSizeWhenEnchant = builder
                    .comment("Enable Change Size When Enchanted. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.changeSizeWhenEnchant")
                    .define("Change Size", false);
            dungeonsLikeHealth = builder
                    .comment("Enable Increase Health like Dungeons When Enchanted. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.dungeonsLikeHealth")
                    .define("Increase Health like Dungeons", false);
            bigYourSelf = builder
                    .comment("Enable Player More Bigger When You have Huge Enchant. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.bigYourSelf")
                    .define("Big Your Self", false);
            universalEnchant = builder
                    .comment("Enable All MobEnchant for all mob. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.universalEnchant")
                    .define("UniversalEnchant", false);
            difficultyBasePercent = builder
                    .comment("Set The Difficulty Base Enchanted Mob Spawn Percent. [(Difficulty Base Percent * Difficulty id) + (Effective Difficulty Percent * Effective Difficulty)]")
                    .translation(EnchantWithMob.MODID + ".config.DifficultyEnchantedSpawnPercent")
                    .defineInRange("Difficulty Enchanted Spawn Percent", 0.005D, 0.0D, 1D);
            effectiveBasePercent = builder
                    .comment("Set The Effective Difficulty Base Enchanted Mob Spawn Percent [(Difficulty Base Percent * Difficulty id) + (Effective Difficulty Percent * Effective Difficulty)]")
                    .translation(EnchantWithMob.MODID + ".config.EffectiveDifficultyEnchantedSpawnPercent")
                    .defineInRange("Effective Difficulty Enchanted Spawn Percent", 0.025D, 0.0D, 1D);
            disableEnchanterArmor = builder
                    .comment("Disable Enchanter Armor Item. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.DisableEnchanterArmor")
                    .define("Disable Enchanter Armor", false);
            disableMobEnchantStuffItems = builder
                    .comment("Disable MobEnchant Stuff Items. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.DisableMobEnchantStuffItems")
                    .define("Disable MobEnchant Stuff Items", false);

        }
    }

}
