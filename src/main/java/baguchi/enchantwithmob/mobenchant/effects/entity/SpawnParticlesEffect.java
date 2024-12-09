package baguchi.enchantwithmob.mobenchant.effects.entity;

import baguchi.enchantwithmob.mobenchant.effects.MobEnchantEntityEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record SpawnParticlesEffect(
        ParticleOptions particle,
        PositionSource horizontalPosition,
        PositionSource verticalPosition,
        VelocitySource horizontalVelocity,
        VelocitySource verticalVelocity,
        FloatProvider speed
) implements MobEnchantEntityEffect {
    public static final MapCodec<SpawnParticlesEffect> CODEC = RecordCodecBuilder.mapCodec(
            p_345987_ -> p_345987_.group(
                            ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesEffect::particle),
                            PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesEffect::horizontalPosition),
                            PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesEffect::verticalPosition),
                            VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesEffect::horizontalVelocity),
                            VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesEffect::verticalVelocity),
                            FloatProvider.CODEC.optionalFieldOf("speed", ConstantFloat.ZERO).forGetter(SpawnParticlesEffect::speed)
                    )
                    .apply(p_345987_, SpawnParticlesEffect::new)
    );

    public static PositionSource offsetFromEntityPosition(float offset) {
        return new PositionSource(PositionSourceType.ENTITY_POSITION, offset, 1.0F);
    }

    public static PositionSource inBoundingBox() {
        return new PositionSource(PositionSourceType.BOUNDING_BOX, 0.0F, 1.0F);
    }

    public static VelocitySource movementScaled(float movementScale) {
        return new VelocitySource(movementScale, ConstantFloat.ZERO);
    }

    public static VelocitySource fixedVelocity(FloatProvider velocity) {
        return new VelocitySource(0.0F, velocity);
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, @Nullable LivingEntity living, Entity entity, Vec3 origin) {
        RandomSource randomsource = entity.getRandom();
        Vec3 vec3 = entity.getKnownMovement();
        float f = entity.getBbWidth();
        float f1 = entity.getBbHeight();
        level.sendParticles(
                this.particle,
                this.horizontalPosition.getCoordinate(origin.x(), origin.x(), f, randomsource),
                this.verticalPosition.getCoordinate(origin.y(), origin.y() + (double) (f1 / 2.0F), f1, randomsource),
                this.horizontalPosition.getCoordinate(origin.z(), origin.z(), f, randomsource),
                0,
                this.horizontalVelocity.getVelocity(vec3.x(), randomsource),
                this.verticalVelocity.getVelocity(vec3.y(), randomsource),
                this.horizontalVelocity.getVelocity(vec3.z(), randomsource),
                (double) this.speed.sample(randomsource)
        );
    }

    @Override
    public MapCodec<SpawnParticlesEffect> codec() {
        return CODEC;
    }

    public static record PositionSource(PositionSourceType type, float offset, float scale) {
        public static final MapCodec<PositionSource> CODEC = RecordCodecBuilder.<PositionSource>mapCodec(
                        p_345074_ -> p_345074_.group(
                                        PositionSourceType.CODEC.fieldOf("type").forGetter(PositionSource::type),
                                        Codec.FLOAT.optionalFieldOf("offset", Float.valueOf(0.0F)).forGetter(PositionSource::offset),
                                        ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("scale", 1.0F).forGetter(PositionSource::scale)
                                )
                                .apply(p_345074_, PositionSource::new)
                )
                .validate(
                        p_345424_ -> p_345424_.type() == PositionSourceType.ENTITY_POSITION && p_345424_.scale() != 1.0F
                                ? DataResult.error(() -> "Cannot scale an entity position coordinate source")
                                : DataResult.success(p_345424_)
                );

        public double getCoordinate(double position, double center, float size, RandomSource random) {
            return this.type.getCoordinate(position, center, size * this.scale, random) + (double) this.offset;
        }
    }

    public static enum PositionSourceType implements StringRepresentable {
        ENTITY_POSITION("entity_position", (p_344963_, p_352938_, p_346310_, p_345258_) -> p_344963_),
        BOUNDING_BOX("in_bounding_box", (p_345669_, p_352951_, p_345281_, p_345162_) -> p_352951_ + (p_345162_.nextDouble() - 0.5) * (double) p_345281_);

        public static final Codec<PositionSourceType> CODEC = StringRepresentable.fromEnum(PositionSourceType::values);
        private final String id;
        private final CoordinateSource source;

        private PositionSourceType(String id, CoordinateSource source) {
            this.id = id;
            this.source = source;
        }

        public double getCoordinate(double position, double center, float size, RandomSource random) {
            return this.source.getCoordinate(position, center, size, random);
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }

        @FunctionalInterface
        interface CoordinateSource {
            double getCoordinate(double position, double center, float size, RandomSource random);
        }
    }

    public static record VelocitySource(float movementScale, FloatProvider base) {
        public static final MapCodec<VelocitySource> CODEC = RecordCodecBuilder.mapCodec(
                p_346005_ -> p_346005_.group(
                                Codec.FLOAT.optionalFieldOf("movement_scale", Float.valueOf(0.0F)).forGetter(VelocitySource::movementScale),
                                FloatProvider.CODEC.optionalFieldOf("base", ConstantFloat.ZERO).forGetter(VelocitySource::base)
                        )
                        .apply(p_346005_, VelocitySource::new)
        );

        public double getVelocity(double scale, RandomSource random) {
            return scale * (double) this.movementScale + (double) this.base.sample(random);
        }
    }
}
