package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.data.resources.registries.MobEnchantEyes;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;

public class RegistryDataGenerator {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY, MobEnchantTypes::bootstrap)
            .add(MobEnchantEyes.MOB_ENCHANT_EYE_REGISTRY_KEY, MobEnchantEyes::bootstrap);


    public static HolderLookup.Provider createWorldLookup() {
        RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        HolderLookup.Provider newRegistries = BUILDER.build(staticRegistries);
        return newRegistries;
    }
}