package baguchi.enchantwithmob.message;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class AncientMessage implements CustomPacketPayload, IPayloadHandler<AncientMessage> {

    public static final StreamCodec<FriendlyByteBuf, AncientMessage> STREAM_CODEC = CustomPacketPayload.codec(
            AncientMessage::write, AncientMessage::new
    );
    public static final Type<AncientMessage> TYPE = new Type<>(EnchantWithMob.prefix("ancient"));

    private int entityId;
    private boolean isAncient;

    public AncientMessage(Entity entity, boolean ancient) {
        this.entityId = entity.getId();
        this.isAncient = ancient;
    }

    public AncientMessage(int id, boolean ancient) {
        this.entityId = id;
        this.isAncient = ancient;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.isAncient);
    }

    public AncientMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBoolean());
    }

    @Override
    public void handle(AncientMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof LivingEntity livingEntity) {
                if (livingEntity instanceof IEnchantCap cap) {
                    cap.getEnchantCap().setEnchantType((LivingEntity) entity, message.isAncient ? MobEnchantCapability.EnchantType.ANCIENT : MobEnchantCapability.EnchantType.NORMAL);
                }

            }
        });
    }
}