package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModRegistries;
import baguchi.enchantwithmob.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CustomTagProvider {

    public static class MobEnchantTagGenerator extends TagsProvider<MobEnchant> {

        public MobEnchantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, ModRegistries.MOB_ENCHANT, provider, EnchantWithMob.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider p_256380_) {
            this.tag(ModTags.MobEnchantTags.TOOLTIP_ORDER).add(MobEnchants.TOUGH).add(MobEnchants.HEALTH_BOOST).add(MobEnchants.PROTECTION);

            this.tag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.TOUGH).add(MobEnchants.HEALTH_BOOST).add(MobEnchants.PROTECTION);
            this.tag(ModTags.MobEnchantTags.RANDOM_LOOT).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN);
        }
    }
}
