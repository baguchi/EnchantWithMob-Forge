package baguchi.enchantwithmob.message;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class RemoveMobEnchantOwnerMessage implements CustomPacketPayload, IPayloadHandler<RemoveMobEnchantOwnerMessage> {

    public static final StreamCodec<FriendlyByteBuf, RemoveMobEnchantOwnerMessage> STREAM_CODEC = CustomPacketPayload.codec(
            RemoveMobEnchantOwnerMessage::write, RemoveMobEnchantOwnerMessage::new
    );
    public static final Type<RemoveMobEnchantOwnerMessage> TYPE = new Type<>(EnchantWithMob.prefix("remove_mob_enchant_owner"));
    private int entityId;

    public RemoveMobEnchantOwnerMessage(Entity entity) {
        this.entityId = entity.getId();
    }

    public RemoveMobEnchantOwnerMessage(int id) {
        this.entityId = id;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    public RemoveMobEnchantOwnerMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    public void handle(RemoveMobEnchantOwnerMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
                if (entity != null && entity instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        cap.getEnchantCap().removeOwner(livingEntity);
                    }
                }
            });
    }
}