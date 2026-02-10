package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(packOutput, event.getLookupProvider());

        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), datapackProvider);

        BlockTagGenerator blockTagsProvider = new BlockTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ItemTagGenerator(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTagGenerator(packOutput, lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CustomTagProvider.MobEnchantTagGenerator(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new CustomTagProvider.MobEnchantTypeTagGenerator(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new CraftingGenerator(packOutput, lookupProvider));
    }
}