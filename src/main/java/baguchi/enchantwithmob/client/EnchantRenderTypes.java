package baguchi.enchantwithmob.client;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class EnchantRenderTypes {
    public static final Function<Identifier, RenderType> MOB_ENCHANT = Util.memoize(
            texture -> {
                return RenderType.create("enchant_effect", RenderSetup.builder(RenderPipelines.GLINT)
                        .withTexture("Sampler0", texture)
                        .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                        .withForcedSolidModelPhase()
                        .createRenderSetup());
            }
    );
    public static final Function<Identifier, RenderType> MOB_ENCHANT_BEAM = Util.memoize(
            texture -> {
                return RenderType.create("enchant_beam_effect", RenderSetup.builder(ClientRegistrar.MOB_ENCHANT_BEAM)
                        .setOitPipelines(ClientRegistrar.OIT_MOB_ENCHANT_BEAM)
                        .withTexture("Sampler0", texture)
                        .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                        .withForcedSolidModelPhase()
                        .createRenderSetup());
            }
    );

    public static final Function<Identifier, RenderType> ENCHANTED_EYES = Util.memoize((p_173253_) -> {
        return RenderType.create("enchanted_eye", RenderSetup.builder(RenderPipelines.EYES)
                .setOitPipelines(RenderPipelines.OIT_EYES)
                .useOverlay()
                .affectsCrumbling()
                .withTexture("Sampler0", p_173253_).createRenderSetup());
    });
}
