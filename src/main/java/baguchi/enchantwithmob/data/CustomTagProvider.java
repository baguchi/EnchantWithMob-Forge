package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;

import java.util.concurrent.CompletableFuture;

public class CustomTagProvider {

    public static class MobEnchantTagGenerator extends KeyTagProvider<MobEnchant> {

        public MobEnchantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
            super(output, MobEnchants.MOB_ENCHANT_REGISTRY, provider, EnchantWithMob.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider p_256380_) {

            this.tag(ModTags.MobEnchantTags.ENCHANTER_ENCHANT).add(MobEnchants.TOUGH.getKey()).add(MobEnchants.HEALTH_BOOST.getKey()).add(MobEnchants.PROTECTION.getKey()).add(MobEnchants.THORN.getKey()).add(MobEnchants.STRONG.getKey());
            this.tag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.TOUGH.getKey()).add(MobEnchants.HEALTH_BOOST.getKey()).add(MobEnchants.PROTECTION.getKey())
                    .add(MobEnchants.THORN.getKey()).add(MobEnchants.STRONG.getKey()).add(MobEnchants.SPEEDY.getKey())
                    .add(MobEnchants.DEFLECT.getKey()).add(MobEnchants.POISON_CLOUD.getKey()).add(MobEnchants.POISON.getKey());
            this.tag(ModTags.MobEnchantTags.RANDOM_LOOT).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN);
            this.tag(ModTags.MobEnchantTags.TOOLTIP_ORDER).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.WIND.getKey()).add(MobEnchants.SOUL_STEAL.getKey());
        }
    }
}
