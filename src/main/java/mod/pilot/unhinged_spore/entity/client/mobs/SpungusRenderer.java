package mod.pilot.unhinged_spore.entity.client.mobs;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.pilot.unhinged_spore.entity.mobs.JortEntity;
import mod.pilot.unhinged_spore.entity.mobs.SpungusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpungusRenderer extends GeoEntityRenderer<SpungusEntity> {
    public SpungusRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new SpungusModel());
    }
}
