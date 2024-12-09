package baguchi.enchantwithmob.data;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper exFileHelper) {
        super(packOutput, lookupProvider, provider, EnchantWithMob.MODID, exFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.MOB_ENCHANT_BOOK.get());
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.ENCHANTERS_BOOK.get());
    }
}
