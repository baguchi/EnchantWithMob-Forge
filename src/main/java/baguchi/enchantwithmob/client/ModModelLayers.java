package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModModelLayers {
    public static ModelLayerLocation ENCHANTER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanter"), "main");
    public static ModelLayerLocation ENCHANTED_WIND = new ModelLayerLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanted_wind"), "main");
    public static ModelLayerLocation ENCHANTER_CLOTHES = new ModelLayerLocation(Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanter_clothes"), "main");

}
