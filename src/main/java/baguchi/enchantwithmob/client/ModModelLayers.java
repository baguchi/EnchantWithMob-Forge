package baguchi.enchantwithmob.client;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
	public static ModelLayerLocation ENCHANTER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanter"), "main");
	public static ModelLayerLocation ENCHANTED_WIND = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanted_wind"), "main");
	public static ModelLayerLocation ENCHANTER_CLOTHES = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "enchanter_clothes"), "main");

}
