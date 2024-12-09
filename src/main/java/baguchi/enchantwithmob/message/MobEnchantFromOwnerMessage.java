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

public class MobEnchantFromOwnerMessage implements CustomPacketPayload, IPayloadHandler<MobEnchantFromOwnerMessage> {

    public static final StreamCodec<FriendlyByteBuf, MobEnchantFromOwnerMessage> STREAM_CODEC = CustomPacketPayload.codec(
            MobEnchantFromOwnerMessage::write, MobEnchantFromOwnerMessage::new
    );
    public static final Type<MobEnchantFromOwnerMessage> TYPE = new Type<>(EnchantWithMob.prefix("mob_enchant_from_owner"));


    private int entityId;
    private int ownerID;

    public MobEnchantFromOwnerMessage(Entity entity, Entity ownerEntity) {
        this.entityId = entity.getId();
        this.ownerID = ownerEntity.getId();
    }

    public MobEnchantFromOwnerMessage(int id, int ownerID) {
        this.entityId = id;
        this.ownerID = ownerID;
    }


    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.ownerID);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public MobEnchantFromOwnerMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt());
    }

    @Override
    public void handle(MobEnchantFromOwnerMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            Entity ownerEntity = Minecraft.getInstance().player.level().getEntity(message.ownerID);
            if (entity instanceof LivingEntity livingEntity && ownerEntity instanceof LivingEntity) {
                    if (livingEntity instanceof IEnchantCap cap) {
                        cap.getEnchantCap().addOwner((LivingEntity) entity, (LivingEntity) ownerEntity);
                    }
                }
            });
    }
}