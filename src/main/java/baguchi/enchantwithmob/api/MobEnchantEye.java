package baguchi.enchantwithmob.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public record MobEnchantEye(HolderSet<EntityType<?>> entityType, ResourceLocation texture) {
    public static final Codec<MobEnchantEye> DIRECT_CODEC = RecordCodecBuilder
            .create(instance -> instance
                    .group(RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).fieldOf("entity_type")
                                    .forGetter(MobEnchantEye::entityType),
                            ResourceLocation.CODEC.fieldOf("texture")
                                    .forGetter(MobEnchantEye::texture))
                    .apply(instance, MobEnchantEye::new));
}
