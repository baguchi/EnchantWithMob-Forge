package baguchi.enchantwithmob.compat;

import baguchi.enchantwithmob.client.render.layer.GeoEnchantLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class GeckoLibCompatClientRenderer {
    public static void entityAddLayerEvent(EntityRenderersEvent.AddLayers event) {
        event.getEntityTypes().forEach(entityType -> {
            if (event.getRenderer(entityType) instanceof GeoEntityRenderer<?> r) {
                (r).addRenderLayer(new GeoEnchantLayer(r));
            }
        });
    }
}
