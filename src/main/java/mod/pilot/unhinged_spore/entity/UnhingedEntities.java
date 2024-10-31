package mod.pilot.unhinged_spore.entity;

import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class UnhingedEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UnhingedSpore.MOD_ID);

    public static final RegistryObject<EntityType<JortEntity>> JORT =
            ENTITY_TYPES.register("jort", () -> EntityType.Builder.of(JortEntity::new, MobCategory.MONSTER)
                    .sized(1f, 1f).build("jort"));
    public static final RegistryObject<EntityType<SpungusEntity>> SPUNGUS =
            ENTITY_TYPES.register("spungus", () -> EntityType.Builder.of(SpungusEntity::new, MobCategory.MONSTER)
                    .sized(1.2f, 2.2f).build("spungus"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
