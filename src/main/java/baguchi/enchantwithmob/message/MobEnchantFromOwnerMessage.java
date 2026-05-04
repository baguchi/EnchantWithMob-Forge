package baguchi.enchantwithmob.message;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.client.EnchantWithMobClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class MobEnchantFromOwnerMessage implements CustomPacketPayload, IPayloadHandler<MobEnchantFromOwnerMessage> {

    public static final StreamCodec<FriendlyByteBuf, MobEnchantFromOwnerMessage> STREAM_CODEC = CustomPacketPayload.codec(
            MobEnchantFromOwnerMessage::write, MobEnchantFromOwnerMessage::new
    );
    public static final CustomPacketPayload.Type<MobEnchantFromOwnerMessage> TYPE = new Type<>(EnchantWithMob.prefix("mob_enchant_from_owner"));


    private int entityId;
    private EntityReference<LivingEntity> owner;

    public MobEnchantFromOwnerMessage(Entity entity, EntityReference<LivingEntity> ownerEntity) {
        this.entityId = entity.getId();
        this.owner = ownerEntity;
    }

    public MobEnchantFromOwnerMessage(int id, EntityReference<LivingEntity> owner) {
        this.entityId = id;
        this.owner = owner;
    }


    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeJsonWithCodec(EntityReference.codec(), this.owner);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public MobEnchantFromOwnerMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readLenientJsonWithCodec(EntityReference.codec()));
    }

    @Override
    public void handle(MobEnchantFromOwnerMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
            LivingEntity ownerEntity = EntityReference.getLivingEntity(this.owner, Minecraft.getInstance().player.level());
            if (entity instanceof LivingEntity livingEntity) {
                if (entity instanceof IEnchantCap cap && ownerEntity != null) {
                        cap.getEnchantCap().addOwner(livingEntity, ownerEntity);
                        EnchantWithMobClientProxy.playEnchantBeamSound(livingEntity);
                    }
                }
            });
    }
}