package baguchi.enchantwithmob.registry;

import baguchi.enchantwithmob.EnchantWithMob;
import baguchi.enchantwithmob.entity.Enchanter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = EnchantWithMob.MODID)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EnchantWithMob.MODID);

    public static final Supplier<EntityType<Enchanter>> ENCHANTER = ENTITIES_REGISTRY.register("enchanter", () -> EntityType.Builder.of(Enchanter::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build(prefix("enchanter")));

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(ENCHANTER.get(), Enchanter.createAttributeMap().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(ENCHANTER.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static ResourceKey<EntityType<?>> prefix(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(EnchantWithMob.MODID, path));
    }
}
