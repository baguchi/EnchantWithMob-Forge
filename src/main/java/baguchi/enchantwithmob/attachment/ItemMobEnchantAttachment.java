package baguchi.enchantwithmob.attachment;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class ItemMobEnchantAttachment implements ValueIOSerializable {
	private boolean hasEnchant;

	public boolean hasEnchant() {
		return hasEnchant;
	}

	public void setHasEnchant(boolean hasEnchant) {
		this.hasEnchant = hasEnchant;
	}

	@Override
	public void serialize(ValueOutput output) {
		if (hasEnchant) {
			output.putBoolean("HasEnchant", hasEnchant);
		}
	}

	@Override
	public void deserialize(ValueInput valueInput) {
		hasEnchant = valueInput.getBooleanOr("HasEnchant", false);
	}
}