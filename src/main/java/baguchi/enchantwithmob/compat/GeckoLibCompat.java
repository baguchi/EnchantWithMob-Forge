package baguchi.enchantwithmob.compat;

import baguchi.enchantwithmob.EnchantWithMob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GeckoLibCompat {
	public static boolean isLoaded = false;
	public static final String GECKO_LIB_MOD_ID = "geckolib3";

	@SubscribeEvent
	public static void onInterMod(InterModProcessEvent event) {
		if (ModList.get().isLoaded(GECKO_LIB_MOD_ID)) {
			isLoaded = true;
		}
	}
}
