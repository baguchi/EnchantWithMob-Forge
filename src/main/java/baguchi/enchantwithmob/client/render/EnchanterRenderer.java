package baguchi.enchantwithmob.client.render;

import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.client.ModModelLayers;
import baguchi.enchantwithmob.client.model.EnchanterModel;
import baguchi.enchantwithmob.entity.Enchanter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnchanterRenderer<T extends Enchanter> extends MobRenderer<T, EnchanterModel<T>> {
    private static final ResourceLocation ILLAGER = ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchanter_clothed.png");

    private static final ResourceLocation GLOW = ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchanter_clothed_glow.png");
    private static final RenderType GLOW_TYPE = RenderType.eyes(GLOW);


    public EnchanterRenderer(EntityRendererProvider.Context p_173952_) {
        super(p_173952_, new EnchanterModel<>(p_173952_.bakeLayer(ModModelLayers.ENCHANTER)), 0.5F);
        this.addLayer(new EyesLayer<T, EnchanterModel<T>>(this) {
            @Override
            public RenderType renderType() {
                return GLOW_TYPE;
            }
        });
        this.addLayer(new CustomArmorLayer<>(this, p_173952_));

    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return ILLAGER;
    }
}