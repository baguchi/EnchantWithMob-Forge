package baguchan.enchantwithmob;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue showEnchantedMobHud;
        public final ForgeConfigSpec.BooleanValue disablePoisonParticle;
        public final ForgeConfigSpec.BooleanValue disableAuraRender;
        public final ForgeConfigSpec.BooleanValue oldStyleAnimation;

        public Client(ForgeConfigSpec.Builder builder) {
            showEnchantedMobHud = builder
                    .translation(EnchantWithMob.MODID + ".config.showEnchantedMobHud")
                    .define("Show Enchanted Mob Hud", true);
            disablePoisonParticle = builder
                    .comment("Disable Poison Mob Enchant Particle. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.disablePoisonParticle")
                    .define("Disable Poison Particle", true);
            disableAuraRender = builder
                    .comment("Disable Aura Render. [true / false]")
                    .define("Disable Aura Render", false);
            oldStyleAnimation = builder
                    .comment("Enable Old Style Animation. [true / false]")
                    .define("OldStyleAnimation", false);
        }
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> buffedDragon;
        public final ForgeConfigSpec.BooleanValue naturalSpawnEnchantedMob;
        public final ForgeConfigSpec.BooleanValue spawnEnchantedAnimal;
        public final ForgeConfigSpec.BooleanValue enchantYourSelf;
        public final ForgeConfigSpec.BooleanValue changeSizeWhenEnchant;
        public final ForgeConfigSpec.BooleanValue dungeonsLikeHealth;
        public final ForgeConfigSpec.BooleanValue bigYourSelf;
        public final ForgeConfigSpec.BooleanValue universalEnchant;
        public final ForgeConfigSpec.DoubleValue difficultyBasePercent;
        public final ForgeConfigSpec.DoubleValue effectiveBasePercent;
        public final ForgeConfigSpec.BooleanValue disableEnchanterArmor;
        public final ForgeConfigSpec.BooleanValue disableMobEnchantStuffItems;


        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ENCHANT_ON_SPAWN_EXCLUSION_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAY_ENCHANTABLE_ANCIENT_MOBS;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLE_ENCHANTS;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOW_POISON_CLOUD_PROJECTILE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOW_MULTISHOT_PROJECTILE;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> WHITELIST_SHOOT_ENTITY;

        public Common(ForgeConfigSpec.Builder builder) {
            naturalSpawnEnchantedMob = builder
                    .comment("Enable the the spawning of enchanted mobs. [true / false]")
                    .translation(EnchantWithMob.MODID + ".config.naturalSpawnEnchantedMob")
                    .define("Enchanted Mob can Spawn Natural", true);
            ENCHANT_ON_SPAWN_EXCLUSION_MOBS = builder
                    .comment("Disables specific mob from receiveing enchantments on spawn. Use the full name, eg: minecraft:ender_dragon.")
                    .define("enchantOnSpawnExclusionMobs", Lists.newArrayList("minecraft:wither", "minecraft:ender_dragon"));
            ALWAY_ENCHANTABLE_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments on spawn. Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableMobs", Lists.newArrayList());
            ALWAY_ENCHANTABLE_ANCIENT_MOBS = builder
                    .comment("Allow the specific mob from alway receiveing enchantments as Ancient Mob on spawn(This feature may break for balance so be careful). Use the full name, eg: minecraft:zombie.")
                    .define("alwayEnchantableAncientMobs", Lists.newArrayList());
            DISABLE_ENCHANTS = builder
                    .comment("Disables the specific mob enchant. Use the full name(This config only disabled mob enchant when mob spawn. not mean delete complete, eg: enchantwithmob:thorn.")
                    .define("disableMobEnchants", Lists.newArrayList());
            ALLOW_POISON_CLOUD_PROJECTILE = builder
                    .comment("Allow the poison cloud for projectile. Use the full name(eg: minecraft:arrow.")
                    .define("allowPoisonCloudProjectiles", Lists.newArrayList("minecraft:arrow", "minecraft:snowball", "earthmobsmod:melon_seeds", "earthmobsmod:zombie_flesh", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball", "tofucraft:fukumame", "tofucraft:nether_fukumame", "tofucraft:soul_fukumame"));
            ALLOW_MULTISHOT_PROJECTILE = builder
                    .comment("Allow the multi shot for projectile. Use the full name(eg: minecraft:arrow.")
                    .define("allowMultiShotProjectiles", Lists.newArrayList("minecraft:arrow", "minecraft:snowball", "earthmobsmod:melon_seeds", "earthmobsmod:zombie_flesh", "conjurer_illager:throwing_card", "conjurer_illager:bouncy_ball", "tofucraft:fukumame", "tofucraft:nether_fukumame", "tofucraft:soul_fukumame"));

            WHITELIST_SHOOT_ENTITY = builder
                    .comment("Whitelist the projectile mob enchant for mob. Use the full name(eg: minecraft:zombie.")
                    .define("whitelistShootEntity", Lists.newArrayList("minecraft:skeleton", "minecraft:pillager", "minecraft:shulker", "minecraft:llama", "conjurer_illager:conjurer", "earthmobsmod:bone_spider", "earthmobsmod:lobber_zombie", "earthmobsmod:lobber_drowned"
                            , "earthmobsmod:melon_golem", "minecraft:piglin", "minecraft:snow_golem", "minecraft:player"));

            buffedDragon = builder
                    .comment("Set the MobEnchant on the EnderDragon. If you want more harder fight. should set it! eg: enchantwithmob:thorn.")
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
                    .define("UniversalEnchant", false);
            difficultyBasePercent = builder
                    .comment("Set The Difficulty Base Enchanted Mob Spawn Percent. [(Difficulty Base Percent * Difficulty id) + (Effective Difficulty Percent * Effective Difficulty)]")
                    .defineInRange("Difficulty Enchanted Spawn Percent", 0.005D, 0.0D, 1D);
            effectiveBasePercent = builder
                    .comment("Set The Effective Difficulty Base Enchanted Mob Spawn Percent [(Difficulty Base Percent * Difficulty id) + (Effective Difficulty Percent * Effective Difficulty)]")
                    .defineInRange("Effective Difficulty Enchanted Spawn Percent", 0.025D, 0.0D, 1D);
            disableEnchanterArmor = builder
                    .comment("Disable Enchanter Armor Item. [true / false]")
                    .define("Disable Enchanter Armor", false);
            disableMobEnchantStuffItems = builder
                    .comment("Disable MobEnchant Stuff Items. [true / false]")
                    .define("Disable MobEnchant Stuff Items", false);

        }
    }

}
