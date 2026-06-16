package baguchi.enchantwithmob.data.generators;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends EntityTypeTagsProvider {
    public EntityTagGenerator(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_) {
        super(p_256095_, p_256572_, EnchantWithMob.MODID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(EntityTypeTags.ILLAGER).add(ModEntities.ENCHANTER.getKey());
        this.tag(EntityTypeTags.RAIDERS).add(ModEntities.ENCHANTER.getKey());
    }
}