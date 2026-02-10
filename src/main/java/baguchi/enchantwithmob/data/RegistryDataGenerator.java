package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantEyes;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY, MobEnchantTypes::bootstrap)
            .add(MobEnchantEyes.MOB_ENCHANT_EYE_REGISTRY_KEY, MobEnchantEyes::bootstrap);



    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", EnchantWithMob.MODID));
    }


}