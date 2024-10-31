package mod.pilot.unhinged_spore.events;

import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class UnhingedEventBus {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(UnhingedEntities.JORT.get(), JortEntity.createAttributes().build());
        event.put(UnhingedEntities.SPUNGUS.get(), SpungusEntity.createAttributes().build());
    }
}
