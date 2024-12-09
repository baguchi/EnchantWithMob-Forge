package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.capability.ItemMobEnchantCapability;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModCapability {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, EnchantWithMob.MODID);
    public static final Supplier<AttachmentType<ItemMobEnchantCapability>> ITEM_MOB_ENCHANT = ATTACHMENT_TYPES.register(
            "item_mob_enchant", () -> AttachmentType.serializable(ItemMobEnchantCapability::new).build());
}
