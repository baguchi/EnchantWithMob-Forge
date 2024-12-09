package baguchi.enchantwithmob.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class ItemMobEnchantCapability implements INBTSerializable<CompoundTag> {
	private boolean hasEnchant;

	public boolean hasEnchant() {
		return hasEnchant;
	}

	public void setHasEnchant(boolean hasEnchant) {
		this.hasEnchant = hasEnchant;
	}


	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag nbt = new CompoundTag();

		nbt.putBoolean("HasEnchant", hasEnchant);

		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
		hasEnchant = compoundTag.getBoolean("HasEnchant");
	}
}