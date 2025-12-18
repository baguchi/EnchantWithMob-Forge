package baguchi.enchantwithmob.client.render.layer;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.client.ClientRegistrar;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;


public class EnchantedEyesLayer<T extends LivingEntityRenderState, M extends EntityModel<T>> extends EyesLayer<T, M> {

    private static final Function<Identifier, RenderType> ENCHANTED_EYES = Util.memoize((p_173253_) -> {
        return RenderType.create("enchanted_eye", RenderSetup.builder(ClientRegistrar.MOB_ENCHANT_EYE)
                .withTexture("Sampler0", p_173253_).createRenderSetup());
    });

	public final RenderType render_types;

	public EnchantedEyesLayer(RenderLayerParent<T, M> p_116964_, RenderType renderType) {
		super(p_116964_);

		this.render_types = renderType;
	}

	@Override
    public void submit(PoseStack p_116983_, SubmitNodeCollector submitNodeCollector, int p_116985_, T entity, float p_116987_, float p_116988_) {
        boolean enchanted = entity.getRenderDataOrDefault(EnchantLayer.ENCHANTED, false);

        if (enchanted && !EnchantConfig.CLIENT.disableAuraRender.get()) {
            submitNodeCollector.submitModel(this.getParentModel(), entity, p_116983_, this.renderType(), p_116985_, OverlayTexture.NO_OVERLAY, -1,
                        null,
                        entity.outlineColor,
                        null);
		}
	}

    public static RenderType enchantedEyes(Identifier p_110455_) {
		return ENCHANTED_EYES.apply(p_110455_);
	}

	public RenderType renderType() {
		return render_types;
	}
}