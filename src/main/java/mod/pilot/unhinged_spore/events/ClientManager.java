package mod.pilot.unhinged_spore.events;

import mod.pilot.unhinged_spore.UnhingedSpore;
import mod.pilot.unhinged_spore.entity.UnhingedEntities;
import mod.pilot.unhinged_spore.entity.client.mobs.JortRenderer;
import mod.pilot.unhinged_spore.entity.client.mobs.SpungusRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnhingedSpore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientManager {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(UnhingedEntities.JORT.get(), JortRenderer::new);
        event.registerEntityRenderer(UnhingedEntities.SPUNGUS.get(), SpungusRenderer::new);
    }
}
