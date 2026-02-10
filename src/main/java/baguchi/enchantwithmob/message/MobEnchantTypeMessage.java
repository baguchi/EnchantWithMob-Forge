package baguchi.enchantwithmob.message;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.api.MobEnchantType;
import baguchi.enchantwithmob.data.resources.registries.MobEnchantTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class MobEnchantTypeMessage implements CustomPacketPayload, IPayloadHandler<MobEnchantTypeMessage> {

    public static final StreamCodec<FriendlyByteBuf, MobEnchantTypeMessage> STREAM_CODEC = CustomPacketPayload.codec(
            MobEnchantTypeMessage::write, MobEnchantTypeMessage::new
    );
    public static final Type<MobEnchantTypeMessage> TYPE = new Type<>(EnchantWithMob.prefix("ancient"));

    private int entityId;
    private ResourceKey<MobEnchantType> mobEnchantType;

    public MobEnchantTypeMessage(Entity entity, ResourceKey<MobEnchantType> mobEnchantType) {
        this.entityId = entity.getId();
        this.mobEnchantType = mobEnchantType;
    }

    public MobEnchantTypeMessage(int id, ResourceKey<MobEnchantType> mobEnchantType) {
        this.entityId = id;
        this.mobEnchantType = mobEnchantType;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeResourceKey(this.mobEnchantType);
    }

    public MobEnchantTypeMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readResourceKey(MobEnchantTypes.MOB_ENCHANT_TYPE_REGISTRY_KEY));
    }

    @Override
    public void handle(MobEnchantTypeMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            if (entity != null && entity instanceof LivingEntity livingEntity) {
                if (livingEntity instanceof IEnchantCap cap) {
                    cap.getEnchantCap().setEnchantType((LivingEntity) entity, message.mobEnchantType);
                }

            }
        });
    }
}