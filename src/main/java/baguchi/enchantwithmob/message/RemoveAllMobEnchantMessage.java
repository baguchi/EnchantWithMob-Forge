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

public class RemoveAllMobEnchantMessage implements CustomPacketPayload, IPayloadHandler<RemoveAllMobEnchantMessage> {

    public static final StreamCodec<FriendlyByteBuf, RemoveAllMobEnchantMessage> STREAM_CODEC = CustomPacketPayload.codec(
            RemoveAllMobEnchantMessage::write, RemoveAllMobEnchantMessage::new
    );
    public static final Type<RemoveAllMobEnchantMessage> TYPE = new Type<>(EnchantWithMob.prefix("remove_all_mob_enchant"));


    private int entityId;

    public RemoveAllMobEnchantMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    public RemoveAllMobEnchantMessage(Entity entity) {
        this.entityId = entity.getId();
    }

    public RemoveAllMobEnchantMessage(int id) {
        this.entityId = id;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handle(RemoveAllMobEnchantMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
                if (entity != null && entity instanceof LivingEntity livingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        cap.getEnchantCap().removeAllMobEnchant((LivingEntity) entity);
                    }
                }
            });
    }
}