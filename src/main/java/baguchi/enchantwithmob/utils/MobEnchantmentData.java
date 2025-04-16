package baguchi.enchantwithmob.utils;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.core.Holder;

public class MobEnchantmentData {
	public final Holder<MobEnchant> enchantment;
	public final int enchantmentLevel;

	public MobEnchantmentData(Holder<MobEnchant> enchantmentObj, int enchLevel) {
		this.enchantment = enchantmentObj;
		this.enchantmentLevel = enchLevel;
	}

    public Holder<MobEnchant> getEnchantment() {
        return enchantment;
    }

    public int weight() {
        return this.getEnchantment().value().getRarity().getWeight();
    }

}