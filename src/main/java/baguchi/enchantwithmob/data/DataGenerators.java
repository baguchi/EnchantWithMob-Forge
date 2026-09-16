package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.data.generators.*;
import baguchi.enchantwithmob.data.generators.crafting.CraftingGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        event.createWorldRegistryObjects(RegistryDataGenerator.BUILDER);

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getWorldLookupProvider();

        BlockTagGenerator blockTagsProvider = new BlockTagGenerator(packOutput, lookupProvider);

        generator.addProvider(true, new LootModifierProviderFactory().create(event, lookupProvider));

        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ItemTagGenerator(packOutput, lookupProvider));
        generator.addProvider(true, new EntityTagGenerator(packOutput, lookupProvider));
        generator.addProvider(true, new CustomTagProvider.MobEnchantTagGenerator(packOutput, lookupProvider));
        generator.addProvider(true, new CustomTagProvider.MobEnchantTypeTagGenerator(packOutput, lookupProvider));
        event.createReloadableRegistryObjects(
                new RegistrySetBuilder()
                        .add(RecipeProvider.asBootstrap(CraftingGenerator::new)),
                Set.of("minecraft", EnchantWithMob.MODID));

    }

}