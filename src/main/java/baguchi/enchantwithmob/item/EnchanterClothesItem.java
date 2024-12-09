package baguchi.enchantwithmob.item;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.client.ModModelLayers;
import baguchi.enchantwithmob.client.model.EnchanterClothesModel;
import baguchi.enchantwithmob.registry.ModArmorMaterials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnchanterClothesItem extends ArmorItem {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, "textures/models/armor/enchanter_clothes.png");
    public EnchanterClothesItem(ArmorItem.Type type, Properties p_40388_) {
        super(ModArmorMaterials.ENCHANTER_CLOTHES, type, p_40388_);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return TEXTURE;
    }

    @Override
    public boolean isEnabled(FeatureFlagSet p_249172_) {
        return super.isEnabled(p_249172_) && !EnchantConfig.COMMON.disableEnchanterArmor.get();
    }

    public static final class ArmorRender implements IClientItemExtensions {
        public static final ArmorRender INSTANCE = new ArmorRender();


        @Override
        public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            EntityModelSet models = Minecraft.getInstance().getEntityModels();
            ModelPart root = models.bakeLayer(ModModelLayers.ENCHANTER_CLOTHES);

            EnchanterClothesModel<?> model2 = new EnchanterClothesModel<>(root);
            ClientHooks.copyModelProperties(original, model2);
            model2.rightBoots.copyFrom(model2.rightLeg);
            model2.leftBoots.copyFrom(model2.leftLeg);
            this.setPartVisibility(model2, equipmentSlot);
            return model2;
        }

        protected void setPartVisibility(EnchanterClothesModel p_117126_, EquipmentSlot p_117127_) {
            p_117126_.setAllVisible(false);
            switch (p_117127_) {
                case HEAD:
                    p_117126_.head.visible = true;
                    p_117126_.hat.visible = true;
                    break;
                case CHEST:
                    p_117126_.body.visible = true;
                    p_117126_.rightArm.visible = true;
                    p_117126_.leftArm.visible = true;
                    break;
                case LEGS:
                    p_117126_.rightLeg.visible = true;
                    p_117126_.leftLeg.visible = true;
                    break;
                case FEET:
                    p_117126_.rightBoots.visible = true;
                    p_117126_.leftBoots.visible = true;
            }

        }

    }
}