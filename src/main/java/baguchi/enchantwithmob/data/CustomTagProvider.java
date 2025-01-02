package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModRegistries;
import baguchi.enchantwithmob.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;

import java.util.concurrent.CompletableFuture;

public class CustomTagProvider {

    public static class MobEnchantTagGenerator extends TagsProvider<MobEnchant> {

        public MobEnchantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
            super(output, ModRegistries.MOB_ENCHANT, provider, EnchantWithMob.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider p_256380_) {

            this.tag(ModTags.MobEnchantTags.POISON).add(MobEnchants.POISON_CLOUD).add(MobEnchants.POISON);
            this.tag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.TOUGH).add(MobEnchants.HEALTH_BOOST).add(MobEnchants.PROTECTION)
                    .add(MobEnchants.MULTISHOT).add(MobEnchants.THORN).add(MobEnchants.WIND).add(MobEnchants.SPEEDY)
                    .add(MobEnchants.DEFLECT).add(MobEnchants.POISON_CLOUD).add(MobEnchants.POISON);
            this.tag(ModTags.MobEnchantTags.RANDOM_LOOT).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN);
            this.tag(ModTags.MobEnchantTags.TOOLTIP_ORDER).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.WIND).add(MobEnchants.SOUL_STEAL);
        }
    }
}
