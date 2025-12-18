package baguchi.enchantwithmob.data.generators;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, EnchantWithMob.MODID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.MOB_ENCHANT_BOOK.get());
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.ENCHANTERS_BOOK.get());
    }
}
