package baguchi.enchantwithmob.mobenchant;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableFloat;

public class ProtectionMobEnchant extends MobEnchant {
    public ProtectionMobEnchant(Properties properties) {
        super(properties);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 11;
    }

    @Override
    public void modifyDamageProtection(ServerLevel level, int p344605, LivingEntity entity, DamageSource damageSource, MutableFloat mutablefloat) {
        mutablefloat.setValue(mutablefloat.getValue() + Mth.floor((float) ((6 + p344605) * 1.5F) / 3));
    }
}
