package baguchi.enchantwithmob.message;

import baguchi.enchantwithmob.EnchantWithMob;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SoulParticleMessage implements CustomPacketPayload, IPayloadHandler<SoulParticleMessage> {

    public static final StreamCodec<FriendlyByteBuf, SoulParticleMessage> STREAM_CODEC = CustomPacketPayload.codec(
            SoulParticleMessage::write, SoulParticleMessage::new
    );
	public static final Type<SoulParticleMessage> TYPE = new Type<>(EnchantWithMob.prefix("soul_particle"));


	private int entityId;

	public SoulParticleMessage(Entity entity) {
		this.entityId = entity.getId();
	}

	public SoulParticleMessage(int id) {
		this.entityId = id;
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public SoulParticleMessage(FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	@Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
	}

    public void handle(SoulParticleMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
			Entity entity = Minecraft.getInstance().level.getEntity(message.entityId);
			if (entity != null) {
				for (int i = 0; i < 4; i++) {
					entity.level().addParticle(ParticleTypes.SCULK_SOUL, entity.getRandomX(0.5D), entity.getRandomY(), entity.getRandomZ(0.5D), 0.0F, 0.1F, 0.0F);
				}
			}
		});
    }
}