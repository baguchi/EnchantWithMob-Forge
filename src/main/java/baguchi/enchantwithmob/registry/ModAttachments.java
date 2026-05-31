package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.attachment.ItemMobEnchantAttachment;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.attachment.MobEnchantSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, EnchantWithMob.MODID);
    public static final Supplier<AttachmentType<ItemMobEnchantAttachment>> ITEM_MOB_ENCHANT = ATTACHMENT_TYPES.register(
            "item_mob_enchant", () -> AttachmentType.serializable(ItemMobEnchantAttachment::new).build());
    public static final Supplier<AttachmentType<MobEnchantAttachment>> MOB_ENCHANTS = ATTACHMENT_TYPES.register(
            "mob_enchants", () -> AttachmentType.serializable(MobEnchantAttachment::new).sync(new MobEnchantSyncHandler()).build());
}
