package baguchi.enchantwithmob.compat;

import baguchi.enchantwithmob.EnchantWithMob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = EnchantWithMob.MODID, value = Dist.CLIENT)
public class GeckoLibCompatClient {
	@SubscribeEvent
	public static void entityAddLayerEvent(EntityRenderersEvent.AddLayers event) {
		if (GeckoLibCompat.isLoaded) {
			GeckoLibCompatClientRenderer.entityAddLayerEvent(event);
		}
	}
}
