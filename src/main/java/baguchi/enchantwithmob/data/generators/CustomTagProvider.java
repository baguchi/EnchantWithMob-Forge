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

            this.tag(ModTags.MobEnchantTags.ENCHANTER_ENCHANT).add(MobEnchants.TOUGH.getKey()).add(MobEnchants.HEALTH_BOOST.getKey()).add(MobEnchants.PROTECTION.getKey()).add(MobEnchants.THORN.getKey()).add(MobEnchants.STRONG.getKey());
            this.tag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.TOUGH.getKey()).add(MobEnchants.HEALTH_BOOST.getKey()).add(MobEnchants.PROTECTION.getKey())
                    .add(MobEnchants.THORN.getKey()).add(MobEnchants.STRONG.getKey()).add(MobEnchants.SPEEDY.getKey())
                    .add(MobEnchants.DEFLECT.getKey()).add(MobEnchants.POISON_CLOUD.getKey()).add(MobEnchants.POISON.getKey()).add(MobEnchants.BOUNCE.getKey())
                    .add(MobEnchants.FROZEN.getKey()).add(MobEnchants.FIRE_ASPECT.getKey());
            this.tag(ModTags.MobEnchantTags.RANDOM_LOOT).add(MobEnchants.TOUGH.getKey()).add(MobEnchants.HEALTH_BOOST.getKey()).add(MobEnchants.PROTECTION.getKey())
                    .add(MobEnchants.THORN.getKey()).add(MobEnchants.STRONG.getKey()).add(MobEnchants.SPEEDY.getKey())
                    .add(MobEnchants.DEFLECT.getKey()).add(MobEnchants.POISON_CLOUD.getKey()).add(MobEnchants.POISON.getKey())
                    .add(MobEnchants.FROZEN.getKey()).add(MobEnchants.FIRE_ASPECT.getKey());

            this.tag(ModTags.MobEnchantTags.TOOLTIP_ORDER).addTag(ModTags.MobEnchantTags.RANDOM_SPAWN).add(MobEnchants.WIND.getKey()).add(MobEnchants.SOUL_STEAL.getKey());

            this.tag(ModTags.MobEnchantTags.AFFECT_SELF_REFLECT)
                    .add(MobEnchants.BOUNCE.getKey())
                    .add(MobEnchants.THORN.getKey())
                    .add(MobEnchants.DEFLECT.getKey());


            this.tag(ModTags.MobEnchantTags.AFFECT_SPEED)
                    .add(MobEnchants.SPEEDY.getKey());

            this.tag(ModTags.MobEnchantTags.POST_ATTACK)
                    .add(MobEnchants.POISON.getKey())
                    .add(MobEnchants.POISON_CLOUD.getKey())
                    .add(MobEnchants.FROZEN.getKey()).add(MobEnchants.FIRE_ASPECT.getKey());

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
