package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.item.MobEnchantBookItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = EnchantWithMob.MODID)
public class ModCreativeTabs {
    @SubscribeEvent
    public static void registerCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.ENCHANTER_SPAWNEGG.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            MobEnchantBookItem.generateEnchantmentBookTypesOnlyMaxLevel(event, event.getParameters().holders().lookupOrThrow(ModRegistries.MOB_ENCHANT), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.ENCHANATERS_BOTTLE.get());
        }
    }
}
