package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

import static baguchi.enchantwithmob.EnchantWithMob.prefix;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, EnchantWithMob.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.singleTex(ModItems.ENCHANATERS_BOTTLE);
        this.singleTex(ModItems.ENCHANATERS_EXPERIENCE_BOTTLE);
        this.singleTex(ModItems.ENCHANTERS_BOOK);
        this.singleTex(ModItems.MOB_ENCHANT_BOOK);
    }

    private ItemModelBuilder singleTex(Supplier<Item> item) {
        return generated(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), prefix("item/" + BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }

    public ItemModelBuilder egg(Supplier<Item> item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder generated(String name, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, "item/generated");
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        return builder;
    }
}
