package baguchi.enchantwithmob.utils;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.core.Holder;
import net.minecraft.util.random.WeightedEntry;

public class MobEnchantmentData extends WeightedEntry.IntrusiveBase {
    public final Holder<MobEnchant> enchantment;
    public final int enchantmentLevel;

    public MobEnchantmentData(Holder<MobEnchant> enchantmentObj, int enchLevel) {
        super(enchantmentObj.value().getRarity().getWeight());
        this.enchantment = enchantmentObj;
        this.enchantmentLevel = enchLevel;
    }
}