package baguchi.enchantwithmob.attachment;

import baguchi.enchantwithmob.api.MobEnchantType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityReference;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class MobEnchantSyncHandler implements AttachmentSyncHandler<MobEnchantAttachment> {
    @Override
    public void write(RegistryFriendlyByteBuf buf, MobEnchantAttachment attachment, boolean initialSync) {
        buf.writeVarInt(attachment.mobEnchants.size());

        for (int i = 0; i < attachment.mobEnchants.size(); i++) {
            buf.writeJsonWithCodec(MobEnchantContent.CODEC, attachment.mobEnchants.get(i));
        }
        buf.writeBoolean(attachment.enchantOwner.isPresent());

        attachment.enchantOwner.ifPresent(livingEntityEntityReference -> buf.writeJsonWithCodec(EntityReference.codec(), livingEntityEntityReference));

        buf.writeJsonWithCodec(MobEnchantType.CODEC, attachment.mobEnchantType);
    }

    @Override
    public @Nullable MobEnchantAttachment read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable MobEnchantAttachment previousValue) {
        int size = buf.readVarInt();

        MobEnchantAttachment attachment = new MobEnchantAttachment();
        attachment.mobEnchants.clear();

        for (int i = 0; i < size; i++) {
            attachment.mobEnchants.add(buf.readLenientJsonWithCodec(MobEnchantContent.CODEC));
        }
        if (buf.readBoolean()) {
            attachment.enchantOwner = Optional.of(buf.readLenientJsonWithCodec(EntityReference.codec()));
        }
        attachment.mobEnchantType = buf.readLenientJsonWithCodec(MobEnchantType.CODEC);

        return attachment;
    }
}
