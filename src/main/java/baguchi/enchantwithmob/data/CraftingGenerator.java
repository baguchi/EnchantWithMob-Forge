package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftingGenerator extends RecipeProvider {
    public CraftingGenerator(HolderLookup.Provider p_248933_, RecipeOutput p_323846_) {
        super(p_248933_, p_323846_);
    }

    @Override
    protected void buildRecipes() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, new ItemStack(ModItems.ENCHANATERS_BOTTLE.get(), 3))
                .pattern(" G ")
                .pattern("ALA")
                .pattern(" A ")
                .define('A', Items.AMETHYST_SHARD)
                .define('L', Items.LAPIS_LAZULI)
                .define('G', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(this.output);
    }
}
