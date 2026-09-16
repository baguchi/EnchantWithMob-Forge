package baguchi.enchantwithmob.data.generators.crafting;

import baguchi.enchantwithmob.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class CraftingGenerator extends RecipeProvider {
    public CraftingGenerator(final BootstrapContext<Recipe<?>> recipeOutput, final BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, new ItemStackTemplate(ModItems.ENCHANATERS_BOTTLE.get(), 3))
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
