package baguchi.enchantwithmob.attachment;

import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.MobEnchants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

public class MobEnchantContent {
	public static final Codec<MobEnchantContent> CODEC = RecordCodecBuilder.create(
			i -> i.group(
							MobEnchants.getRegistry().holderByNameCodec().fieldOf("mob_enchant").forGetter(MobEnchantContent::getMobEnchant),
							Codec.INT.fieldOf("enchant_level").forGetter(MobEnchantContent::getEnchantLevel)
					)
					.apply(i, MobEnchantContent::new)
	);
	private Holder<MobEnchant> mobEnchant;
    private int enchantLevel;

	public MobEnchantContent(Holder<MobEnchant> mobEnchant, int enchantLevel) {
        this.mobEnchant = mobEnchant;
        this.enchantLevel = enchantLevel;
    }


	public Holder<MobEnchant> getMobEnchant() {
		return mobEnchant;
	}

	public int getEnchantLevel() {
		return enchantLevel;
	}

	public void setEnchantLevel(int enchantLevel) {
		this.enchantLevel = enchantLevel;
	}

	public void setMobEnchant(Holder<MobEnchant> mobEnchant) {
		this.mobEnchant = mobEnchant;
	}
}
