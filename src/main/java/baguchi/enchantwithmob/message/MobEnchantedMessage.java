package baguchi.enchantwithmob.message;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantHandler;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import baguchi.enchantwithmob.registry.ModRegistries;
import baguchi.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.Optional;

public class MobEnchantedMessage implements CustomPacketPayload, IPayloadHandler<MobEnchantedMessage> {

    public static final StreamCodec<FriendlyByteBuf, MobEnchantedMessage> STREAM_CODEC = CustomPacketPayload.codec(
            MobEnchantedMessage::write, MobEnchantedMessage::new
    );
    public static final CustomPacketPayload.Type<MobEnchantedMessage> TYPE = new Type<>(EnchantWithMob.prefix("mob_enchant"));


    private int entityId;
    private ResourceLocation enchantType;
    private int level;

    public MobEnchantedMessage(Entity entity, MobEnchantHandler enchantType) {
        this.entityId = entity.getId();
        this.enchantType = enchantType.getMobEnchant().getKey().location();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantedMessage(int id, MobEnchantHandler enchantType) {
        this.entityId = id;
        this.enchantType = enchantType.getMobEnchant().getKey().location();
        this.level = enchantType.getEnchantLevel();
    }

    public MobEnchantedMessage(Entity entity, Holder<MobEnchant> enchantType, int level) {
        this.entityId = entity.getId();
        this.enchantType = enchantType.getKey().location();
        this.level = level;
    }

    public MobEnchantedMessage(int entity, ResourceLocation enchantType, int level) {
        this.entityId = entity;
        this.enchantType = enchantType;
        this.level = level;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeResourceLocation(this.enchantType);
        buffer.writeInt(this.level);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public MobEnchantedMessage(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readResourceLocation(), buffer.readInt());
    }

    @Override
    public void handle(MobEnchantedMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);


            if (entity != null && entity instanceof LivingEntity livingEntity) {
                Optional<Holder.Reference<MobEnchant>> mobEnchant = entity.registryAccess().lookupOrThrow(ModRegistries.MOB_ENCHANT).get(message.enchantType);
                    if (livingEntity instanceof IEnchantCap cap) {
                        if (!MobEnchantUtils.findMobEnchantHandler(cap.getEnchantCap().getMobEnchants(), mobEnchant.get())) {
                            cap.getEnchantCap().addMobEnchant((LivingEntity) entity, mobEnchant.get(), message.level);
                        }
                    }
                }
        });
    }
}