package baguchi.enchantwithmob.data.generators;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import baguchi.enchantwithmob.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;

import java.util.concurrent.CompletableFuture;

public class CustomTagProvider {

    public static class MobEnchantTagGenerator extends TagsProvider<MobEnchant> {

        public MobEnchantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
            super(output, MobEnchants.MOB_ENCHANT_REGISTRY, provider, EnchantWithMob.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider p_256380_) {

            this.tag(ModTags.MobEnchantTags.ENCHANTER_ENCHANT).add(MobEnchants.TOUGH.key()).add(MobEnchants.HEALTH_BOOST.key()).add(MobEnchants.PROTECTION.key()).add(MobEnchants.THORN.key()).add(MobEnchants.STRONG.key());
            this.tag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.TOUGH.key()).add(MobEnchants.HEALTH_BOOST.key()).add(MobEnchants.PROTECTION.key())
                    .add(MobEnchants.THORN.key()).add(MobEnchants.STRONG.key()).add(MobEnchants.SPEEDY.key())
                    .add(MobEnchants.DEFLECT.key()).add(MobEnchants.POISON_CLOUD.key()).add(MobEnchants.POISON.key()).add(MobEnchants.BOUNCE.key())
                    .add(MobEnchants.FROZEN.key()).add(MobEnchants.FIRE_ASPECT.key());
            this.tag(ModTags.MobEnchantTags.RANDOM_LOOT).add(MobEnchants.TOUGH.key()).add(MobEnchants.HEALTH_BOOST.key()).add(MobEnchants.PROTECTION.key())
                    .add(MobEnchants.THORN.key()).add(MobEnchants.STRONG.key()).add(MobEnchants.SPEEDY.key())
                    .add(MobEnchants.DEFLECT.key()).add(MobEnchants.POISON_CLOUD.key()).add(MobEnchants.POISON.key())
                    .add(MobEnchants.FROZEN.key()).add(MobEnchants.FIRE_ASPECT.key());

            this.tag(ModTags.MobEnchantTags.TOOLTIP_ORDER).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.WIND.key()).add(MobEnchants.SOUL_STEAL.key());

            this.tag(ModTags.MobEnchantTags.AFFECT_SELF_REFLECT)
                    .add(MobEnchants.BOUNCE.key())
                    .add(MobEnchants.THORN.key())
                    .add(MobEnchants.DEFLECT.key());


            this.tag(ModTags.MobEnchantTags.AFFECT_SPEED)
                    .add(MobEnchants.SPEEDY.key());

            this.tag(ModTags.MobEnchantTags.POST_ATTACK)
                    .add(MobEnchants.POISON.key())
                    .add(MobEnchants.POISON_CLOUD.key())
                    .add(MobEnchants.FROZEN.key()).add(MobEnchants.FIRE_ASPECT.key());

        }
    }

    public static class MobEnchantTypeTagGenerator extends TagsProvider<MobEnchantType> {

        public MobEnchantTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
            super(output, MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY, provider, EnchantWithMob.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider p_256380_) {
            this.tag(ModTags.MobEnchantTypeTags.PREVENT_REMOVE_SELF).add(MobEnchantTypes.ANCIENT);
        }
    }
}
