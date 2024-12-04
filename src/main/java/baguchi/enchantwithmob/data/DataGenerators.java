package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        event.addProvider(new ItemModelGenerator(packOutput, existingFileHelper));
        BlockTagGenerator blockTagsProvider = new BlockTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper());

        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ItemTagGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(true, new EntityTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(true, new CustomTagProvider.MobEnchantTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(true, new Runner(packOutput, lookupProvider));
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
            return new CraftingGenerator(lookupProvider, output);
        }

        @Override
        public String getName() {
            return EnchantWithMob.MODID + "recipes";
        }
    }
}