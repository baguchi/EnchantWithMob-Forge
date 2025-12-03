package baguchi.enchantwithmob.client.render;

import baguchi.bagus_lib.client.layer.CustomArmorLayer;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.client.ModModelLayers;
import baguchi.enchantwithmob.client.model.EnchanterModel;
import baguchi.enchantwithmob.client.render.state.EnchanterRenderState;
import baguchi.enchantwithmob.entity.Enchanter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.item.CrossbowItem;


public class EnchanterRenderer extends MobRenderer<Enchanter, EnchanterRenderState, EnchanterModel<EnchanterRenderState>> {
    private static final Identifier ILLAGER = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchanter_clothed.png");

    private static final Identifier GLOW = Identifier.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/entity/enchanter_clothed_glow.png");
    private static final RenderType GLOW_TYPE = RenderTypes.eyes(GLOW);


    public EnchanterRenderer(EntityRendererProvider.Context p_173952_) {
        super(p_173952_, new EnchanterModel<>(p_173952_.bakeLayer(ModModelLayers.ENCHANTER)), 0.5F);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return GLOW_TYPE;
            }
        });
        this.addLayer(new CustomArmorLayer<>(this, p_173952_));

    }

    @Override
    public EnchanterRenderState createRenderState() {
        return new EnchanterRenderState();
    }

    @Override
    public void extractRenderState(Enchanter p_365030_, EnchanterRenderState p_364586_, float p_360560_) {
        super.extractRenderState(p_365030_, p_364586_, p_360560_);
        p_364586_.isRiding = p_365030_.isPassenger();
        p_364586_.mainArm = p_365030_.getMainArm();
        p_364586_.armPose = p_365030_.getArmPose();
        p_364586_.maxCrossbowChargeDuration = p_364586_.armPose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE
                ? CrossbowItem.getChargeDuration(p_365030_.getUseItem(), p_365030_)
                : 0;
        p_364586_.ticksUsingItem = p_365030_.getTicksUsingItem();
        p_364586_.attackAnim = p_365030_.getAttackAnim(p_360560_);
        p_364586_.isAggressive = p_365030_.isAggressive();
        p_364586_.attackAnimationState.copyFrom(p_365030_.attackAnimationState);
        p_364586_.castingAnimationState.copyFrom(p_365030_.castingAnimationState);
        p_364586_.idleAnimationState.copyFrom(p_365030_.idleAnimationState);
    }

    @Override
    public Identifier getTextureLocation(EnchanterRenderState p_110775_1_) {
        return ILLAGER;
    }
}