package baguchi.enchantwithmob.capability;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;

public class MobEnchantHandler {
	private Holder<MobEnchant> mobEnchant;
    private int enchantLevel;

	public MobEnchantHandler(Holder<MobEnchant> mobEnchant, int enchantLevel) {
        this.mobEnchant = mobEnchant;
        this.enchantLevel = enchantLevel;
    }


	public Holder<MobEnchant> getMobEnchant() {
		return mobEnchant;
	}

	public int getEnchantLevel() {
		return enchantLevel;
	}

	public CompoundTag writeNBT(RegistryAccess registryAccess) {
		CompoundTag nbt = new CompoundTag();

		if (mobEnchant != null) {
			nbt.putString("MobEnchant", registryAccess.lookupOrThrow(MobEnchants.MOB_ENCHANT_REGISTRY).getKey(mobEnchant.value()).toString());
			nbt.putInt("EnchantLevel", enchantLevel);
		}

		return nbt;
	}

	public void setEnchantLevel(int enchantLevel) {
		this.enchantLevel = enchantLevel;
	}

	public void setMobEnchant(Holder<MobEnchant> mobEnchant) {
		this.mobEnchant = mobEnchant;
	}
}
