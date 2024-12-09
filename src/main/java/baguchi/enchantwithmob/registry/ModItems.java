package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, EnchantWithMob.MODID);

    public static final Supplier<Item> ENCHANTERS_BOOK = ITEM_REGISTRY.register("enchanters_book", () -> new EnchantersBookItem((new Item.Properties()).stacksTo(1).durability(64)));
    public static final Supplier<Item> MOB_ENCHANT_BOOK = ITEM_REGISTRY.register("mob_enchant_book", () -> new MobEnchantBookItem((new Item.Properties()).stacksTo(1).durability(5)));
    public static final Supplier<Item> ENCHANATERS_BOTTLE = ITEM_REGISTRY.register("enchanters_bottle", () -> new EnchanterBottleItem((new Item.Properties()).stacksTo(16)));
    public static final Supplier<Item> ENCHANATERS_EXPERIENCE_BOTTLE = ITEM_REGISTRY.register("enchanters_experience_bottle", () -> new EnchanterExperienceBottleItem((new Item.Properties()).craftRemainder(ENCHANATERS_BOTTLE.get()).stacksTo(1)));
    public static final Supplier<Item> ENCHANTER_SPAWNEGG = ITEM_REGISTRY.register("enchanter_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.ENCHANTER, 9804699, 0x81052d, (new Item.Properties())));
    public static final Supplier<Item> ENCHANTER_CLOTHES = ITEM_REGISTRY.register("enchanter_clothes", () -> new EnchanterClothesItem(ArmorItem.Type.CHESTPLATE, (new Item.Properties().durability(214))));
    public static final Supplier<Item> ENCHANTER_HAT = ITEM_REGISTRY.register("enchanter_hat", () -> new EnchanterClothesItem(ArmorItem.Type.HELMET, (new Item.Properties().durability(162))));
    public static final Supplier<Item> ENCHANTER_BOOTS = ITEM_REGISTRY.register("enchanter_boots", () -> new EnchanterClothesItem(ArmorItem.Type.BOOTS, (new Item.Properties().durability(154))));
    public static final Supplier<Item> ENCHANTER_LEGGINGS = ITEM_REGISTRY.register("enchanter_leggings", () -> new EnchanterClothesItem(ArmorItem.Type.LEGGINGS, (new Item.Properties().durability(182))));

}
